package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.StatisticsUtil;

import java.util.ArrayList;

/*
 * Created with <3 by marcluque, March 2021
 */
public class IterativeDeepening {

    @FunctionalInterface
    public interface SearchStrategy {
        Move apply(int[] states);
    }

    @FunctionalInterface
    public interface PostSearchAction {
        void apply(int leafStates, double averageBranchingForDepth, double meanBranchingFactor, long totalTimeUntilDepth);
    }

    private static int currentDepth;

    private static final int[] totalStates;

    private static final ArrayList<Integer> statesPerDepth = new ArrayList<>();

    private static long totalTime;

    private static Move chosenMove;

    private static final PostSearchAction postSearchActionDepthLimit;

    private static final PostSearchAction postSearchActionTimeLimit;

    private static boolean continueSearch;

    private static double summedBranching;

    static  {
        currentDepth = 1;
        totalStates = new int[1];
        // At depth 0 we only have the root state
        statesPerDepth.add(0, 1);
        totalTime = 0;
        chosenMove = null;
        continueSearch = true;
        summedBranching = 0;

        postSearchActionDepthLimit = (leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth) -> {
            // Print statistics
            StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates[0],
                    leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth, totalTime);
        };

        postSearchActionTimeLimit = (leafStates, branchingForDepth, meanBranchingFactor, totalTimeUntilDepth) -> {
            summedBranching += branchingForDepth;

            // Make time estimation for next iteration
            double branchingAverage = summedBranching / currentDepth;
            double estimatedTime = branchingAverage * leafStates * (totalTime / (double) totalStates[0]);
            estimatedTime += totalTime;

            // Check whether enough time is left for next iteration
            if (GameInstance.getLeftTime() <= estimatedTime) {
                System.out.println("No time left for next depth!");
                continueSearch = false;
            }

            // Print statistics
            StatisticsUtil.printStatisticsWithEstimation(currentDepth, chosenMove, totalStates[0],
                    leafStates, meanBranchingFactor, totalTime, GameInstance.getLeftTime(), estimatedTime,
                    totalTimeUntilDepth, branchingForDepth, branchingAverage);
        };
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit, SearchStrategy searchStrategy) {
        while (currentDepth <= depthLimit) {
            performSearch(searchStrategy, postSearchActionDepthLimit);
            currentDepth++;
        }

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy searchStrategy) {
        continueSearch = true;
        summedBranching = 0;

        while (continueSearch) {
            performSearch(searchStrategy, postSearchActionTimeLimit);
            currentDepth++;
        }

        return chosenMove;
    }

    private static void performSearch(SearchStrategy searchStrategy, PostSearchAction postSearchAction) {
        // Do search
        long start = System.nanoTime();
        chosenMove = searchStrategy.apply(totalStates);
        long totalTimeUntilDepth = System.nanoTime() - start;
        totalTime += totalTimeUntilDepth;

        // Count states
        statesPerDepth.add(currentDepth, totalStates[0]);
        int leafStates = statesPerDepth.get(currentDepth) - statesPerDepth.get(currentDepth - 1);

        // Calculate mean branching factor
        double nonTerminalStates = totalStates[0] - leafStates;
        double meanBranchingFactor = totalStates[0] / nonTerminalStates;

        // Branching factor (NOT average over the whole tree, just the last two states) at currentDepth - 1
        double branchingForDepth = 0;
        if (currentDepth >= 1) {
            branchingForDepth = (double) leafStates / statesPerDepth.get(currentDepth - 1);
        }

        postSearchAction.apply(leafStates, branchingForDepth, meanBranchingFactor, totalTimeUntilDepth);
    }
}