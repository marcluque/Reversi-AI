package de.marcluque.reversi.ai.evaluation.rules;

import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

public class Rules {

    public static boolean OVERRIDE_STONES = false;

    public static void updateOverrideStoneRule(Map map) {
        int freeTiles = 0;
        for (int y = 0; y < Map.getMapHeight(); y++) {
            for (int x = 0; x < Map.getMapWidth(); x++) {
                if (!MapUtil.isOccupied(map.getGameField()[y][x])) {
                    freeTiles++;
                }
            }
        }

        OVERRIDE_STONES = freeTiles - Map.getNumberOfHoles() == 0;
    }
}