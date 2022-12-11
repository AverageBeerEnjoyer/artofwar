package com.mygdx.game.db;

import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GameDatabaseTest {
    private static GameDatabase gameDatabase;
    private static DBController dbController;

    @BeforeAll
    static void init() throws SQLException {
        dbController = new DBController(":memory:");
        dbController.openConnection();
        gameDatabase = new GameDatabase(dbController.getConnection());
        gameDatabase.createSchema();
    }

    @Test
    void addingPlayers() throws SQLException {
        Map map = mock(Map.class);
        Collection<String> names = Arrays.asList("Ivan", "Elena", "Sergey", "Alex", "John");
        List<Player> players = names.stream()
            .map(ele -> new Player(ele, map, null))
            .toList();

        gameDatabase.insertPlayers(players);

        Connection connection = dbController.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT name FROM player");

        for (String name: names) {
            rs.next();
            assertThat(rs.getString(1)).isEqualTo(name);
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        dbController.closeConnection();
    }
}
