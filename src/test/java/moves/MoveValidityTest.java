package moves;

import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveValidityTest {

    private static final String BASE = "src/test/resources/maps";

    private static Stream<Arguments> validBuildMapMovePlayer() {
        return Stream.of(
                Arguments.of(0, 25, 2, '6'),
                Arguments.of(1, 35, 14, '8'),
                Arguments.of(2, 0, 39, '4'),
                Arguments.of(3, 16, 0, '5'),
                Arguments.of(4, 22, 19, '5'),
                Arguments.of(5, 34, 0, '1'),
                Arguments.of(6, 14, 1, '7'));
    }

    @ParameterizedTest
    @MethodSource("validBuildMapMovePlayer")
    public void testValidBuildMoves(int testNumber, int x, int y, char player) {
        String mapString = null;
        try {
            InputStream mapStream = Files.newInputStream(Path.of(String.format("%s/%s", BASE,
                    String.format("buildmoves/before/map23_test%d_before.txt", testNumber))).toAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(mapStream));
            mapString = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(mapString);

        GameInstance.setMap(MapLoader.generateMapFromString(mapString));
        Map.setPhase(1);

        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(AbstractMove.isMoveValid(GameInstance.getMap(), x, y, player,
                false, true, capturableTiles));

        AbstractMove.executeMove(GameInstance.getMap(), x, y, 0, player, capturableTiles);

        String mapPath = String.format("%s/buildmoves/after/map23_test%d_after.txt", BASE, testNumber);
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), MapLoader.generateArrayFromMapFile(mapPath)));
    }

    private static Stream<Arguments> validBombMapMovePlayer() {
        return Stream.of();
    }

    @ParameterizedTest
    @MethodSource("validBombMapMovePlayer")
    public void testBombMove(int testNumber, int x, int y, char player) {
        String mapString = null;
        try {
            String mapPath = String.format("%s/bombmoves/before/map23_test%d_before.txt", BASE, testNumber);
            InputStream mapStream = Files.newInputStream(Path.of(mapPath).toAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(mapStream));
            mapString = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(mapString);

        GameInstance.setMap(MapLoader.generateMapFromString(mapString));
        Map.setPhase(2);

        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(AbstractMove.isMoveValid(GameInstance.getMap(), x, y, player,
                false, true, capturableTiles));

        AbstractMove.executeMove(GameInstance.getMap(), x, y, 0, player, capturableTiles);

        String mapPath = String.format("%s/bombmoves/after/map23_test%d_after.txt", BASE, testNumber);
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), MapLoader.generateArrayFromMapFile(mapPath)));
    }

    @Test
    public void testOverrideStones() {
        String mapString = null;
        try {
            String mapPath = String.format("%s/buildmoves/before/overrideStones.txt", BASE);
            InputStream mapStream = Files.newInputStream(Path.of(mapPath).toAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader(mapStream));
            mapString = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(mapString);

        GameInstance.setMap(MapLoader.generateMapFromString(mapString));
        Map.setPhase(1);

        // Player 1 overrides stone of player 2
        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(AbstractMove.isMoveValid(GameInstance.getMap(), 2, 1, '1',
                false, true, capturableTiles));
        AbstractMove.executeMove(GameInstance.getMap(), 2, 1, 0, '1', capturableTiles);
        char[][] expected = {
                {'0', '0', 'x'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), expected));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(2, GameInstance.getMap().getOverrideStones()[1]);

        // Player 1 overrides his own stone
        GameInstance.getMap().getGameField()[1][1] = '2';
        capturableTiles = new ArrayList<>();
        Assertions.assertTrue(AbstractMove.isMoveValid(GameInstance.getMap(), 2, 1, '1',
                false, true, capturableTiles));
        AbstractMove.executeMove(GameInstance.getMap(), 2, 1, 0, '1', capturableTiles);
        char[][] expected2 = {
                {'0', '0', 'x'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), expected2));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(1, GameInstance.getMap().getOverrideStones()[1]);

        // Player 1 overrides an expansion tile/stone, without enclosing a path (Expansion rule)
        capturableTiles = new ArrayList<>();
        Assertions.assertTrue(AbstractMove.isMoveValid(GameInstance.getMap(), 2, 0, '1',
                false, true, capturableTiles));
        AbstractMove.executeMove(GameInstance.getMap(), 2, 0, 0, '1', capturableTiles);
        char[][] expected3 = {
                {'0', '0', '1'},
                {'1', '1', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), expected3));

        // Move should be invalid because no override stones are left
        GameInstance.getMap().getGameField()[1][1] = '2';
        capturableTiles = new ArrayList<>();
        Assertions.assertFalse(AbstractMove.isMoveValid(GameInstance.getMap(), 1, 1, '1',
                false, true, capturableTiles));
        AbstractMove.executeMove(GameInstance.getMap(), 1, 1, '1', '0', capturableTiles);
        char[][] expected4 = {
                {'0', '0', '1'},
                {'1', '2', '1'},
                {'0', '0', '0'}};
        Assertions.assertTrue(mapEquals(GameInstance.getMap().getGameField(), expected4));

        // Check whether the number of override stones left is correct
        Assertions.assertEquals(0, GameInstance.getMap().getOverrideStones()[1]);
    }


    private boolean mapEquals(char[][] actualMap, char[][] expectedMap) {
        for (int i = 0; i < actualMap.length; i++) {
            for (int j = 0; j < actualMap[0].length; j++) {
                if (expectedMap[i][j] != actualMap[i][j]) {
                    System.err.println("Expected " + expectedMap[i][j] + " at (" + j + ", " + i + ")");
                    System.err.println("Actual " + actualMap[i][j] + " at (" + j + ", " + i + ")");
                    return false;
                }
            }
        }

        return true;
    }

}