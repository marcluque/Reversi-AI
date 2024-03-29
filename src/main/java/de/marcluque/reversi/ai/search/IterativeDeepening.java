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
        boolean searchSpaceCompleted = false;
        int currentDepth;
        int[] totalStatesUntilDepth = new int[1];
        int prevStatesPerDepth = 1;

        long timeUntilDepth = 0;
        long totalTime = 0;

        int leafStates = 0;
        int prevLeafStates = 1;

        double summedBranching = 0;
        double lastBranchingFactor = 0;

        for (currentDepth = 1; currentDepth <= depthLimit; currentDepth++) {
            totalStatesUntilDepth[0] = 0;
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStatesUntilDepth, currentDepth);
            timeUntilDepth = System.nanoTime() - start;
            totalTime += timeUntilDepth;

            if (totalStatesUntilDepth[0] - prevStatesPerDepth == 0) {
                searchSpaceCompleted = true;
                totalTime -= timeUntilDepth;
                break;
            }

            // Count states
            leafStates = totalStatesUntilDepth[0] - prevStatesPerDepth;
            prevStatesPerDepth = totalStatesUntilDepth[0];

            // Branching factor (NOT average over the whole tree, just the last two depths)
            lastBranchingFactor = (double) leafStates / prevLeafStates;
            summedBranching += lastBranchingFactor;
            prevLeafStates = leafStates;
        }

        currentDepth--;
        StatisticsUtil.printAllStatsWithoutEstimation(currentDepth, chosenMove, totalStatesUntilDepth[0], leafStates,
                lastBranchingFactor, summedBranching / currentDepth, timeUntilDepth, totalTime,
                searchSpaceCompleted);

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy searchStrategy) {
        Move chosenMove = null;
        boolean searchSpaceCompleted = false;
        int currentDepth;
        int[] totalStatesUntilDepth = new int[1];
        int prevStatesPerDepth = 1;

        double estimatedTime = 0;
        double prevEstimatedTime = 0;
        double timeUntilDepth = 0;
        long totalTime = 0;

        int leafStates = 0;
        int prevLeafStates = 1;

        double summedBranching = 0;
        double lastBranchingFactor = 0;

        for (currentDepth = 1; GameInstance.getLeftTime() >= estimatedTime; currentDepth++) {
            totalStatesUntilDepth[0] = 0;
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStatesUntilDepth, currentDepth);
            timeUntilDepth = System.nanoTime() - start;
            totalTime += timeUntilDepth;

            if (totalStatesUntilDepth[0] - prevStatesPerDepth == 0) {
                searchSpaceCompleted = true;
                totalTime -= timeUntilDepth;
                break;
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
            double timeUntilDepthInMs = timeUntilDepth / 1_000_000;
            double statesOnNextDepth = Math.max(lastBranchingFactor, summedBranching / currentDepth) * leafStates;
            double timePerState = (timeUntilDepthInMs / totalStatesUntilDepth[0]);
            double estimatedTimeForNextDepth = statesOnNextDepth * timePerState;
            prevEstimatedTime = estimatedTime;
            estimatedTime = timeUntilDepthInMs + estimatedTimeForNextDepth;
        }

        currentDepth--;
        StatisticsUtil.printAllStats(currentDepth, chosenMove, totalStatesUntilDepth[0], leafStates,
                lastBranchingFactor, summedBranching / currentDepth, timeUntilDepth, prevEstimatedTime, totalTime,
                GameInstance.getLeftTime(), estimatedTime, searchSpaceCompleted);

        return chosenMove;
    }
}