package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.SortNode;

import java.util.*;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class MoveSorting {

    public static List<SortNode> sort(Map map, char player, java.util.Map<SortNode, List<Coordinate>> availableMoveMap) {
        List<SortNode> moves = new ArrayList<>(availableMoveMap.keySet());

        moves.forEach(move -> {
            Map mapClone = new Map(map);
            AbstractMove.executeMove(mapClone, move.getX(), move.getY(), player, availableMoveMap.get(move));
            move.setHeuristicValue(Evaluation.heuristicValue(mapClone, player));
        });

        moves.sort(Comparator.comparingDouble(SortNode::getHeuristicValue));

        return moves;
    }
}