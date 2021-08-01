package de.marcluque.reversi.util;

import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.ai.search.strategies.maxn.MaxNSearch;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBeta;
import de.marcluque.reversi.ai.search.strategies.minimax.AlphaBetaMoveSorting;
import de.marcluque.reversi.ai.search.strategies.opps.OpponentPruningParanoidSearch;
import de.marcluque.reversi.map.Map;

public class InputParser {

    enum Strategy {
        BRS, MAX_N, AB, AB_MOVE_SORTING, OPPS, PARANOID_AB, PARANOID;
    }

    private static boolean userChoice;

    private static Strategy strategyType;

    private static IterativeDeepening.SearchStrategy userStrategy;

    public static void parse(String[] args) {
        for (String argument : args) {
            switch (argument) {
                case "-brs" -> strategyType = Strategy.BRS;
                case "-maxn" -> strategyType = Strategy.MAX_N;
                case "-ab" -> strategyType = Strategy.AB;
                case "-ab-move-sorting" -> strategyType = Strategy.AB_MOVE_SORTING;
                case "-opps" -> strategyType = Strategy.OPPS;
                case "-paranoid-ab" -> strategyType = Strategy.PARANOID_AB;
                case "-paranoid" -> strategyType = Strategy.PARANOID;
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
        switch (strategyType) {
            case BRS -> userStrategy = (totalStates, depthLimit) -> BestReplySearch.search(map, depthLimit, totalStates);
            case MAX_N -> userStrategy = (totalStates, depthLimit) -> MaxNSearch.search(map, depthLimit, totalStates);
            case AB -> {
                AbstractSearch.MIN = AbstractSearch.MAX_NUMBER == 1 ? '2' : '1';
                userStrategy = (totalStates, depthLimit) -> AlphaBeta.search(map, depthLimit, totalStates);
            }
            case AB_MOVE_SORTING -> {
                AbstractSearch.MIN = AbstractSearch.MAX_NUMBER == 1 ? '2' : '1';
                userStrategy = (totalStates, depthLimit) -> AlphaBetaMoveSorting.search(map, depthLimit, totalStates);
            }
            case OPPS -> userStrategy = (totalStates, depthLimit) -> OpponentPruningParanoidSearch.search(map, depthLimit, totalStates);
            //case PARANOID_AB -> userStrategy = (totalStates) -> ParanoidAlphaBetaSearch.search(map, depthLimit, totalStates);
            //case PARANOID -> userStrategy = (totalStates) -> ParanoidSearch.search(map, depthLimit, totalStates);
        }

        return userStrategy;
    }
}
