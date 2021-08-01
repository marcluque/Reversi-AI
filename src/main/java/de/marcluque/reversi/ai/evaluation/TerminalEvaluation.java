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
        double playerWithMostStones = MapUtil.playerWithMaxStones(map);
        int maxPlayerStones = map.getNumberOfStones()[AbstractSearch.MAX_NUMBER];

        // Return 1 for first place in ranking and lowest possible value for last place in ranking
        return playerWithMostStones / maxPlayerStones;
    }

    public static double[] nPlayerUtility(Map map) {
        // Find player with max number of stones
        double playerWithMostStones = MapUtil.playerWithMaxStones(map);
        double[] utilities = new double[Map.getNumberOfPlayers() + 1];

        for (int player = 1; player <= utilities.length; player++) {
            utilities[player] = map.getNumberOfStones()[player] / playerWithMostStones;
        }

        // Return 1 for first place in ranking and lowest possible value for last place in ranking
        return utilities;
    }
}