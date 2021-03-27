package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.search.AbstractSearch;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StatisticsUtil {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_BLUE = "\u001B[34m";

    private static final String PREFIX = "%-50s" + ANSI_RED;

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates,
                                       double branchingForDepth, double meanBranchingFactor,
                                       long totalTimeUntilDepth, double totalTime) {
        Logger.print("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =%n", depth);
        Logger.print(PREFIX + "%s %s: %s%n" + ANSI_RESET, "Chosen Move:", "Player", AbstractSearch.MAX, chosenMove);
        Logger.print(PREFIX + "%d, %d, %d%n" + ANSI_RESET, "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        if (depth >= 2) {
            Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Branching factor at depth " + (depth - 1) + ":",
                    branchingForDepth);
        }
        Logger.print(PREFIX + "%s%n" + ANSI_RESET,
                "Mean branching factor:", meanBranchingFactor);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (ms):", totalTime / totalStates);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (µs):",
                (totalTime / totalStates) * 1_000);
        Logger.print(PREFIX + "%d%n" + ANSI_RESET,
                String.format("Computation time up to (inc.) depth %d (ms):", depth), totalTimeUntilDepth);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Total computation time (ms):", totalTime);
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double meanBranchingFactor, double totalTime, long timeLeft,
                                                     double estimatedTime, long totalTimeUntilDepth,
                                                     double branchingForDepth, double averageBranching) {
        printStatistics(depth, chosenMove, totalStates, leafStates, branchingForDepth, meanBranchingFactor,
                totalTimeUntilDepth, totalTime);
        System.out.println();
        Logger.print("- - - - - - - - - - - - - -" + ANSI_BLUE
                + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -%n", depth + 1);
        Logger.print(PREFIX + "%d%n" + ANSI_RESET, "Time left (ms):", timeLeft);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Estimated (ms):", estimatedTime);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Branching average:", averageBranching);
    }
}