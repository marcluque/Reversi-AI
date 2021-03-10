package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.search.AbstractSearch;

public class StatisticsUtil {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_BLUE = "\u001B[34m";

    private static final String PREFIX = "%-40s" + ANSI_RED;

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates,
                                       double averageBranchingForDepth, double meanBranchingFactor,
                                       long totalTimeUntilDepth, double totalTime) {
        System.out.printf("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =%n", depth);
        System.out.printf(PREFIX + "%s %s: %s%n" + ANSI_RESET, "Chosen Move:", "Player", AbstractSearch.MAX, chosenMove);
        System.out.printf(PREFIX + "%d, %d, %d%n" + ANSI_RESET, "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        if (depth >= 2) {
            System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Branching factor at depth " + (depth - 1) + ":",
                    averageBranchingForDepth);
        }
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Mean branching factor (total states / leaf states):",
                meanBranchingFactor);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (ms):", totalTime / totalStates);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (µs):",
                (totalTime / totalStates) * 1_000);
        System.out.printf(PREFIX + "%d%n" + ANSI_RESET, String.format("Computation time up to (including) depth %d (ms):", depth),
                totalTimeUntilDepth);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Total computation time (ms):", totalTime);
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double meanBranchingFactor, double totalTime, long timeLeft,
                                                     double estimatedTime, long totalTimeUntilDepth,
                                                     double averageBranchingForDepth, double averageBranching) {
        printStatistics(depth, chosenMove, totalStates, leafStates, averageBranchingForDepth, meanBranchingFactor,
                totalTimeUntilDepth, totalTime);
        System.out.println();
        System.out.printf("- - - - - - - - - - - - - -" + ANSI_BLUE
                + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -%n", depth + 1);
        System.out.printf(PREFIX + "%d%n" + ANSI_RESET, "Time left (ms):", timeLeft);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Estimated (ms):", estimatedTime);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Branching average:", averageBranching);
    }
}