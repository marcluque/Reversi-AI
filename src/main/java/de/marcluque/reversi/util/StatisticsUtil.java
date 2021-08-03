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

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    static {
        DECIMAL_FORMAT.setRoundingMode(RoundingMode.CEILING);
    }

    public static void printAllStats(int depth, Move chosenMove, int totalStates, int leafStates,
                                     double avgBranchingLastDepth, double avgBranching,
                                     double timeForFullDepth, double totalTime,
                                     long timeLeft, double estimatedTime) {
        System.out.println();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "================================="
                        + " MOVE %d ================================" + ANSI_RESET,
                GameInstance.getMoveCount() + 1);
        printStatisticsWithEstimation(depth, chosenMove, totalStates, leafStates, avgBranchingLastDepth,
                avgBranching, timeForFullDepth, totalTime, timeLeft, estimatedTime);
        printRules();
        printMetrics();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK
                + "==========================================================================" + ANSI_RESET);
        System.out.println();
    }

    public static void printAllStatsWithoutEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                      double avgBranchingLastDepth, double avgBranching,
                                                      double timeForFullDepth, double totalTime) {
        System.out.println();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "================================="
                + " MOVE %d ================================" + ANSI_RESET,
                GameInstance.getMoveCount() + 1);
        printStatistics(depth, chosenMove, totalStates, leafStates, avgBranchingLastDepth,
                avgBranching, timeForFullDepth, totalTime);
        printRules();
        printMetrics();
        Logger.appendPrintMessage(ANSI_CYAN_BACKGROUND + ANSI_BLACK
                + "==========================================================================" + ANSI_RESET);
        System.out.println();
    }

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates,
                                       double avgBranchingLastDepth, double avgBranching,
                                       double timeForFullDepth, double totalTime) {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =", depth);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Move number:",
                GameInstance.getMoveCount() + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%s %s: %s" + ANSI_RESET,
                "Chosen move:", "Player",
                AbstractSearch.MAX, chosenMove);
        Logger.appendPrintMessage(STATS_PREFIX + "%d, %d, %d" + ANSI_RESET,
                "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                String.format("Average branching factor (depth %d->%d):", depth - 1, depth),
                avgBranchingLastDepth);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Average branching factor (tree):",
                avgBranching);
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
                Rules.useStoneMaximization);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Override stones allowed:",
                Rules.useOverrideStones);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Pick override stones over bombs:",
                Rules.pickOverrideStoneOverBomb);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Try to search entire tree:",
                Rules.useFullGameTreeSearch);
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printInitialMetrics() {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = = =" + ANSI_BLUE
                + " INITIAL METRICS " + ANSI_RESET + "= = = = = = = = = = = = = =");
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Number of playable tiles:",
                Metrics.numberPlayableTiles);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal bomb effect (tile area):",
                Metrics.maximalBombEffect);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal bomb power (area * #bombs):",
                Metrics.maximalBombPower);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Maximal override effect (maxLength * #override):",
                Metrics.maximalOverrideEffect);
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printMetrics() {
        System.out.println();
        Logger.appendPrintMessage("= = = = = = = = = = = = = = =" + ANSI_BLUE
                + " METRICS MOVE %d " + ANSI_RESET + "= = = = = = = = = = = = = =", GameInstance.getMoveCount() + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%s" + ANSI_RESET,
                "Players with at least one move:",
                Metrics.opponentsWithMoves);
        Logger.appendPrintMessage("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double avgBranchingLastDepth, double avgBranching,
                                                     double timeForFullDepth, double totalTime,
                                                     long timeLeft, double estimatedTime) {
        printStatistics(depth, chosenMove, totalStates, leafStates, avgBranchingLastDepth, avgBranching,
                timeForFullDepth, totalTime);
        System.out.println();
        Logger.appendPrintMessage("- - - - - - - - - - - - - -" + ANSI_BLUE
                + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -%n", depth + 1);
        Logger.appendPrintMessage(STATS_PREFIX + "%d%n" + ANSI_RESET,
                "Time left (ms):",
                timeLeft);
        Logger.appendPrintMessage(STATS_PREFIX + "%s%n" + ANSI_RESET,
                "Estimated (ms):",
                estimatedTime);
        Logger.appendPrintMessage(STATS_PREFIX + "%s%n" + ANSI_RESET,
                "Average Branching Factor:",
                avgBranching);
    }
}