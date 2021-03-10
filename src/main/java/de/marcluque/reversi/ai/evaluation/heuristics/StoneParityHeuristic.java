package de.marcluque.reversi.ai.evaluation.heuristics;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;

import java.util.Arrays;

public class StoneParityHeuristic extends AbstractHeuristic implements Heuristic {

    public StoneParityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map) {
        int coinParity = 0;
        int[] num = map.getNumberOfStones();

        for (int j = 1; j < num.length; j++) {
            coinParity += (num[AbstractSearch.MAX] - num[j]);
        }

        return coinParity / ((double) Arrays.stream(num).sum() * num.length);
    }
}