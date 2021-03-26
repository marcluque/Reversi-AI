package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;
import de.marcluque.reversi.util.SortNode;

import java.util.*;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MoveSorting {

    public static List<SortNode> sortForMax(Map map) {
        return sortMoves(map, AbstractSearch.MAX);
    }

    public static List<SortNode> sortForMin(Map map) {
        return sortMoves(map, AbstractSearch.MIN);
    }

    public static List<SortNode> sortMoves(Map map, char player) {
        var availableMoveMap = MapUtil.getAvailableMoves(map, player);
        var moves = new ArrayList<>(availableMoveMap.keySet());

        for (SortNode move : moves) {
            Map mapClone = new Map(map);
            Move tempMove = AbstractMove.executeMove(mapClone, move.getX(), move.getY(), player, availableMoveMap.get(move));

            // We always evaluate for MAX since MIN tries to play the worst possible move for MAX
            move.setHeuristicValue(HeuristicEvaluation.heuristicValue(mapClone));
            move.setMap(mapClone);
            move.setSpecialTile(tempMove.getSpecialTile());
        }

        if (player == AbstractSearch.MAX) {
            // Descending order (maximum first)
            moves.sort(Comparator.comparingDouble(SortNode::getHeuristicValue).reversed());
        } else {
            // Ascending order (minimum first)
            moves.sort(Comparator.comparingDouble(SortNode::getHeuristicValue));
        }

        return moves;
    }
}