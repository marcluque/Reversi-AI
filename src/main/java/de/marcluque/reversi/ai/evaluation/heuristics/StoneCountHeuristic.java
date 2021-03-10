package de.datasecs.reversi.ai.evaluation.heuristics;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.util.MapUtil;

public class StoneCountHeuristic implements Heuristic {
    
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
        return map.getNumberOfStones()[MapUtil.playerToInt(player)] / (double) (Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles());
    }
}