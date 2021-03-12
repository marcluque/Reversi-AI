package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class PredecessorHeuristic extends AbstractHeuristic implements Heuristic {

    public PredecessorHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        // Determine predecessor
        int playerStones = map.getNumberOfStones()[MapUtil.playerToInt(player)];
        int min = Integer.MAX_VALUE;
        int predecessor = -1;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if ((playerStones - map.getNumberOfStones()[i]) < min) {
                min = playerStones - map.getNumberOfStones()[i];
                predecessor = i;
            }
        }

        int numberOfPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles();
        return (numberOfPlayableTiles - map.getNumberOfStones()[predecessor]) / (double) numberOfPlayableTiles;
    }
}