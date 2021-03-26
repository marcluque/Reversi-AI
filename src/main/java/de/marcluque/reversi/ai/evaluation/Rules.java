package de.marcluque.reversi.ai.evaluation.rules;

import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Rules {

    public static boolean useOverrideStones = false;

    public static boolean useStoneMaximization = false;

    public static boolean useFullGameTreeSearch = false;

    public static int moveThresholdFullGameTreeSearch = -1;

    public static void updateOverrideStoneRule() {
        useOverrideStones = GameInstance.getMap().getNumberFreeTiles() - Map.getNumberOfHoles() == 0;
    }

    public static void updateStoneMaximizationRule() {
        useStoneMaximization = GameInstance.getMap().getNumberFreeTiles() == 0;
    }

    public static void updateFullGameTreeSearch() {
        useFullGameTreeSearch = moveThresholdFullGameTreeSearch
                >= GameInstance.getMap().getNumberFreeTiles() + Arrays.stream(GameInstance.getMap().getOverrideStones()).sum();
    }
}