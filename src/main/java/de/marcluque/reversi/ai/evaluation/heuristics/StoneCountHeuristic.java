package de.marcluque.reversi.ai.evaluation.heuristics;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

public class StoneCountHeuristic extends AbstractHeuristic implements Heuristic {

    public StoneCountHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {
        int numberOfHoles = 0;
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                if (MapUtil.isTileHole(map.getGameField()[y][x])) {
                    numberOfHoles++;
                }
            }
        }

        Map.setNumberOfHoles(numberOfHoles);
    }

    @Override
    public double executeHeuristic(Map map, char player) {
        return map.getNumberOfStones()[MapUtil.playerToInt(player)]
                / (double) ((Map.getMapHeight() * Map.getMapWidth()) - Map.getNumberOfHoles());
    }
}