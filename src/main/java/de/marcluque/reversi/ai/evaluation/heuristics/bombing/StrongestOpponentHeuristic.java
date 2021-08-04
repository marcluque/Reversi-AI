package de.marcluque.reversi.ai.evaluation.heuristics.bombing;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StrongestOpponentHeuristic extends AbstractHeuristic implements Heuristic {

    public StrongestOpponentHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int playerInt = MapUtil.playerToInt(player);

        // Determine strongest opponent
        double max = Integer.MIN_VALUE;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            if (i != playerInt && map.getNumberOfStones()[i] > max) {
                max = map.getNumberOfStones()[i];
            }
        }

        // The greater the distance to the strongest opponent, the better
        return Math.abs(map.getNumberOfStones()[playerInt] - max) / Arrays.stream(map.getNumberOfStones()).sum();
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