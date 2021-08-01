package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StatisticsUtil {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_RED = "\u001B[31m";

    private static final String ANSI_BLUE = "\u001B[34m";

    private static final String PREFIX = "%-50s" + ANSI_RED;

    public static void printStatistics(int depth, Move chosenMove, int totalStates, int leafStates,
                                       double avgBranchingFactor, double timeForFullDepth, double totalTime) {
        System.out.println();
        Logger.print("= = = = = = = = = = = = = =" + ANSI_BLUE
                + " STATISTICS DEPTH %d " + ANSI_RESET + "= = = = = = = = = = = = = =", depth);
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Move number:", GameInstance.getMoveCount() + 1);
        Logger.print(PREFIX + "%s %s: %s" + ANSI_RESET, "Chosen Move:", "Player", AbstractSearch.MAX, chosenMove);
        Logger.print(PREFIX + "%d, %d, %d" + ANSI_RESET, "Total-, Interior-, Leaf-nodes:",
                totalStates, (totalStates - leafStates), leafStates);
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Branching factor:",
                avgBranchingFactor);
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Computation time per state (ms):",
                (timeForFullDepth / 1_000_000) / totalStates);
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Computation time per state (µs):",
                ((timeForFullDepth / 1_000) / totalStates));
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Total computation time (ms):", (totalTime / 1_000_000));
        Logger.print(PREFIX + "%s" + ANSI_RESET, "Override stones allowed:", Rules.useOverrideStones);
        Logger.print("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =\n");
    }

    public static void printStatisticsWithEstimation(int depth, Move chosenMove, int totalStates, int leafStates,
                                                     double meanBranchingFactor, long totalTime, long timeLeft,
                                                     double estimatedTime,
                                                     double branchingForDepth, double averageBranching) {
        printStatistics(depth, chosenMove, totalStates, leafStates, averageBranching, totalTime, totalTime);
        System.out.println();
        Logger.print("- - - - - - - - - - - - - -" + ANSI_BLUE
                + " ESTIMATION DEPTH %d " + ANSI_RESET + "- - - - - - - - - - - - - -%n", depth + 1);
        Logger.print(PREFIX + "%d%n" + ANSI_RESET, "Time left (ms):", timeLeft);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Estimated (ms):", estimatedTime);
        Logger.print(PREFIX + "%s%n" + ANSI_RESET, "Branching average:", averageBranching);
    }
}