package de.datasecs.reversi.util;

import de.datasecs.reversi.ai.search.AbstractSearch;

public class StatisticsUtil {

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates, double branching,
                                       double totalTime) {
        System.out.printf("= = = = = = = = = = STATISTICS DEPTH %d = = = = = = = = = =%n", depth);
        System.out.printf("Chosen move: %-20s  Player %s: %s", AbstractSearch.MAX, chosenMove);
        System.out.printf("Total-, Interior-, Leaf-nodes: %-20s %d, %d, %d" + totalStates,
                (totalStates - leafStates), leafStates);
        System.out.printf("Branching factor: %-20s %s%n", branching);
        System.out.println("Computation time per state (ms): %-20s " + totalTime / totalStates);
        System.out.println("Total computation time (ms): %-20s " + totalTime);
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double branching, double totalTime, int timeLeft,
                                                     double estimatedTime, double averageBranching) {
        printStatistics(depth, chosenMove, totalStates, leafStates, branching, totalTime);
        System.out.printf("- - - - - - - - - - ESTIMATION DEPTH %d - - - - - - - - - -", depth + 1);
        System.out.println("Time left (ms): %-10s " + timeLeft);
        System.out.println("Estimated (ms): %-10s " + estimatedTime);
        System.out.println("Branching average: %-10s " + averageBranching);
    }
}