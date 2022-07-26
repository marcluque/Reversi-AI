package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Rules {

    private Rules() {}

    private static boolean useOverrideStones = false;

    private static boolean useStoneMaximization = false;

    private static boolean useFullGameTreeSearch = false;

    private static int moveThresholdFullGameTreeSearch = -1;

    private static boolean pickOverrideStoneOverBomb;

    public static void updateOverrideStoneRule() {
        useOverrideStones = GameInstance.getMap().getNumberFreeTiles() == 0
                || MapUtil.noMovesPossibleForPlayer(GameInstance.getMap(), AbstractSearch.getMax());
    }

    public static void updateStoneMaximizationRule() {
        useStoneMaximization = useOverrideStones;
    }

    public static void updateFullGameTreeSearch() {
        // TODO: Override stones allow for many possible moves, this is not regarded
        useFullGameTreeSearch = moveThresholdFullGameTreeSearch >= GameInstance.getMap().getNumberFreeTiles()
                + Arrays.stream(GameInstance.getMap().getOverrideStones()).sum();
    }

    public static void updateOverrideOverBombRule() {
        // We pick override stones over bombs while bomb power does not exceed power of destroying half the playable tiles
        pickOverrideStoneOverBomb = Metrics.getMaximalOverrideEffect() >= Metrics.getMaximalBombPower();
    }

    public static boolean isUseOverrideStones() {
        return useOverrideStones;
    }

    public static boolean isUseStoneMaximization() {
        return useStoneMaximization;
    }

    public static boolean isUseFullGameTreeSearch() {
        return useFullGameTreeSearch;
    }

    public static int getMoveThresholdFullGameTreeSearch() {
        return moveThresholdFullGameTreeSearch;
    }

    public static void setMoveThresholdFullGameTreeSearch(int moveThresholdFullGameTreeSearch) {
        Rules.moveThresholdFullGameTreeSearch = moveThresholdFullGameTreeSearch;
    }

    public static boolean isPickOverrideStoneOverBomb() {
        return pickOverrideStoneOverBomb;
    }
}