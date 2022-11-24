package com.mygdx.game.db;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {
    private static final String DB_NAME = "artofwar.db";
    private static final String DB_URL = String.format("jdbc:sqlite:%s", DB_NAME);
    private static Connection connection = null;

    /**
     * Opens a connection to database instance on DB_URL.
     * @throws SQLException
     */
    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(DB_URL, config.toProperties());
        }
    }

    /**
     * Closes the current connection to database instance.
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    /**
     * Creates the schema for game
     * @throws SQLException
     */
    public void createSchema() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
            "CREATE TABLE IF NOT EXISTS player" +
                "(" +
                "    id   INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(63) NOT NULL" +
                ");"
        );
        statement.execute(
            "CREATE TABLE IF NOT EXISTS game" +
                "(" +
                "    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    players_qty     INTEGER NOT NULL," +
                "    start_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    end_timestamp   DATETIME," +
                "    map_seed        INTEGER NOT NULL" +
                ");"
        );
        statement.execute(
            "CREATE TABLE IF NOT EXISTS move" +
                "(" +
                "    id                INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    number            INTEGER," +
                "    current_player_id INTEGER NOT NULL," +
                "    game_id           INTEGER NOT NULL," +
                "    timestamp         DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    action            TEXT," +
                "    FOREIGN KEY (current_player_id) REFERENCES player (id)," +
                "    FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE" +
                ");"
        );
        statement.execute(
            "CREATE TABLE IF NOT EXISTS resource" +
                "(" +
                "    move_id      INTEGER NOT NULL," +
                "    player_id    INTEGER NOT NULL," +
                "    is_game_over BOOLEAN DEFAULT false," +
                "    data         BLOB," +
                "    FOREIGN KEY (move_id) REFERENCES move (id) ON DELETE CASCADE," +
                "    FOREIGN KEY (player_id) REFERENCES player (id)," +
                "    PRIMARY KEY (move_id, player_id)" +
                ");"
        );
        statement.close();
    }
}
