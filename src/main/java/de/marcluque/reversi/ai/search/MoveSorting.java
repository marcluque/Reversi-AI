package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.SortNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MoveSorting {

    public static List<SortNode> sortMoves(Map map, char player) {
        List<SortNode> moves = new ArrayList<>();

        // We first add the elements, taking n steps and then sort with n * log(n) steps in the worst-case
        MapUtil.iterateMap((x, y) -> {
            List<Coordinate> capturableStones = new ArrayList<>();
            if (AbstractMove.isMoveValid(map, x, y, player, false, capturableStones)) {
                Map mapClone = new Map(map);
                moves.add(new SortNode(AbstractMove.executeMove(mapClone, x, y, 0, player, capturableStones), mapClone));
            }
        });

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