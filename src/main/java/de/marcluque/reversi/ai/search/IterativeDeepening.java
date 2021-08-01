package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.util.Logger;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.StatisticsUtil;

import java.util.ArrayList;

/*
 * Created with <3 by marcluque, March 2021
 */
public class IterativeDeepening {

    @FunctionalInterface
    public interface SearchStrategy {
        Move apply(int[] states, int depthLimit);
    }

    @FunctionalInterface
    public interface PostSearchAction {
        void apply(int leafStates, double averageBranchingForDepth, double meanBranchingFactor, long totalTimeUntilDepth);
    }

    private static int currentDepth;

    private static int[] totalStates;

    private static ArrayList<Integer> statesPerDepth = new ArrayList<>();

    private static long totalTime;

    private static Move chosenMove;

    private static final PostSearchAction postSearchActionTimeLimit;

    private static boolean continueSearch;

    private static double summedBranching;

    static  {
        continueSearch = true;
        summedBranching = 0;

        postSearchActionTimeLimit = (leafStates, branchingForDepth, meanBranchingFactor, totalTimeUntilDepth) -> {

        };
    }

    public static Move iterativeDeepeningDepthLimit(int depthLimit, SearchStrategy searchStrategy) {
        currentDepth = 1;
        chosenMove = null;
        totalStates = new int[1];
        long timeForFullDepth = 0;
        statesPerDepth = new ArrayList<>();
        statesPerDepth.add(0, 1);
        double avgBranchingFactor = 0;

        while (currentDepth <= depthLimit) {
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStates, currentDepth);

            if (chosenMove == null) {
                Rules.useOverrideStones = true;
            }

            timeForFullDepth = System.nanoTime() - start;
            totalTime += timeForFullDepth;
            statesPerDepth.add(currentDepth, totalStates[0]);

            avgBranchingFactor += (double) statesPerDepth.get(currentDepth) / statesPerDepth.get(currentDepth - 1);
            currentDepth++;
        }

        currentDepth--;
        int leafStates = statesPerDepth.get(currentDepth) - statesPerDepth.get(currentDepth - 1);

        // Calculate average branching factor
        avgBranchingFactor /= depthLimit;

        // Print statistics
        StatisticsUtil.printStatistics(currentDepth, chosenMove, totalStates[0],
                leafStates, avgBranchingFactor, timeForFullDepth, totalTime);

        return chosenMove;
    }

    public static Move iterativeDeepeningTimeLimit(SearchStrategy searchStrategy) {
        continueSearch = true;
        summedBranching = 0;
        currentDepth = 1;
        chosenMove = null;
        long totalTimeUntilDepth = 0;
        double meanBranchingFactor = 0;
        int leafStates = 0;
        double branchingForDepth = 0;

        while (continueSearch) {
            // Do search
            long start = System.nanoTime();
            chosenMove = searchStrategy.apply(totalStates, currentDepth);
            totalTimeUntilDepth = System.nanoTime() - start;
            totalTime += totalTimeUntilDepth;

            // Count states
            statesPerDepth.add(currentDepth, totalStates[0]);
            leafStates = statesPerDepth.get(currentDepth) - statesPerDepth.get(currentDepth - 1);

            // Calculate mean branching factor
            double nonTerminalStates = totalStates[0] - leafStates;
            meanBranchingFactor = totalStates[0] / nonTerminalStates;

            // Branching factor (NOT average over the whole tree, just the last two states) at currentDepth - 1
            if (currentDepth >= 1) {
                branchingForDepth = (double) leafStates / statesPerDepth.get(currentDepth - 1);
            }





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
                    branchingForDepth, branchingAverage);




            currentDepth++;
        }

        return chosenMove;
    }
}