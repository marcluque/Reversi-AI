package de.datasecs.reversi.ai.evaluation.heuristics;

import de.datasecs.reversi.map.Map;

import java.util.Arrays;

public class StoneParityHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int coinParity = 0;
        int[] num = map.getNumberOfStones();

        for (int j = 1; j < num.length; j++) {
            coinParity += (num[player] - num[j]);
        }

        return coinParity / ((double) Arrays.stream(num).sum() * num.length);
    }
}