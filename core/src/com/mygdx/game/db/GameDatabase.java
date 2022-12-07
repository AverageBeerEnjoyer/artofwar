package com.mygdx.game.db;

import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.players.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GameDatabase {
    private final Connection connection;

    public GameDatabase(Connection connection) throws SQLException {
        this.connection = connection;
        createSchema();
    }

    /**
     * Creates the schema for game
     *
     * @throws SQLException
     */
    public void createSchema() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
            "CREATE TABLE IF NOT EXISTS player" +
                "(" +
                "    id   INTEGER     NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    name VARCHAR(63) NOT NULL UNIQUE" +
                ");"
        );
        statement.execute(
            "CREATE TABLE IF NOT EXISTS game" +
                "(" +
                "    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    players_qty     INTEGER NOT NULL," +
                "    start_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    end_timestamp   DATETIME," +
                "    map_seed        INTEGER NOT NULL," +
                "    map_width       INTEGER NOT NULL," +
                "    map_height      INTEGER NOT NULL" +
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
        connection.commit();
        statement.close();
    }

    public void insertPlayers(List<Player> players) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT OR IGNORE INTO player (name) VALUES (?) RETURNING id");
        PreparedStatement getIdStatement = connection.prepareStatement(
            "SELECT id FROM player WHERE name = ?"
        );
        for (Player player : players) {
            statement.setString(1, player.name);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                getIdStatement.setString(1, player.name);
                rs = getIdStatement.executeQuery();
            }
            player.setId(rs.getInt(1));
        }
        statement.close();
        getIdStatement.close();
        connection.commit();
    }

    public void insertGame(GamingProcess gamingProcess, int playerQty, long seed, int mapWidth, int mapHeight) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO game (players_qty, map_seed, map_width, map_height) VALUES (?, ?, ?, ?) RETURNING id"
        );
        statement.setInt(1, playerQty);
        statement.setInt(2, (int) seed);
        statement.setInt(3, mapWidth);
        statement.setInt(4, mapHeight);
        ResultSet rs = statement.executeQuery();
        gamingProcess.setId(rs.getInt(1));
        statement.close();
        connection.commit();
    }
}
