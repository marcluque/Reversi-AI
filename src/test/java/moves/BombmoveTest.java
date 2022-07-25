package moves;

import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import shared.TestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BombmoveTest {

    private static final String BASE = "src/test/resources/bombmoves";

    private static Stream<Arguments> testBombMoveCorrectness() {
        // Arguments follow the structure: (test-id, x-coordinate, y-coordinate, player-number)
        return Stream.of(
                Arguments.of(0, 0, 0, '1'),
                Arguments.of(1, 5, 5, '1'),
                Arguments.of(2, 0, 0, '1'),
                Arguments.of(3, 5, 5, '1'),
                Arguments.of(4, 2, 0, '1'),
                Arguments.of(5, 2, 0, '1'),
                Arguments.of(6, 2, 2, '1'),
                Arguments.of(7, 0, 0, '1'),
                Arguments.of(8, 0, 1, '1'),
                Arguments.of(9, 0, 1, '1'),
                Arguments.of(10, 5, 5, '1'),
                Arguments.of(11, 5, 5, '1'),
                Arguments.of(12, 5, 5, '1')
        );
    }

    @ParameterizedTest
    @MethodSource("testBombMoveCorrectness")
    public void testBombMove(int testNumber, int x, int y, char player) {
        String beforeMapPath = String.format("%s/bombmove_test%d_before.txt", BASE, testNumber);
        GameInstance.setMap(MapLoader.generateMapFromMapFile(beforeMapPath));
        Map.setPhase(2);

        Set<Coordinate> capturableStones = new HashSet<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), x, y, player, false,
                true, capturableStones));

        System.out.println(MapUtil.mapToPrintableString(GameInstance.getMap().getGameField()));

        var s = System.nanoTime();
        Move.executeMove(GameInstance.getMap(), x, y, 0, player, capturableStones);
        var e = System.nanoTime();
        System.out.println("TIME: " + ((e - s) / 1_000) + " µs");

        System.out.println(MapUtil.mapToPrintableString(GameInstance.getMap().getGameField()));

        String afterMapPath = String.format("%s/bombmove_test%d_after.txt", BASE, testNumber);
        System.out.println(MapUtil.mapToPrintableString(MapLoader.generateArrayFromMapFile(afterMapPath)));
        Assertions.assertTrue(TestUtils.mapEquals(MapLoader.generateArrayFromMapFile(afterMapPath), GameInstance.getMap().getGameField()
        ));
    }

    private static Stream<Arguments> testBombMoveValidity() {
        // Arguments follow the structure: (test-id, x-coordinate, y-coordinate, player-number, move-is-valid)
        return Stream.of(
                Arguments.of(4, 1, 0, '1', true),
                Arguments.of(4, 2, 0, '1', true),
                Arguments.of(4, 3, 0, '1', true),
                Arguments.of(4, 0, 0, '1', false)
        );
    }

    @ParameterizedTest
    @MethodSource("testBombMoveValidity")
    public void testBombMoveValidity(int testNumber, int x, int y, char player, boolean isValid) {
        String beforeMapPath = String.format("%s/bombmove_test%d_before.txt", BASE, testNumber);
        GameInstance.setMap(MapLoader.generateMapFromMapFile(beforeMapPath));
        Map.setPhase(2);

        Set<Coordinate> capturableStones = new HashSet<>();
        Assertions.assertEquals(Move.isMoveValid(GameInstance.getMap(), x, y, player, false,
                true, capturableStones), isValid);
    }
}
