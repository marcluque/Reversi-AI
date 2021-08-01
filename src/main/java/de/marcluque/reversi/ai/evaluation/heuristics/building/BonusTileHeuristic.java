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
public class BonusTileHeuristic extends AbstractHeuristic implements Heuristic {

    public BonusTileHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {
        MapUtil.iterateMap((x, y) -> {
            if (MapUtil.isTileBonus(map.getGameField()[y][x])) {
                Map.getBonusTiles().add(new Coordinate(x, y));
            }
        });
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        double count = 0;
        List<Coordinate> bombTiles = Map.getBonusTiles();
        for (Coordinate bonusTile : bombTiles) {
            if (map.getGameField()[bonusTile.getY()][bonusTile.getX()] == player) {
                count++;
            }
        }

        return bombTiles.size() == 0 ? 0 : count / bombTiles.size();
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