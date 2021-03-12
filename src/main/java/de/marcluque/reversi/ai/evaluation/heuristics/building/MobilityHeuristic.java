package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;

import java.util.LinkedList;
import java.util.List;

public class MobilityHeuristic extends AbstractHeuristic implements Heuristic {

    public MobilityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        int numberOfMoves = 0;
        List<Coordinate> temp = new LinkedList<>();

        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                if (AbstractMove.isMoveValid(map, j, i, player, true, Rules.OVERRIDE_STONES, temp)) {
                    numberOfMoves++;
                }
            }
        }

        return numberOfMoves / (double) (Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles());
    }
}