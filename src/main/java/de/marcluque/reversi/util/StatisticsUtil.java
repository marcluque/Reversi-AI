package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StatisticsUtil {

    private StatisticsUtil() {}

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_BLUE = "\u001B[34m";

    public static final String ANSI_BLACK = "\u001B[30m";

    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_YELLOW = "\u001B[33m";

    public static final String ANSI_PURPLE = "\u001B[35m";

    public static final String ANSI_CYAN = "\u001B[36m";

    public static final String ANSI_WHITE = "\u001B[37m";

    private static final String STATS_PREFIX = "%-50s" + ANSI_RED;

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#########.###");

    static {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    public static void printAllStats(int depth, MoveTriplet chosenMoveTriplet, int totalStates, int leafStates,
                                     double avgBranchingLastDepth, double avgBranching,
                                     double timeForFullDepth, double prevEstimatedTime,
                                     double totalTime, long timeLeft, double estimatedTime,
                                     boolean searchSpaceCompleted) {
        System.out.println();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "================================="
                        + " MOVE %d ================================" + ANSI_RESET,
                GameInstance.getMoveCount() + 1);
        printStatisticsWithEstimation(depth, chosenMoveTriplet, totalStates, leafStates, avgBranchingLastDepth,
                avgBranching, timeForFullDepth, prevEstimatedTime, totalTime, timeLeft, estimatedTime, searchSpaceCompleted);
        printRules();
        printMetrics();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK
                + "==========================================================================" + ANSI_RESET);
        System.out.println();
    }

    public static void printAllStatsWithoutEstimation(int depth, MoveTriplet chosenMoveTriplet, int totalStates, int leafStates,
                                                      double avgBranchingLastDepth, double avgBranching,
                                                      double timeForFullDepth, double totalTime,
                                                      boolean searchSpaceCompleted) {
        System.out.println();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "================================="
                + " MOVE %d ================================" + ANSI_RESET,
                GameInstance.getMoveCount() + 1);
        printStatistics(depth, chosenMoveTriplet, totalStates, leafStates, avgBranchingLastDepth,
                avgBranching, timeForFullDepth, totalTime, searchSpaceCompleted);
        printRules();
        printMetrics();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK
                + "==========================================================================" + ANSI_RESET);
        System.out.println();
    }

    public static void printStatistics(int depth, MoveTriplet chosenMoveTriplet, int totalStates, int leafStates,
                                       double avgBranchingLastDepth, double avgBranching,
                                       double timeForFullDepth, double totalTime, boolean searchSpaceCompleted) {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =", depth);
        if (searchSpaceCompleted) {
            Logger.appendPrintMessage(ANSI_YELLOW + "Entire search space was explored!" + ANSI_RESET);
        }
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Move number:",
                GameInstance.getMoveCount() + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%s %s: %s" + ANSI_RESET,
                "Chosen move:", "Player",
                AbstractSearch.getMax(), chosenMoveTriplet);
        Logger.appendPrintMessage(STATS_PREFIX + "%d, %d, %d" + ANSI_RESET,
                "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                String.format("Average branching factor (depth %d->%d):", depth - 1, depth),
                DECIMAL_FORMAT.format(avgBranchingLastDepth));
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Average branching factor (tree):",
                DECIMAL_FORMAT.format(avgBranching));
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Computation time per state (ms):",
                DECIMAL_FORMAT.format((timeForFullDepth / 1_000_000) / totalStates));
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Computation time per state (µs):",
                DECIMAL_FORMAT.format(((timeForFullDepth / 1_000) / totalStates)));
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Total computation time (ms):",
                DECIMAL_FORMAT.format(totalTime / 1_000_000));
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printRules() {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = = =" + ANSI_BLUE
                + " RULES MOVE %d " + ANSI_RESET + "= = = = = = = = = = = = = = =", GameInstance.getMoveCount() + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Use stone maximizing heuristics:",
                Rules.isUseStoneMaximization());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Override stones allowed:",
                Rules.isUseOverrideStones());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Pick override stones over bombs:",
                Rules.isPickOverrideStoneOverBomb());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Try to search entire tree:",
                Rules.isUseFullGameTreeSearch());
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printInitialMetrics() {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = = =" + ANSI_BLUE
                + " INITIAL METRICS " + ANSI_RESET + "= = = = = = = = = = = = = =");
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Number of playable tiles:",
                Metrics.getNumberPlayableTiles());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal bomb effect (tile area):",
                Metrics.getMaximalBombEffect());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal bomb power (area * #bombs):",
                Metrics.getMaximalBombPower());
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal override effect (maxLength * #override):",
                Metrics.getMaximalOverrideEffect());
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printMetrics() {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = = =" + ANSI_BLUE
                + " METRICS MOVE %d " + ANSI_RESET + "= = = = = = = = = = = = = =", GameInstance.getMoveCount() + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Players with at least one move:",
                Metrics.getOpponentsWithMoves());
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printStatisticsWithEstimation(int depth, MoveTriplet chosenMoveTriplet, int totalStates, int leafStates,
                                                     double avgBranchingLastDepth, double avgBranching,
                                                     double timeForFullDepth, double prevEstimatedTime,
                                                     double totalTime, long timeLeft, double estimatedTime,
                                                     boolean searchSpaceCompleted) {
        printStatistics(depth, chosenMoveTriplet, totalStates, leafStates, avgBranchingLastDepth, avgBranching,
                timeForFullDepth, totalTime, searchSpaceCompleted);
        if (!searchSpaceCompleted) {
            System.out.println();
            Logger.appendPrintMessage("- - - - - - - - - - - - - -" + ANSI_PURPLE
                    + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -", depth + 1);
            Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                    "Estimated time for previous depth (ms):",
                    DECIMAL_FORMAT.format(prevEstimatedTime));
            Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                    "Actual time for previous depth (ms):",
                    DECIMAL_FORMAT.format(timeForFullDepth / 1_000_000));
            Logger.appendPrintMessage(STATS_PREFIX + "%d" + ANSI_RESET,
                    "Time left (ms):",
                    timeLeft);
            Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                    String.format("Estimated for depth %d (ms):", depth + 1),
                    DECIMAL_FORMAT.format(estimatedTime));
            Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                    "Average branching factor:",
                    DECIMAL_FORMAT.format(avgBranching));
            Logger.appendPrintMessage("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        }
    }
}