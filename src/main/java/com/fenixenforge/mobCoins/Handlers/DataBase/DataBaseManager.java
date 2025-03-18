package com.fenixenforge.mobCoins.Handlers.DataBase;

import com.fenixenforge.mobCoins.ymlData.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

    private final JavaPlugin plugin;
    private Connection connection;
    private boolean useMySQL;

    public DataBaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        useMySQL = Config.useMySQL();
        try {
            if (useMySQL) {
                connectMySQL();
            } else {
                connectSQLite();
            }
            CreateTables();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error al conectar a la base de datos:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void connectSQLite()
            throws SQLException {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File dbFile = new File(dataFolder, "mobcoins.db");
        String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        connection = DriverManager.getConnection(url);
        plugin.getLogger().info("Conectado a SQLite(" + dbFile.getAbsolutePath() + ")");
    }

    private void connectMySQL()
            throws SQLException {
        String host = Config.getMySQLHost();
        int port = Config.getMySQLPort();
        String database = Config.getMySQLDatabase();
        String username = Config.getMySQLUsername();
        String password = Config.getMySQLPassword();

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";
        connection = DriverManager.getConnection(url, username, password);
        plugin.getLogger().info("Conectado a MySQL(" + host + ":" + port + "/" + database + ")");
    }

    private void CreateTables() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" + "uuid TEXT PRIMARY KEY, " + "name TEXT, " + "mobcoins INTEGER DEFAULT " + Config.getCoinsDefault() + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
