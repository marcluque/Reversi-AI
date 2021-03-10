package de.datasecs.reversi.util;

import de.datasecs.reversi.ai.search.AbstractSearch;
import de.datasecs.reversi.map.Map;

public class StatisticsUtil {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_BLUE = "\u001B[34m";

    private static final String PREFIX = "%-40s" + ANSI_RED;

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates, double branching,
                                       long totalTimeForDepth, double totalTime) {
        System.out.printf("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =%n", depth);
        System.out.printf(PREFIX + "%s %s: %s%n" + ANSI_RESET, "Chosen Move:", "Player", AbstractSearch.MAX, chosenMove);
        System.out.printf(PREFIX + "%d, %d, %d%n" + ANSI_RESET, "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Branching factor:", branching);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (ms):", totalTime / totalStates);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Computation time per state (µs):",
                (totalTime / totalStates) * 1_000);
        System.out.printf(PREFIX + "%d%n" + ANSI_RESET, String.format("Computation time depth %d (ms):", depth),
                totalTimeForDepth);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Total computation time (ms):", totalTime);
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double branching, double totalTime, long timeLeft,
                                                     double estimatedTime, long totalTimeForDepth,
                                                     double averageBranching) {
        printStatistics(depth, chosenMove, totalStates, leafStates, branching, totalTimeForDepth, totalTime);
        System.out.println();
        System.out.printf("- - - - - - - - - - - - - -" + ANSI_BLUE
                + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -%n", depth + 1);
        System.out.printf(PREFIX + "%d%n" + ANSI_RESET, "Time left (ms):", timeLeft);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Estimated (ms):", estimatedTime);
        System.out.printf(PREFIX + "%s%n" + ANSI_RESET, "Branching average:", averageBranching);
    }
}