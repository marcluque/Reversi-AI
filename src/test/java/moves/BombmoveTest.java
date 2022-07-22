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
import java.util.List;
import java.util.stream.Stream;

public class BombmoveTest {

    private static final String BASE = "src/test/resources/maps/bombmoves";

    private static Stream<Arguments> validBombMapMovePlayer() {
        // Arguments follow the structure: (test-id, x-coordinate, y-coordinate, player-number)
        return Stream.of(
                Arguments.of(0, 0, 0, '1'),
                Arguments.of(1, 5, 5, '1'),
                Arguments.of(2, 0, 0, '1'),
                Arguments.of(3, 5, 5, '1')
        );
    }

    @ParameterizedTest
    @MethodSource("validBombMapMovePlayer")
    public void testBombMove(int testNumber, int x, int y, char player) {
        String beforeMapPath = String.format("%s/bombmove_test%d_before.txt", BASE, testNumber);

        GameInstance.setMap(MapLoader.generateMapFromMapFile(beforeMapPath));
        Map.setPhase(2);

        List<Coordinate> capturableTiles = new ArrayList<>();
        Assertions.assertTrue(Move.isMoveValid(GameInstance.getMap(), x, y, player, false,
                true, capturableTiles));

        Move.executeMove(GameInstance.getMap(), x, y, 0, player, capturableTiles);

        String afterMapPath = String.format("%s/bombmove_test%d_after.txt", BASE, testNumber);
        Assertions.assertTrue(TestUtils.mapEquals(GameInstance.getMap().getGameField(),
                MapLoader.generateArrayFromMapFile(afterMapPath)));
    }
}
