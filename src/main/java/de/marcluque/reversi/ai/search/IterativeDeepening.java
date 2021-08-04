package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.StatisticsUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class IterativeDeepening {

    @FunctionalInterface
    public interface SearchStrategy {
        Move apply(int[] states, int depthLimit);
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit, SearchStrategy searchStrategy) {
        Move chosenMove = null;
        int[] totalStatesUntilDepth = new int[1];
        int prevStatesPerDepth = 1;

        long timeUntilDepth = 0;
        long totalTime = 0;

        int leafStates = 0;
        int prevLeafStates = 1;

        double summedBranching = 0;
        double lastBranchingFactor = 0;

        for (int currentDepth = 1; currentDepth <= depthLimit; currentDepth++) {
            totalStatesUntilDepth[0] = 0;
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStatesUntilDepth, currentDepth);
            timeUntilDepth = System.nanoTime() - start;
            totalTime += timeUntilDepth;

            // TODO: Is this necessary?
            if (chosenMove == null) {
                Rules.useOverrideStones = true;
            }

            // Count states
            leafStates = totalStatesUntilDepth[0] - prevStatesPerDepth;
            prevStatesPerDepth = totalStatesUntilDepth[0];

            // Branching factor (NOT average over the whole tree, just the last two depths)
            lastBranchingFactor = (double) leafStates / prevLeafStates;
            summedBranching += lastBranchingFactor;
            prevLeafStates = leafStates;
        }

        // Print statistics
        StatisticsUtil.printAllStatsWithoutEstimation(depthLimit, chosenMove, totalStatesUntilDepth[0], leafStates,
                lastBranchingFactor, summedBranching / depthLimit, timeUntilDepth, totalTime);

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy searchStrategy) {
        Move chosenMove = null;
        int currentDepth;
        int[] totalStatesUntilDepth = new int[1];
        int prevStatesPerDepth = 1;

        double estimatedTime = 0;
        double timeUntilDepth = 0;
        long totalTime = 0;

        int leafStates = 0;
        int prevLeafStates = 1;

        double summedBranching = 0;
        double lastBranchingFactor = 0;
        double branchingAverage = 0;

        for (currentDepth = 1; GameInstance.getLeftTime() >= estimatedTime; currentDepth++) {
            totalStatesUntilDepth[0] = 0;
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStatesUntilDepth, currentDepth);
            timeUntilDepth = System.nanoTime() - start;
            totalTime += timeUntilDepth;

            // TODO: Is this necessary?
            if (chosenMove == null) {
                Rules.useOverrideStones = true;
            }

            // Count states
            leafStates = totalStatesUntilDepth[0] - prevStatesPerDepth;
            prevStatesPerDepth = totalStatesUntilDepth[0];

            // Branching factor (NOT average over the whole tree, just the last two depths)
            lastBranchingFactor = (double) leafStates / prevLeafStates;
            summedBranching += lastBranchingFactor;
            prevLeafStates = leafStates;

            // TODO: Make better estimations, still miscalculating
            // Make time estimation for next iteration (everything in ms)
            branchingAverage = summedBranching / currentDepth;
            double timeUntilDepthInMs = timeUntilDepth / 1_000_000;
            double statesOnNextDepth = branchingAverage * leafStates;
            double timePerState = (timeUntilDepthInMs / totalStatesUntilDepth[0]);
            double estimatedTimeForNextDepth = statesOnNextDepth * timePerState;
            estimatedTime = timeUntilDepthInMs + estimatedTimeForNextDepth;
        }

        // Print statistics
        currentDepth--;
        StatisticsUtil.printAllStats(currentDepth, chosenMove, totalStatesUntilDepth[0],
                leafStates, lastBranchingFactor, branchingAverage, timeUntilDepth, totalTime,
                GameInstance.getLeftTime(), estimatedTime);

        return chosenMove;
    }
}