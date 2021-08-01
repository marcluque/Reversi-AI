package de.marcluque.reversi.ai.evaluation.heuristics;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StoneParityHeuristic extends AbstractHeuristic implements Heuristic {

    public StoneParityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int coinParity = 0;
        int[] num = map.getNumberOfStones();

        for (int j = 1; j < num.length; j++) {
            coinParity += num[MapUtil.playerToInt(player)] - num[j];
        }

        return coinParity / ((double) Arrays.stream(num).sum() * num.length);
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