package moves;

import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import shared.TestUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameReplayTest {

    private static final String LOG_BASE = "src/test/resources/replays";

    private static final String MAP_BASE = "src/test/resources/maps";

    private static int totalMoves;

    private static long start;

    @BeforeAll
    static void startTimer() {
        start = System.nanoTime();
    }

    private static Stream<Arguments> matchReplay() {
        List<Arguments> arguments = new ArrayList<>();
        // Match 0
        for (int i = 0; i < 504; i++) {
            // Arguments follow the structure: (server-log-path, map-path)
            arguments.add(Arguments.of(String.format("%s/match0/matchpoint-1597941208976-%d-server-out", LOG_BASE, i),
                    String.format("%s/map%d.map", MAP_BASE, i / 9)));
        }

        // Match 1
        for (int i = 0; i < 448; i++) {
            // Arguments follow the structure: (server-log-path, map-path)
            arguments.add(Arguments.of(String.format("%s/match1/matchpoint-1597838003363-%d-server-out", LOG_BASE, i),
                    String.format("%s/map%d.map", MAP_BASE, i / 8)));
        }

        // Match 2
        for (int i = 0; i < 448; i++) {
            // Arguments follow the structure: (server-log-path, map-path)
            arguments.add(Arguments.of(String.format("%s/match2/matchpoint-1597852326864-%d-server-out", LOG_BASE, i),
                    String.format("%s/map%d.map", MAP_BASE, i / 8)));
        }

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("matchReplay")
    public void testGameReplay(String serverLogPath, String mapPath) {
        GameInstance.setMap(MapLoader.generateMapFromMapFile(mapPath));
        Map.setPhase(1);

        var path = Path.of(serverLogPath).toAbsolutePath();
        String line = "";
        int moveNumber = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
            while ((line = br.readLine()) != null) {
                int numberOfOverrides = 0;
                int numberOfBombs = 0;
                while (line != null && !line.startsWith("Sending move request to player")) {
                    if (line.contains("Bombing-only map")) {
                        Map.setPhase(2);
                        moveNumber = 1;
                    } else if (line.startsWith("Player") && line.contains("overrides") && line.contains("bombs")) {
                        int player = Integer.parseInt(line.split("layer ")[1].split(" has")[0]);
                        numberOfOverrides = Integer.parseInt(line.split(" overrides")[0].split("has ")[1]);
                        int finalMoveNumberTemp = moveNumber;
                        Assertions.assertEquals(numberOfOverrides, GameInstance.getMap().getOverrideStones()[player],
                                () -> "Move: " + finalMoveNumberTemp);
                        numberOfBombs = Integer.parseInt(line.split(" bombs")[0].split("and ")[1]);
                        Assertions.assertEquals(numberOfBombs, GameInstance.getMap().getBombs()[player]);
                    }

                    line = br.readLine();
                }

                if (line == null) {
                    break;
                }

                boolean skipFirstBoard = false;
                char player = line.split("layer ")[1].charAt(0);
                line = br.readLine();
                boolean isMoveValidExpected = false;
                if (line.startsWith("Answer from player")) {
                    isMoveValidExpected = true;
                    int expectedMoveNumber = Integer.parseInt(line.split("move ")[1].split(" was")[0]);
                    Assertions.assertEquals(expectedMoveNumber, moveNumber);
                } else if (line.startsWith("send_msg(): failed to send header")) {
                    continue;
                } else if (line.startsWith("receive_msg(): error during reading hdr")) {
                    continue;
                } else if (line.contains("invalid choice for plain or inversion move")) {
                    line = br.readLine();
                    skipFirstBoard = true;
                } else if (line.startsWith("timeout detected!")) {
                    moveNumber += 1;
                    continue;
                }

                moveNumber += 1;

                String[] s = line.split("\\(")[2].split(",");
                int x = Integer.parseInt(s[0]);
                int y = Integer.parseInt(s[1]);
                int specialField = Integer.parseInt(s[2].split("\\)")[0]);

                // Test move validity
                List<Coordinate> capturableTiles = new ArrayList<>();
                int finalMoveNumber = moveNumber;
                boolean isMoveValidActual = Move.isMoveValid(GameInstance.getMap(), x, y, player,
                        false, true, capturableTiles);

                if (specialField == 20 || specialField == 21) {
                    isMoveValidActual &= MapUtil.isTileBonus(GameInstance.getMap().getGameField()[y][x]);
                }

                if (specialField >= 1 && specialField <= Map.getNumberOfPlayers()) {
                    isMoveValidActual &= GameInstance.getMap().getGameField()[y][x] == 'c';
                }

                Assertions.assertEquals(isMoveValidExpected, isMoveValidActual, () -> "Move: " + finalMoveNumber);

                if (skipFirstBoard) {
                    //noinspection StatementWithEmptyBody
                    while ((line = br.readLine()) != null && !line.startsWith("Board:"));
                    br.readLine();
                }

                // We skip lines until we read Board
                //noinspection StatementWithEmptyBody
                while ((line = br.readLine()) != null && !line.startsWith("Board:"));

                if (line == null) {
                    break;
                }


                // read map
                char[][] expected = new char[Map.getMapHeight()][Map.getMapWidth()];
                for (int i = 0; i < Map.getMapHeight(); i++) {
                    char[] mapLine = br.readLine().toCharArray();
                    System.arraycopy(mapLine, 0, expected[i], 0, mapLine.length);
                }

                if (isMoveValidExpected) {
                    Move.executeMove(GameInstance.getMap(), x, y, specialField, player, capturableTiles);

                    Assertions.assertTrue(TestUtils.mapEquals(expected, GameInstance.getMap().getGameField()), () -> "Move: " + finalMoveNumber);
                }

                br.readLine();
                line = br.readLine();
                if (line.startsWith("Terminal state reached")) {
                    // TODO Check scores

                    //noinspection StatementWithEmptyBody
                    while ((line = br.readLine()) != null && !line.contains("bombs"));

                    if (line == null) {
                        break;
                    }

                    Map.setPhase(2);
                    moveNumber = Math.max(moveNumber, 1);
                } else if (line.startsWith("Final state reached")) {
                    // TODO Check scores

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CURRENT LINE: " + line);
            System.out.println("LOG: " + serverLogPath);
            System.out.println("MAP: " + mapPath);
        }

        totalMoves += moveNumber;
    }

    @AfterAll
    static void printMoves() {
        long end = System.nanoTime();
        System.out.println("Total moves processed: " + totalMoves);
        System.out.println("Total time: " + (end - start) / 1_000_000_000d + " s");
    }
}