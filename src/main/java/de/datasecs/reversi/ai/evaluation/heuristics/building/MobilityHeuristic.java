package de.datasecs.reversi.ai.evaluation.heuristics.building;

import de.datasecs.reversi.ai.evaluation.heuristics.Heuristic;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.moves.Move;
import de.datasecs.reversi.util.Coordinate;

import java.util.LinkedList;
import java.util.List;

public class MobilityHeuristic implements Heuristic {

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player, boolean allowOverrideStones) {
        int numberOfMoves = 0;
        List<Coordinate> temp = new LinkedList<>();

        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                if (Move.isMoveValidImpl(map, j, i, player, true, allowOverrideStones, temp, 1)) {
                    numberOfMoves++;
                }
            }
        }

        return numberOfMoves / (double) (Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles());
    }
}