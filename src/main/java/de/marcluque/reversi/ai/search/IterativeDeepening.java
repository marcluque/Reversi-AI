package de.datasecs.reversi.ai.search;

import de.datasecs.reversi.network.Client;
import de.datasecs.reversi.util.Move;
import de.datasecs.reversi.util.StatisticsUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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

            // Calculate branching factor
            double branching = (totalStates[0] - 1) / (double) (totalStates[0] - leafStates);

            // Print statistics
            StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates[0],
                    leafStates, branching, totalTimeForDepth, totalTime);

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

        long totalTimeForDepth;
        long totalTime = 0;
        Move chosenMove = null;
        List<Double> branchings = new ArrayList<>();

        while (currentDepth <= depthLimit) {
            // Do search
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStates);
            totalTimeForDepth = System.nanoTime() - start;
            totalTime += totalTimeForDepth;

            // Count states
            statesPerDepth.add(currentDepth, totalStates[0]);
            int leafStates = statesPerDepth.get(currentDepth) - statesPerDepth.get(currentDepth - 1);

            // Calculate branching factor
            branchings.add(currentDepth, (totalStates[0] - 1) / (double) (totalStates[0] - leafStates));
            // Calculate aver branching factor
            double averageBranching = branchings.stream().mapToDouble(d -> d).sum() / (double) currentDepth;

            // Make time estimation for next iteration
            double estimatedTime = averageBranching
                    * leafStates
                    * (totalTime / (double) totalStates[0]);

            // Print statistics
            StatisticsUtil.printStatisticsWithEstimation(currentDepth, chosenMove, totalStates[0],
                    leafStates, branchings.get(currentDepth), totalTime, Client.getLeftTime(), estimatedTime,
                    totalTimeForDepth, averageBranching);

            // Check whether enough time is left for next iteration
            if (Client.getLeftTime() <= estimatedTime) {
                System.out.println("No time left!");
            }

            currentDepth++;
        }

        return chosenMove;
    }
}