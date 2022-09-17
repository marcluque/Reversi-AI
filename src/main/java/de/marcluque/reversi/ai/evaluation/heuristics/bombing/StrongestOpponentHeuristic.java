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
    public void initHeuristic(Map map) {
        // Heuristic has no state, so no initialization required
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        int playerInt = MapUtil.playerToInt(player);
        long sum = 0;

        // Determine strongest opponent
        double maxStoneCount = 0;
        for (int i = 1, numberOfStonesLength = map.getNumberOfStones().length; i < numberOfStonesLength; i++) {
            int stoneCount = map.getNumberOfStones()[i];
            if (i != playerInt) {
                maxStoneCount = Math.max(maxStoneCount, map.getNumberOfStones()[i]);
            }
            sum += stoneCount;
        }

        // The greater the distance to the strongest opponent, the better
        return Math.abs(map.getNumberOfStones()[playerInt] - maxStoneCount) / Math.max(sum, 1);
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