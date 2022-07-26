package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class TerminalEvaluation {

    private TerminalEvaluation() {}

    public static double utility(Map map) {
        return (double) map.getNumberOfStones()[AbstractSearch.getMaxId()] / Arrays.stream(map.getNumberOfStones()).sum();
    }

    public static double[] nPlayerUtility(Map map) {
        // Find player with max number of stones
        int playerWithMostStones = MapUtil.playerWithMaxStones(map);
        double[] utilities = new double[Map.getNumberOfPlayers()];

        for (int player = 1; player < utilities.length; player++) {
            utilities[player] = (double) map.getNumberOfStones()[player] / map.getNumberOfStones()[playerWithMostStones];
        }

        // Return 1 for first place in ranking and lowest value for last place in ranking
        return utilities;
    }
}