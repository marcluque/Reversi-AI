package de.marcluque.reversi.map;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.metrics.Metrics;
import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.ai.search.strategies.maxn.MaxNSearch;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBetaMoveSorting;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class GameInstance {

    private static Map map;

    private static int depthLimit;

    private static long timeLimit;

    private static long startTime;

    public static final long TIME_BUFFER = 200;

    public static void processOpponentMove(int x, int y, int specialField, char opponent) {
        List<Coordinate> capturableTiles = new ArrayList<>();
        if (AbstractMove.isMoveValid(map, x, y, opponent, false, Rules.useOverrideStones, capturableTiles)) {
            AbstractMove.executeMove(map, x, y, opponent, capturableTiles);
        } else {
            System.err.println("OPPONENT MOVE: (" + x + "," + y + ") with special " + specialField + " by player "
                    + opponent + " wasn't valid!");
        }

        System.out.println("OPPONENT MOVE: (" + x + "," + y + ") with special " + specialField
                + " by player " + opponent);

        ////////////////////////
        //   METRICS UPDATES  //
        ////////////////////////
        Metrics.updateOpponentsWithMoves();

        ////////////////////////
        //    RULE UPDATES    //
        ////////////////////////
        Rules.updateOverrideStoneRule();
        Rules.updateStoneMaximizationRule();
        Rules.updateFullGameTreeSearch();

        if (Rules.useStoneMaximization) {
            HeuristicEvaluation.activateStoneMaximization();
        }
    }

    public static Move generateMoveResponse() {
        Move responseMove;
        IterativeDeepening.SearchStrategy searchStrategy;

        // If we have a 2-Player map or only one opponent has moves available, we use classical MiniMax/Alpha-Beta
        if (Map.getNumberOfPlayers() == 2 || Metrics.opponentsWithMoves.size() == 1) {
            AbstractSearch.MIN = Metrics.opponentsWithMoves.get(0);
            searchStrategy = (totalStates) -> AlphaBetaMoveSorting.search(map, depthLimit, totalStates);
        }
        // We don't have a two player game, but are close to the end, so start doing a full tree search
        else if (Rules.useFullGameTreeSearch) {
            searchStrategy = (totalStates) -> MaxNSearch.search(map, depthLimit, totalStates);
        }
        // We are mid-game, so just do the search that the user picked
        else {
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