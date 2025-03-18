package com.fenixenforge.mobCoins.Handlers.DataBase;

import com.fenixenforge.mobCoins.MobCoins;
import com.fenixenforge.mobCoins.ymlData.Config;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final Connection connection;

    public UserManager(MobCoins plugin) {
        this.connection = plugin.getDbManager().getConnection();
    }

    public void CreateUser(Player player) {
        String sql = "INSERT OR IGNORE INTO users (uuid, name, mobcoins) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            ps.setInt(3, Config.getCoinsDefault());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int BalanceUser(Player player) {
        String sql = "SELECT mobcoins FROM users WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("mobcoins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void AddBalance(Player player, int amount) {
        int current = BalanceUser(player);
        int newBalance = current + amount;
        UpdateBalance(player, newBalance);
    }

    public void RemoveBalance(Player player, int amount) {
        int current = BalanceUser(player);
        int newBalance = Math.max(0, current - amount);
        UpdateBalance(player, newBalance);
    }

    public void UpdateBalance(Player player, int newBalance) {
        String sql = "UPDATE users SET mobcoins = ? WHERE uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newBalance);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> BalanceTop() {
        List<String> topBalance = new ArrayList<>();
        String sql = "SELECT name, mobcoins FROM users ORDER BY mobcoins DESC LIMIT 10";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                int balance = rs.getInt("mobcoins");
                topBalance.add(rank + ". " + name + " - " + balance + " coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topBalance;
    }
}
