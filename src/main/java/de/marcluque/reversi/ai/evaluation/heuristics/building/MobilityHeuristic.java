package de.marcluque.reversi.ai.evaluation.heuristics.building;

import de.marcluque.reversi.ai.evaluation.heuristics.AbstractHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MobilityHeuristic extends AbstractHeuristic implements Heuristic {

    public MobilityHeuristic(double weight) {
        super.weight = weight;
    }

    @Override
    public void initHeuristic(Map map) {}

    @Override
    public double executeHeuristic(Map map, char player) {
        double numberOfMoves = 0;

        for (int i = 0; i < Map.getMapHeight(); i++) {
            for (int j = 0; j < Map.getMapWidth(); j++) {
                if (AbstractMove.isMoveValid(map, j, i, player, true, Rules.useOverrideStones)) {
                    numberOfMoves++;
                }
            }
        }

        return numberOfMoves / (Map.getMapHeight() * Map.getMapWidth() - Map.getNumberOfHoles());
    }
}