//package de.datasecs.reversi.ai.search;
//
//import de.datasecs.reversi.ai.evaluation.Evaluation;
//import de.datasecs.reversi.map.Map;
//import de.datasecs.reversi.util.Coordinate;
//import de.datasecs.reversi.util.MapUtil;
//
//public class AlphaBeta {
//
//    private static Coordinate returnMove;
//
//    private static int visitedStates;
//
//    private AlphaBeta() {}
//
//    public static void alphaBetaSearch(Map map, char player, boolean override, int phase) {
//        final int[] value = {Integer.MIN_VALUE};
//
//        MapUtil.search(map, player, override, phase, (mapClone, coordinate) -> {
//            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
//            int newValue = minValue(new int[]{Integer.MIN_VALUE}, new int[]{Integer.MAX_VALUE}, mapClone, playerChar, override, phase);
//
//            if (newValue > value[0]) {
//                returnMove = new Coordinate(coordinate.getX(), coordinate.getY());
//                value[0] = newValue;
//            }
//
//            return false;
//        });
//    }
//
//    private static int maxValue(int alpha, int beta, Map map, char player, boolean override, int phase) {
//        // Terminal test
//        int result = Evaluation.utility(map, player);
//        if (result == -1) {
//            return result;
//        }
//
//        visitedStates++;
//        final int[] value = {Integer.MIN_VALUE};
//
//        MapUtil.search(map, player, override, phase, mapClone -> {
//            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
//            value[0] = Math.max(value[0], minValue(alpha, beta, mapClone, playerChar, override, phase));
//            if (value[0] >= beta[0]) {
//                return true;
//            }
//
//            alpha[0] = Math.max(alpha[0], value[0]);
//            return false;
//        });
//
//        return value[0];
//    }
//
//    private static int minValue(int alpha, int beta, Map map, char player, boolean override, int phase) {
//        // Terminal test
//        int result = Evaluation.utility(map, player);
//        if (result == -1) {
//            return result;
//        }
//
//        visitedStates++;
//        final int[] value = {Integer.MAX_VALUE};
//
//        MapUtil.search(map, player, override, phase, mapClone -> {
//            char playerChar = (char) ((Character.getNumericValue(player) % Map.getNumberOfPlayers()) + 1 + 96);
//            value[0] = Math.min(value[0], maxValue(alpha, beta, mapClone, playerChar, override, phase));
//            if (value[0] <= alpha[0]) {
//                return true;
//            }
//
//            beta[0] = Math.min(beta[0], value[0]);
//            return false;
//        });
//
//        return value[0];
//    }
//}