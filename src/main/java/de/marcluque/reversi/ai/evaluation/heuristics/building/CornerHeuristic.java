package de.datasecs.reversi.ai.evaluation.heuristics.building;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.Coordinate;
import de.datasecs.reversi.util.MapUtil;

public class CornerHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                if (MapUtil.isTileCorner(map, x, y) != null) {
                    Map.getCorners().add(new Coordinate(x, y));
                }
            }
        }
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        return Map.getCorners()
                .stream()
                .filter(corner -> map.getGameField()[corner.getY()][corner.getX()] == player)
                .count() / (double) Map.getCorners().size();
    }
}