package de.marcluque.reversi.ai.search;

import de.marcluque.reversi.ai.moves.Move;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.SortNode;

import java.util.*;

/*
 * Created with <3 by marcluque, March 2021
 */
public class MoveSorting {

    public static List<SortNode> sortMoves(Map map, char player) {
        List<SortNode> moves = new ArrayList<>();

        // We first add the elements, taking n steps and then sort with n * log(n) steps in the worst-case
        for (int y = 0, height = Map.getMapHeight(); y < height; y++) {
            for (int x = 0, width = Map.getMapWidth(); x < width; x++) {
                Set<Coordinate> capturableStones = new HashSet<>();
                if (Move.isMoveValid(map, x, y, player, false, capturableStones)) {
                    Map mapClone = new Map(map);
                    moves.add(new SortNode(Move.executeMove(mapClone, x, y, 0, player, capturableStones), mapClone));
                }
            }
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