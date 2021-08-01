package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StrongestOpponentHeuristic extends AbstractHeuristic implements Heuristic {

    public StrongestOpponentHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {

    }

    @Override
    public double executeHeuristic(Map map, char player) {
        int playerInt = MapUtil.playerToInt(player);

        // Determine strongest opponent
        int strongestOpponent = -1;
        int max = Integer.MIN_VALUE;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if (i != playerInt && map.getNumberOfStones()[i] > max) {
                max = map.getNumberOfStones()[i];
                strongestOpponent = i;
            }
        }

        // The more stones the strongest opponent has, the higher is the heuristic value
        double numberOfPlayableTiles = Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles() - map.getNumberFreeTiles();
        return 1 - ((numberOfPlayableTiles - map.getNumberOfStones()[strongestOpponent]) / numberOfPlayableTiles);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void updateWeight(double weight) {
        super.weight = weight;
    }
}