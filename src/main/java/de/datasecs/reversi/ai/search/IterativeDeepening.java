package de.datasecs.reversi.ai.search;

import de.datasecs.reversi.util.Move;
import de.datasecs.reversi.util.StatisticsUtil;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class IterativeDeepening {

    public static Move iterativeDeepeningDepthLimit(int depthLimit, Supplier<AbstractMap.SimpleEntry<Move, Double>> searchStrategy) {
        return iterativeDeepeningDepthLimit(depthLimit, e -> searchStrategy.get());
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit, Function<Double, AbstractMap.SimpleEntry<Move, Double>> searchStrategy) {
        int currentDepth = 1;
        int leafStates = 1;
        int totalStates = 2;
        Move chosenMove = null;
        double lastValue = 0;

        while (currentDepth <= depthLimit) {
            // Do search
            long start = System.nanoTime();
            var entry = searchStrategy.apply(lastValue);
            chosenMove = entry.getKey();
            lastValue = entry.getValue();
            long totalTime = System.nanoTime() - start;

            // Calculate average branching factor
            double branching = (totalStates - 1) / (double) (totalStates - leafStates);

            // Print statistics
            StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates,
                    leafStates, branching, totalTime);

            currentDepth++;
        }

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(int timeLimit, Supplier<AbstractMap.SimpleEntry<Move, Double>> searchStrategy) {
        return iterativeDeepeningTimeLimit(timeLimit, e -> searchStrategy.get());
    }

    public static Move iterativeDeepeningTimeLimit(int timeLimit, Function<Double, AbstractMap.SimpleEntry<Move, Double>> searchStrategy) {
        int depthLimit = 100;
        int currentDepth = 1;
        int leafStates = 1;
        int totalStates = 2;
        Move chosenMove = null;
        double lastValue = 0;
        List<Double> branchings = new ArrayList<>();

        // TODO: NEEDS TO BE CALCULATED
        int timeLeft = 1;

        while (currentDepth <= depthLimit) {
            // Do search
            long start = System.nanoTime();
            // TODO: Needs pointers to leafStates, totalStates
            var entry = searchStrategy.apply(lastValue);
            chosenMove = entry.getKey();
            lastValue = entry.getValue();
            long totalTime = System.nanoTime() - start;

            // Calculate branching factor
            branchings.add(currentDepth,
                    (totalStates - 1) / (double) (totalStates - leafStates));
            double averageBranching = branchings.stream().mapToDouble(d -> d).sum() / (double) currentDepth;

            // Make time estimation for next iteration
            double estimatedTime = averageBranching
                    * leafStates
                    * (totalTime / (double) totalStates);

            // Print statistics
            StatisticsUtil.printStatisticsWithEstimation(currentDepth, chosenMove, totalStates,
                    leafStates, branchings.get(currentDepth), totalTime, timeLeft, estimatedTime,
                    averageBranching);

            // Check whether enough time is left for next iteration
            if (timeLeft <= estimatedTime) {
                System.out.println("No time left!");
            }

            currentDepth++;
        }

        return chosenMove;
    }
}