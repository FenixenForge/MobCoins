Características principales
Recompensas por mobs: Los jugadores ganan monedas al matar mobs (personalizables por tipo de mob).

Comandos:

/mobcoins - Ver tus monedas.

/mobcoins give <jugador> <cantidad> - Dar monedas a un jugador (OP).

/mobcoins remove <jugador> <cantidad> - Quitar monedas a un jugador (OP).

/mobcoins pay <jugador> <cantidad> - Pagar monedas a un jugador (All).

/mobcoins shop - Abrir una tienda personalizable (opcional).

/mobcoins help - Mira todos los comandos disponibles

/mobcoins balance - Mira tu balance

/mobcoins balance-top - Mira una lista de top 10

/mobcoins reload - Recarga los archivos yml del plugin (OP)

# Configuración flexible

Ajusta la cantidad de monedas que dropea cada mob.

Habilita o deshabilita mobs específicos.

Configura mensajes y sonidos al recibir monedas.

Compatibilidad: Funciona en servidores Spigot/Paper 1.18+ (Java 17).

Instalación
Descarga el archivo .jar del plugin desde aquí (agrega el enlace de descarga).

Coloca el archivo .jar en la carpeta plugins de tu servidor.

Reinicia el servidor.

¡Listo! El plugin generará automáticamente un archivo de configuración (config.yml) en la carpeta plugins/MobCoins.

Configuración
El archivo config.yml permite personalizar el plugin. Aquí un ejemplo básico:

yaml
Copy

# Mob Coins - Configuración

# Monedas por mob

    mobs:
    
        #nombre para identificar al mob
        creeper:
        #Nombre del mob en mayusculas
        mob: CREEPER
        #Chance o probabilidad para que el mob suelte monedas
        chance: 10
        #Cantidad de monedas que dara el mob
        coins: 1

        zombie:
        mob: ZOMBIE
        chance: 20
        coins: 2

# Mensajes

messages:
mob_kill:
success: "&aHas recibido %coins% coins por matar un %mob%."

Comandos y Permisos
Comandos
/mobcoins - Muestra tu saldo de monedas.

/mobcoins give <jugador> <cantidad> - Da monedas a un jugador (requiere permisos).

/mobcoins remove <jugador> <cantidad> - Quita monedas a un jugador (requiere permisos).

/mobcoins reload - Recarga la configuración (requiere permisos).

Permisos
mobcoins.admin

Contribuciones
¡Las contribuciones son bienvenidas! Si tienes ideas para mejorar el plugin o encuentras algún error, abre un issue o envía un pull request en GitHub (agrega el enlace a tu
repositorio).

Soporte
Si necesitas ayuda o tienes preguntas, contáctame en:

Discord:  (https://discord.gg/bMXjeJQppj)

Email: fenixenforge@gmail.com

Licencia
Este proyecto está bajo la licencia MIT. Para más detalles, consulta el archivo LICENSE.

¡Gracias por usar Mob Coins! Esperamos que disfrutes el plugin y que haga tu servidor más emocionante. 🎮💰