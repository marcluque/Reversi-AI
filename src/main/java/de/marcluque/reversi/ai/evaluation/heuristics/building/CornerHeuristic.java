package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class CornerHeuristic extends AbstractHeuristic implements Heuristic {

    public CornerHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {
        if (Arrays.stream(map.getOverrideStones()).sum() == 0) {
            super.weight = 1;
        }

        MapUtil.iterateMap((x, y) -> {
            if (MapUtil.isTileCorner(map, x, y)) {
                Map.getCorners().add(new Coordinate(x, y));
            }
        });
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        double count = 0;
        for (Coordinate corner : Map.getCorners()) {
            if (map.getGameField()[corner.getY()][corner.getX()] == player) {
                count++;
            }
        }

        return count / Map.getCorners().size();
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