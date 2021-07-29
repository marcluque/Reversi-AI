package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;

import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class OverrideTileHeuristic extends AbstractHeuristic implements Heuristic {

    public OverrideTileHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {
        MapUtil.iterateMap((x, y) -> {
            if (MapUtil.isTileBonus(map.getGameField()[y][x])) {
                Map.getOverrideTiles().add(new Coordinate(x, y));
            }
        });
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        double count = 0;
        List<Coordinate> overrideTiles = Map.getOverrideTiles();
        for (Coordinate bonusTile : overrideTiles) {
            if (map.getGameField()[bonusTile.getY()][bonusTile.getX()] == player) {
                count++;
            }
        }

        return count / overrideTiles.size();
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