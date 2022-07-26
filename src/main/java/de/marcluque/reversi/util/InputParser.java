package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.ai.search.strategies.maxn.MaxNSearch;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBeta;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBetaMoveSorting;
import de.marcluque.reversi.ai.search.strategies.opps.OpponentPruningParanoidSearch;
import de.marcluque.reversi.map.Map;

/*
 * Created with <3 by marcluque, August 2021
 */
public class InputParser {

    enum Strategy {
        BRS, MAX_N, AB, AB_MOVE_SORTING, OPPS, PARANOID_AB, PARANOID
    }

    private static boolean userChoice;

    private static Strategy strategyType;

    public static void parse(String[] args) {
        for (String argument : args) {
            switch (argument) {
                case "-brs": strategyType = Strategy.BRS; break;
                case "-maxn": strategyType = Strategy.MAX_N; break;
                case "-ab": strategyType = Strategy.AB; break;
                case "-ab-move-sorting": strategyType = Strategy.AB_MOVE_SORTING; break;
                case "-opps": strategyType = Strategy.OPPS; break;
                case "-paranoid-ab": strategyType = Strategy.PARANOID_AB; break;
                case "-paranoid": strategyType = Strategy.PARANOID; break;
                default:
                    throw new UnsupportedOperationException(String.format("Unknown option: %s", argument));
            }
        }
    }

    public static boolean isUserChoice() {
        return userChoice;
    }

    public static void setUserChoice(boolean userChoice) {
        InputParser.userChoice = userChoice;
    }

    public static IterativeDeepening.SearchStrategy getUserStrategy(Map map) {
        IterativeDeepening.SearchStrategy userStrategy;
        switch (strategyType) {
            case BRS:
                userStrategy = (totalStates, depthLimit) -> BestReplySearch.search(map, depthLimit, totalStates);
                break;
            case MAX_N:
                userStrategy = (totalStates, depthLimit) -> MaxNSearch.search(map, depthLimit, totalStates);
                break;
            case AB:
                AbstractSearch.setMin(AbstractSearch.getMaxId() == 1 ? '2' : '1');
                userStrategy = (totalStates, depthLimit) -> AlphaBeta.search(map, depthLimit, totalStates);
                break;
            case AB_MOVE_SORTING:
                AbstractSearch.setMin(AbstractSearch.getMaxId() == 1 ? '2' : '1');
                userStrategy = (totalStates, depthLimit) -> AlphaBetaMoveSorting.search(map, depthLimit, totalStates);
                break;
            case OPPS:
                userStrategy = (totalStates, depthLimit) -> OpponentPruningParanoidSearch.search(map, depthLimit, totalStates);
                break;
            //case PARANOID_AB -> userStrategy = (totalStates) -> ParanoidAlphaBetaSearch.search(map, depthLimit, totalStates);
            //case PARANOID -> userStrategy = (totalStates) -> ParanoidSearch.search(map, depthLimit, totalStates);
            default:
                throw new IllegalStateException(String.format("Unknown strategy: %s", strategyType));
        }

        return userStrategy;
    }
}
