package de.marcluque.reversi.map;

import de.marcluque.reversi.ai.evaluation.metrics.Metrics;
import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBetaMoveSorting;
import de.marcluque.reversi.util.Move;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class GameInstance {

    private static Map map;

    private static int depthLimit;

    private static long timeLimit;

    private static long startTime;

    public static final long TIME_BUFFER = 200;

    public static Move generateMoveResponse() {
        ////////////////////////
        //    RULE UPDATES    //
        ////////////////////////
        Rules.updateOverrideStoneRule(map);

        ////////////////////////
        //     SEARCH MOVE    //
        ////////////////////////
        Move responseMove;
        IterativeDeepening.SearchStrategy searchStrategy;

        if (Map.getNumberOfPlayers() == 2 || Metrics.OPPONENTS_WITH_MOVES.size() == 1) {
            AbstractSearch.MIN = Metrics.OPPONENTS_WITH_MOVES.get(0);
            searchStrategy = (totalStates) -> AlphaBetaMoveSorting.search(map, depthLimit, totalStates);
        } else {
            searchStrategy = (totalStates) -> BestReplySearch.search(map, depthLimit, totalStates);
        }

        if (timeLimit == 0) {
            responseMove = IterativeDeepening.iterativeDeepeningDepthLimit(depthLimit, searchStrategy);
        } else {
            responseMove = IterativeDeepening.iterativeDeepeningTimeLimit(searchStrategy);
        }

        return responseMove;
    }

    public static long getLeftTime() {
        return timeLimit - (startTime - System.currentTimeMillis());
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
}