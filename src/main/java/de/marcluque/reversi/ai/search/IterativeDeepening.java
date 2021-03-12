package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.network.Client;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.StatisticsUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
@SuppressWarnings("DuplicatedCode")
public class IterativeDeepening {

    @FunctionalInterface
    public interface SearchStrategy<MoveReply, States> {
        MoveReply apply(States states);
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit,
                                                    SearchStrategy<Move, int[]> searchStrategy) {
        int currentDepth = 1;
        int[] totalStates = {0};

        ArrayList<Integer> statesPerDepth = new ArrayList<>();
        // At depth 0 we have only the root state
        statesPerDepth.add(0, 1);

        long totalTimeForDepth;
        long totalTime = 0;
        Move chosenMove = null;

        while (currentDepth <= depthLimit) {
            // Do search
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStates);
            totalTimeForDepth = System.nanoTime() - start;
            totalTime += totalTimeForDepth;

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

            // Print statistics
            StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates[0],
                    leafStates, averageBranchingForDepth, meanBranchingFactor, totalTimeForDepth, totalTime);

            currentDepth++;
        }

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy<Move, int[]> searchStrategy) {
        int depthLimit = Integer.MAX_VALUE - 1;
        int currentDepth = 1;
        int[] totalStates = {0};

        ArrayList<Integer> statesPerDepth = new ArrayList<>();
        // At depth 0 we have only the root state
        statesPerDepth.add(0, 1);

        long totalTimeUntilDepth;
        long totalTime = 0;
        Move chosenMove = null;
        List<Double> branchings = new ArrayList<>();

        while (currentDepth <= depthLimit) {
            // Do search
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStates);
            totalTimeUntilDepth = System.nanoTime() - start;
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
            branchings.add(currentDepth - 1, averageBranchingForDepth);

            // Calculate average branching factor for whole tree
            double averageBranching = branchings.stream().mapToDouble(d -> d).sum() / (double) currentDepth;

            // Make time estimation for next iteration
            double estimatedTime = averageBranching * leafStates * (totalTime / (double) totalStates[0]);
            // TODO: IS THIS CORRECT?
            estimatedTime += totalTime;

            // Print statistics
            StatisticsUtil.printStatisticsWithEstimation(currentDepth, chosenMove, totalStates[0],
                    leafStates, meanBranchingFactor, totalTime, Client.getLeftTime(), estimatedTime,
                    totalTimeUntilDepth, branchings.get(currentDepth - 1), averageBranching);

            // Check whether enough time is left for next iteration
            if (Client.getLeftTime() <= estimatedTime) {
                System.out.println("No time left!");
            }

            currentDepth++;
        }

        return chosenMove;
    }
}