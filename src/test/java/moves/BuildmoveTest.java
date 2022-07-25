package moves;

import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import shared.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BuildmoveTest {

    private static final String BASE = "src/test/resources/buildmoves";

    private static Stream<Arguments> validBuildMapMovePlayer() {
        // Arguments follow the structure: (test-id, x-coordinate, y-coordinate, player-number)
        return Stream.of(
                Arguments.of(0, 25, 2, '6'),
                Arguments.of(1, 35, 14, '8'),
                Arguments.of(2, 0, 39, '4'),
                Arguments.of(3, 16, 0, '5'),
                Arguments.of(4, 22, 19, '5'),
                Arguments.of(5, 34, 0, '1'),
                Arguments.of(6, 14, 1, '7')
        );
    }

    @ParameterizedTest
    @MethodSource("validBuildMapMovePlayer")
    public void testValidBuildMoves(int testNumber, int x, int y, char player) {
        String beforeMapPath = String.format("%s/buildmove_test%d_before.txt", BASE, testNumber);

        GameInstance.setMap(MapLoader.generateMapFromMapFile(beforeMapPath));
        Map.setPhase(1);

        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), x, y, player,
                false, true, capturableTiles));

        Move.executeMove(GameInstance.getMap(), x, y, 0, player, capturableTiles);

        String mapPath = String.format("%s/buildmove_test%d_after.txt", BASE, testNumber);
        Assertions.assertTrue(TestUtils.mapEquals(MapLoader.generateArrayFromMapFile(mapPath), GameInstance.getMap().getGameField()
        ));
    }

    @Test
    public void testOverrideStones() {
        String beforeMapPath = String.format("%s/override_stone_test0_before.txt", BASE);

        GameInstance.setMap(MapLoader.generateMapFromMapFile(beforeMapPath));
        Map.setPhase(1);

        // Player 1 overrides stone of player 2
        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), 2, 1, '1',
                false, true, capturableTiles));
        Move.executeMove(GameInstance.getMap(), 2, 1, 0, '1', capturableTiles);
        char[][] expected = {
                {'0', '0', 'x'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(TestUtils.mapEquals(expected, GameInstance.getMap().getGameField()));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(2, GameInstance.getMap().getOverrideStones()[1]);

        // Player 1 overrides his own stone
        GameInstance.getMap().getGameField()[1][1] = '2';
        capturableTiles = new ArrayList<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), 2, 1, '1',
                false, true, capturableTiles));
        Move.executeMove(GameInstance.getMap(), 2, 1, 0, '1', capturableTiles);
        char[][] expected2 = {
                {'0', '0', 'x'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(TestUtils.mapEquals(expected2, GameInstance.getMap().getGameField()));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(1, GameInstance.getMap().getOverrideStones()[1]);

        // Player 1 overrides an expansion tile/stone, without enclosing a path (Expansion rule)
        capturableTiles = new ArrayList<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), 2, 0, '1',
                false, true, capturableTiles));
        Move.executeMove(GameInstance.getMap(), 2, 0, 0, '1', capturableTiles);
        char[][] expected3 = {
                {'0', '0', '1'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(TestUtils.mapEquals(expected3, GameInstance.getMap().getGameField()));

        // Move should be invalid because no override stones are left
        GameInstance.getMap().getGameField()[1][1] = '2';
        capturableTiles = new ArrayList<>();
        Assertions.assertFalse(Move.isMoveValid(GameInstance.getMap(), 1, 1, '1',
                false, true, capturableTiles));
        Move.executeMove(GameInstance.getMap(), 1, 1, '1', '0', capturableTiles);
        char[][] expected4 = {
                {'0', '0', '1'},
                {'1', '2', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(TestUtils.mapEquals(expected4, GameInstance.getMap().getGameField()));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(0, GameInstance.getMap().getOverrideStones()[1]);
    }
}
