package de.datasecs.reversi.ai.search;

import de.datasecs.reversi.ai.evaluation.Evaluation;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.MapUtil;

public class MiniMax {

    private static Coordinate returnMove;

    private static int visitedStates;

    private MiniMax() {}

    public static void miniMax(Map map, char player, boolean override, int phase) {
        final int[] value = {Integer.MIN_VALUE};

        MapUtil.search(map, player, override, phase, (mapClone, coordinate) -> {
            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
            int newValue = minValue(mapClone, playerChar, override, phase);

            if (newValue > value[0]) {
                returnMove = new Coordinate(coordinate.getX(), coordinate.getY());
                value[0] = newValue;
            }

            return false;
        });
    }

    private static int maxValue(Map map, char player, boolean override, int phase) {
        // Terminal test
        int result = Evaluation.utility(map, player);
        if (result == -1) {
            return result;
        }

        visitedStates++;
        final int[] value = {Integer.MIN_VALUE};

        MapUtil.search(map, player, override, phase, mapClone -> {
            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
            value[0] = Math.max(value[0], minValue(mapClone, playerChar, override, phase));

            return false;
        });

        return value[0];
    }

    private static int minValue(Map map, char player, boolean override, int phase) {
        // Terminal test
        int result = Evaluation.utility(map, player);
        if (result == -1) {
            return result;
        }

        visitedStates++;
        final int[] value = {Integer.MAX_VALUE};

        MapUtil.search(map, player, override, phase, mapClone -> {
            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
            value[0] = Math.min(value[0], maxValue(mapClone, playerChar, override, phase));

            return false;
        });

        return value[0];
    }
}