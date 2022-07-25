package de.marcluque.reversi.map;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.ai.search.strategies.maxn.MaxNSearch;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBetaMoveSorting;
import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Created with <3 by marcluque, March 2021
 */
public class GameInstance {

    private static Map map;

    private static int depthLimit;

    private static long timeLimit;

    private static long startTime;

    public static final long TIME_BUFFER = 200;

    private static int moveCount;

    public static void processMove(int x, int y, int specialField, char player) {
        moveCount++;
        Set<Coordinate> capturableStones = new HashSet<>();
        boolean allowOverrideStones = player != AbstractSearch.MAX_NUMBER || Rules.useOverrideStones;
        boolean moveIsValid = Move.isMoveValid(map, x, y, player, false, allowOverrideStones, capturableStones);
        if (moveIsValid) {
            Move.executeMove(map, x, y, specialField, player, capturableStones);
        }

        if (player != AbstractSearch.MAX) {
            if (moveIsValid) {
                Logger.print("ANNOUNCED MOVE " + moveCount + ": (" + x + "," + y + ") with special " + specialField
                        + " by player " + player);
            } else {
                Logger.error("ILLEGAL MOVE " + moveCount + ": (" + x + "," + y + ") with special " + specialField + " by player "
                        + player);
            }
        }

        ////////////////////////
        //   METRICS UPDATES  //
        ////////////////////////
        Metrics.updateOpponentsWithMoves();
        Metrics.updateBombPower();
        Metrics.updateOverrideEffect();

        ////////////////////////
        //    RULE UPDATES    //
        ////////////////////////
        Rules.updateOverrideStoneRule();
        Rules.updateStoneMaximizationRule();
        Rules.updateFullGameTreeSearch();
        Rules.updateOverrideOverBombRule();

        if (Rules.useStoneMaximization) {
            HeuristicEvaluation.activateStoneMaximization();
        }
    }

    public static MoveTriplet generateMoveResponse() {
        MoveTriplet responseMoveTriplet;
        IterativeDeepening.SearchStrategy searchStrategy;

        // If we have a 2-Player map or only one opponent has moves available, we use classical MiniMax/Alpha-Beta
        if (InputParser.isUserChoice()) {
            searchStrategy = InputParser.getUserStrategy(map);
        } else if (Map.getNumberOfPlayers() == 2 || Metrics.opponentsWithMoves.size() == 1) {
            AbstractSearch.MIN = Metrics.opponentsWithMoves.get(0);
            searchStrategy = (totalStates, depthLimit) -> AlphaBetaMoveSorting.search(map, depthLimit, totalStates);
        }
        // We don't have a two player game, but are close to the end, so start doing a full tree search
        else if (Rules.useFullGameTreeSearch) {
            searchStrategy = (totalStates, depthLimit) -> MaxNSearch.search(map, depthLimit, totalStates);
        }
        // We are mid-game, so just do the search that the user picked
        else {
            searchStrategy = (totalStates, depthLimit) -> BestReplySearch.search(map, depthLimit, totalStates);
        }

        if (timeLimit == 0) {
            responseMoveTriplet = IterativeDeepening.iterativeDeepeningDepthLimit(depthLimit, searchStrategy);
//            int[] totalStates = new int[1];
//            long start = System.nanoTime();
//            responseMove = searchStrategy.apply(totalStates, depthLimit);
//            long end = System.nanoTime();
//            System.out.println();
//            System.out.println("STATES: " + totalStates[0]);
//            System.out.println("TIME (ms): " + (end - start) / 1_000_000d);
//            System.out.println("TIME per state (µs): " + ((end - start) / 1_000d) / totalStates[0]);
//            System.out.println();
        } else {
            responseMoveTriplet = IterativeDeepening.iterativeDeepeningTimeLimit(searchStrategy);
        }

        return responseMoveTriplet;
    }

    public static long getLeftTime() {
        return timeLimit - (System.currentTimeMillis() - startTime);
    }

    public static Map getMap() {
        return map;
    }

    public static void setMap(Map map) {
        GameInstance.map = map;
    }

    public static void setDepthLimit(int depthLimit) {
        GameInstance.depthLimit = depthLimit;
    }

    public static long getTimeLimit() {
        return timeLimit;
    }

    public static void setTimeLimit(long timeLimit) {
        GameInstance.timeLimit = timeLimit;
    }

    public static void setStartTime(long startTime) {
        GameInstance.startTime = startTime;
    }

    public static int getMoveCount() {
        return moveCount;
    }
}