package com.mygdx.game.db;

import com.mygdx.game.model.GamingProcess;
import com.mygdx.game.model.maps.Map;
import com.mygdx.game.model.players.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

public class GameDatabaseTest {
    private static GameDatabase gameDatabase;
    private static DBController dbController;

    @BeforeEach
    void init() throws SQLException {
        dbController = new DBController(":memory:");
        dbController.openConnection();
        gameDatabase = new GameDatabase(dbController.getConnection());
        gameDatabase.createSchema();
    }

    @ParameterizedTest
    @MethodSource("playerNames")
    void addingPlayersTest(List<String> names) throws SQLException {
        Map map = mock(Map.class);
        List<Player> players = names.stream()
            .map(ele -> new Player(ele, map, null))
            .toList();

        gameDatabase.insertPlayers(players);

        Connection connection = dbController.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT name FROM player");

        for (Player player : players) {
            rs.next();
            assertThat(rs.getString(1)).isEqualTo(player.name);
        }
        statement.close();
    }

    static Stream<List<String>> playerNames() {
        return Stream.of(
            Arrays.asList("Ivan", "Elena", "Sergey", "Alex", "John"),
            Arrays.asList("player1", "player2")
        );
    }

    @ParameterizedTest
    @MethodSource("gameArguments")
    void creatingGameTest(int playerQty, long seed, int mapWidth, int mapHeight) throws SQLException {
        GamingProcess game = mock(GamingProcess.class);
        int gameId = gameDatabase.insertGame(game, playerQty, seed, mapWidth, mapHeight);
        assertThat(gameId).isOne();

        Connection connection = dbController.getConnection();
        ResultSet rs = connection.createStatement().executeQuery("" +
            "SELECT id, players_qty, start_timestamp, end_timestamp, map_seed, map_width, map_height FROM game WHERE id = 1"
        );

        assertThat(rs.getInt(1)).isOne();
        assertThat(rs.getInt(2)).isEqualTo(playerQty);
        assertThat(rs.getTimestamp(3)).isNotNull();
        assertThat(rs.getTimestamp(4)).isNull();
        assertThat(rs.getInt(5)).isEqualTo(seed);
        assertThat(rs.getInt(6)).isEqualTo(mapWidth);
        assertThat(rs.getInt(7)).isEqualTo(mapHeight);
    }

    static Stream<Arguments> gameArguments() {
        return Stream.of(
            arguments(2, 231, 10, 10),
            arguments(10, 999, 50, 50)
        );
    }

    @AfterEach
    void tearDown() throws SQLException {
        dbController.closeConnection();
    }
}
