package de.marcluque.reversi.ai.evaluation.heuristics;

import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StoneCountHeuristic extends AbstractHeuristic implements Heuristic {

    public StoneCountHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        return map.getNumberOfStones()[MapUtil.playerToInt(player)] / (double) Metrics.numberPlayableTiles;
    }

    @Override
    public void updateWeight(double weight) {
        super.weight = weight;
    }
}