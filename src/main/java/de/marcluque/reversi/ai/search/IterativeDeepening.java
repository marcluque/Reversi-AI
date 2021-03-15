package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.network.Client;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.StatisticsUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class IterativeDeepening {

    private static int currentDepth;

    private static final int[] totalStates;

    private static final ArrayList<Integer> statesPerDepth = new ArrayList<>();

    private static long totalTime;

    private static Move chosenMove;

    static  {
        currentDepth = 1;
        totalStates = new int[1];
        // At depth 0 we have only the root state
        statesPerDepth.add(0, 1);
        totalTime = 0;
        chosenMove = null;
    }

    @FunctionalInterface
    public interface SearchStrategy {
        Move apply(int[] states);
    }

    @FunctionalInterface
    public interface PostSearchAction {
        void apply(int leafStates, double averageBranchingForDepth, double meanBranchingFactor, long totalTimeUntilDepth);
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit, SearchStrategy searchStrategy) {
        while (currentDepth <= depthLimit) {
            performSearch(searchStrategy,
                    (leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth) -> {
                // Print statistics
                StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates[0],
                        leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth, totalTime);
            });

            currentDepth++;
        }

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy searchStrategy) {
        int depthLimit = Integer.MAX_VALUE - 1;
        List<Double> branchings = new ArrayList<>();

        while (currentDepth <= depthLimit) {
            performSearch(searchStrategy,
                    (leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth) -> {
                branchings.add(currentDepth - 1, averageBranchingForDepth);

                // Calculate average branching factor for whole tree
                double averageBranching = 0;
                for (double d : branchings) {
                    averageBranching += d;
                }
                averageBranching /= currentDepth;

                // Make time estimation for next iteration
                double estimatedTime = averageBranching * leafStates * (totalTime / (double) totalStates[0]);
                // TODO: IS THIS CORRECT?
                estimatedTime += totalTime;

                // Print statistics
                StatisticsUtil.printStatisticsWithEstimation(currentDepth, chosenMove, totalStates[0],
                        leafStates, meanBranchingFactor, totalTime, GameInstance.getLeftTime(), estimatedTime,
                        totalTimeUntilDepth, branchings.get(currentDepth - 1), averageBranching);

                // Check whether enough time is left for next iteration
                if (GameInstance.getLeftTime() <= estimatedTime) {
                    System.out.println("No time left!");
                }
            });

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
        double meanBranchingFactor = (totalStates[0] - 1) / (double) (totalStates[0] - leafStates);

        // Average branching factor at currentDepth - 1
        double averageBranchingForDepth = 0;
        if (currentDepth >= 2) {
            averageBranchingForDepth = leafStates
                    - (statesPerDepth.get(currentDepth - 1) - statesPerDepth.get(currentDepth - 2));
        }

        postSearchAction.apply(leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeUntilDepth);
    }
}