package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.util.MapUtil;

import java.util.Arrays;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Rules {

    public static boolean useOverrideStones = false;

    public static boolean useStoneMaximization = false;

    public static boolean useFullGameTreeSearch = false;

    public static int moveThresholdFullGameTreeSearch = -1;

    public static boolean pickOverrideStoneOverBomb;

    public static void updateOverrideStoneRule() {
        useOverrideStones = GameInstance.getMap().getNumberFreeTiles() == 0
                || MapUtil.noMovesPossibleForPlayer(GameInstance.getMap(), AbstractSearch.MAX);
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
        pickOverrideStoneOverBomb = Metrics.maximalOverrideEffect >= Metrics.maximalBombPower;
    }
}