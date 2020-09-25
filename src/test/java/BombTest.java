import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.map.MapLoader;
import de.datasecs.reversi.moves.BombMove;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BombTest {

    @Test
    void testBombEffectSimpleCaptureMap() {

        Map map = MapLoader.generateStringFromMapFile("simpleCaptureMap.txt");
        char[][] expected = {
                {'0', '-', '-', '-', '1', '1'},
                {'1', '-', '-', '-', '0', '0'},
                {'2', '-', '-', '-', '0', '0'},
                {'0', '0', '0', '2', '0', '0'}
        };
        BombMove.executeBombMove(map, 2, 1);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("simpleCaptureMap.txt");
        Map.setBombRadius(2);
        char[][] expected2 = {
                {'-', '-', '-', '-', '-', '1'},
                {'-', '-', '-', '-', '-', '0'},
                {'-', '-', '-', '-', '-', '0'},
                {'-', '-', '-', '-', '-', '0'}
        };
        BombMove.executeBombMove(map, 2, 1);

        compareMaps(map.getGameField(), expected2);
    }

    @Test
    void testBombEffectBombRadiusMap() {
        Map map = MapLoader.generateStringFromMapFile("bombs/bombRadiusMap.txt");
        Map.setBombRadius(1);
        char[][] expected = {
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '-', '-','-', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '-', '-','-', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '-', '-','-', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
        };
        BombMove.executeBombMove(map, 5, 5);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombRadiusMap.txt");
        Map.setBombRadius(2);
        char[][] expected2 = {
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '0', '-', '-', '-','-', '-', '0', '-', '-'},
                {'-', '-', '0', '-', '-', '-','-', '-', '0', '-', '-'},
                {'-', '-', '0', '-', '-', '-','-', '-', '0', '-', '-'},
                {'-', '-', '0', '-', '-', '-','-', '-', '0', '-', '-'},
                {'-', '-', '0', '-', '-', '-','-', '-', '0', '-', '-'},
                {'-', '-', '0', '0', '0', '0','0', '0', '0', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
        };
        BombMove.executeBombMove(map, 5, 5);
        compareMaps(map.getGameField(), expected2);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombRadiusMap.txt");
        Map.setBombRadius(3);
        char[][] expected3 = {
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
                {'-', '-', '-', '-', '-', '-','-', '-', '-', '-', '-'},
        };
        BombMove.executeBombMove(map, 5, 5);
        compareMaps(map.getGameField(), expected3);
    }

    @Test
    void testBombEffectBombWithHolesMap() {
        Map map = MapLoader.generateStringFromMapFile("bombs/bombEffectWithHoles.txt");
        Map.setBombRadius(2);
        char[][] expected = {
                {'1', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '-'},
        };
        BombMove.executeBombMove(map, 2, 2);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombEffectWithHoles.txt");
        Map.setBombRadius(2);
        char[][] expected2 = {
                {'-', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '1'},
        };
        BombMove.executeBombMove(map, 0, 0);
        compareMaps(map.getGameField(), expected2);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombEffectWithHoles.txt");
        Map.setBombRadius(1);
        char[][] expected3 = {
                {'-', '-', '1'},
                {'-', '-', '2'},
                {'-', '-', '1'},
        };
        BombMove.executeBombMove(map, 0, 1);
        compareMaps(map.getGameField(), expected3);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombEffectWithHoles.txt");
        Map.setBombRadius(2);
        char[][] expected4 = {
                {'-', '-', '-'},
                {'-', '-', '-'},
                {'-', '-', '-'},
        };
        BombMove.executeBombMove(map, 0, 1);
        compareMaps(map.getGameField(), expected4);
    }

    @Test // transition map.getGameField()
    void testBombEffectMap4() {
        Map map = MapLoader.generateStringFromMapFile("bombs/bombTestMap.txt");
        Map.setBombRadius(1);
        char[][] expected = {
                {'0', '-', '-', '-'},
                {'0', '-', '-', '-'},
                {'0', '0', '0', '0'},
                {'0', '0', '-', '0'},
        };
        BombMove.executeBombMove(map, 2, 0);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombTestMap.txt");
        Map.setBombRadius(2);
        char[][] expected2 = {
                {'-', '-', '-', '-'},
                {'-', '-', '-', '-'},
                {'-', '-', '-', '-'},
                {'0', '-', '-', '-'},
        };
        BombMove.executeBombMove(map, 2, 0);
        compareMaps(map.getGameField(), expected2);
    }

    @Test
    void testBombEffectNestedTransitions() {
        Map map = MapLoader.generateStringFromMapFile("bombs/bombEffectNestedTransitions.txt");
        Map.setBombRadius(2);
        char[][] expected = {
                {'-', '-', '-', '0', '0', '0'},
                {'-', '-', '-', '0', '0', '0'},
                {'-', '-', '-', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'-', '-', '0', '0', '0', '0'},
                {'-', '-', '0', '0', '0', '-'},
        };
        BombMove.executeBombMove(map, 0, 0);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombEffectNestedTransitions.txt");
        Map.setBombRadius(2);
        char[][] expected2 = {
                {'-', '0', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'0', '0', '0', '-', '-', '-'},
                {'-', '-', '0', '-', '-', '-'},
                {'-', '-', '0', '-', '-', '-'},
        };
        BombMove.executeBombMove(map, 5, 5);
        compareMaps(map.getGameField(), expected2);
    }

    @Test
    void testBombEffectNestedTransitionsWithHoles() {
        Map map = MapLoader.generateStringFromMapFile("bombs/bombEffectNestedTransitionsWithHoles.txt");
        Map.setBombRadius(2);
        char[][] expected = {
                {'-', '-', '0', '0', '0', '0'},
                {'-', '-', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'-', '-', '0', '0', '-', '-'},
                {'-', '-', '0', '0', '-', '-'},
        };
        BombMove.executeBombMove(map, 0, 0);
        compareMaps(map.getGameField(), expected);

        // reset map.getGameField()

        map = MapLoader.generateStringFromMapFile("bombs/bombEffectNestedTransitionsWithHoles.txt");
        Map.setBombRadius(3);
        char[][] expected2 = {
                {'-', '-', '0', '0', '0', '0'},
                {'-', '-', '0', '0', '0', '0'},
                {'0', '0', '0', '0', '0', '0'},
                {'-', '-', '-', '0', '0', '0'},
                {'-', '-', '-', '0', '-', '-'},
                {'-', '-', '-', '0', '-', '-'},
        };
        BombMove.executeBombMove(map, 5, 5);
        compareMaps(map.getGameField(), expected2);
    }

    private void compareMaps(char[][] actualMap, char[][] expectedMap) {
        for (int i = 0; i < actualMap.length; i++) {
            for (int j = 0; j < actualMap[0].length; j++) {
                System.out.print(actualMap[i][j] + " ");
            }

            System.out.print(" |  ");

            for (int j = 0; j < actualMap[0].length; j++) {
                System.out.print(expectedMap[i][j] + " ");
            }

            System.out.print("\n");
        }

        System.out.println();

        for (int i = 0; i < actualMap.length; i++) {
            for (int j = 0; j < actualMap[0].length; j++) {
                Assertions.assertEquals(expectedMap[i][j], actualMap[i][j], String.format("[i][j]=[%d][%d]", i, j));
            }
        }
    }
}