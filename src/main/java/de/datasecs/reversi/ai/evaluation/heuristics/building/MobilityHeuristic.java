package de.datasecs.reversi.ai.evaluation.heuristics.building;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.ai.evaluation.rules.Rules;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.AbstractMove;
import de.datasecs.reversi.util.Coordinate;

import java.util.LinkedList;
import java.util.List;

public class MobilityHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int numberOfMoves = 0;
        List<Coordinate> temp = new LinkedList<>();
        boolean allowOverrideStones = Rules.getRule(Rules.StdRules.OVERRIDE_STONES.getIndex());

        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                if (AbstractMove.isMoveValidImpl(map, j, i, player, true, allowOverrideStones, temp, 1)) {
                    numberOfMoves++;
                }
            }
        }

        return numberOfMoves / (double) (Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles());
    }
}