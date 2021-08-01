package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class TerminalEvaluation {

    public static double utility(Map map) {
        // Find player with max number of stones
        int playerWithMostStones = MapUtil.playerWithMaxStones(map);
        double maxPlayerStones = map.getNumberOfStones()[AbstractSearch.MAX_NUMBER];

        // Return 1 for first place in ranking and lowest value for last place in ranking
        return maxPlayerStones / map.getNumberOfStones()[playerWithMostStones];
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