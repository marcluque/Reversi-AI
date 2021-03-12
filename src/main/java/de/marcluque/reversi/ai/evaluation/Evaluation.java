package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneParityHeuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

import java.util.*;

/*
 * Created with <3 by Marc Luqué, March 2021
 */
public class Evaluation {

    private static final List<Heuristic> BUILDING_HEURISTICS = new LinkedList<>();

    private static final List<Heuristic> BOMBING_HEURISTICS = new LinkedList<>();

    private static final Heuristic SORTING_HEURISTIC = new StoneParityHeuristic(1);

    public static void addBuildingHeuristic(Heuristic heuristic) {
        BUILDING_HEURISTICS.add(heuristic);
    }

    public static void addBombingHeuristic(Heuristic heuristic) {
        BOMBING_HEURISTICS.add(heuristic);
    }

    public static void initHeuristics(Map map) {
        BUILDING_HEURISTICS.forEach(heuristic -> heuristic.initHeuristic(map));
        BOMBING_HEURISTICS.forEach(heuristic -> heuristic.initHeuristic(map));
    }

    public static double utility(Map map, char player) {
        if (Map.getPhase() == 1) {
            return BUILDING_HEURISTICS.stream()
                    .mapToDouble(heuristic -> heuristic.executeHeuristic(map, player))
                    .sum();
        } else {
            return BOMBING_HEURISTICS.stream()
                    .mapToDouble(heuristic -> heuristic.executeHeuristic(map, player))
                    .sum();
        }
    }

    public static double[] utility(Map map) {
        // 0 is left empty, players are used as indices
        double[] utility = new double[Map.getNumberOfPlayers() + 1];

        if (Map.getPhase() == 1) {
            AbstractSearch.ACTIVE_PLAYERS.forEach(player -> {
                utility[MapUtil.playerToInt(player)] = BUILDING_HEURISTICS.stream()
                        .mapToDouble(heuristic -> heuristic.executeHeuristic(map, player))
                        .sum();
            });
        } else {
            AbstractSearch.ACTIVE_PLAYERS.forEach(player -> {
                utility[MapUtil.playerToInt(player)] = BOMBING_HEURISTICS.stream()
                        .mapToDouble(heuristic -> heuristic.executeHeuristic(map, player))
                        .sum();
            });
        }

        return utility;
    }

    public static double heuristicValue(Map map, char player) {
        return SORTING_HEURISTIC.executeHeuristic(map, player);
    }
}