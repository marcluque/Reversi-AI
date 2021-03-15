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

    private static final List<Heuristic> BUILDING_HEURISTICS = new ArrayList<>();

    private static final List<Heuristic> BOMBING_HEURISTICS = new ArrayList<>();

    private static final Heuristic SORTING_HEURISTIC = new StoneParityHeuristic(1);

    public static void addBuildingHeuristic(Heuristic heuristic) {
        BUILDING_HEURISTICS.add(heuristic);
    }

    public static void addBombingHeuristic(Heuristic heuristic) {
        BOMBING_HEURISTICS.add(heuristic);
    }

    public static void initHeuristics(Map map) {
        for (Heuristic BUILDING_HEURISTIC : BUILDING_HEURISTICS) {
            BUILDING_HEURISTIC.initHeuristic(map);
        }

        for (Heuristic heuristic : BOMBING_HEURISTICS) {
            heuristic.initHeuristic(map);
        }
    }

    public static double utility(Map map, char player) {
        double sum = 0;

        if (Map.getPhase() == 1) {
            for (Heuristic heuristic : BUILDING_HEURISTICS) {
                sum += heuristic.executeHeuristic(map, player);
            }
        } else {
            for (Heuristic heuristic : BOMBING_HEURISTICS) {
                sum += heuristic.executeHeuristic(map, player);
            }
        }

        return sum;
    }

    public static double[] utility(Map map) {
        // 0 is left empty, players are used as indices
        double[] utility = new double[Map.getNumberOfPlayers() + 1];

        if (Map.getPhase() == 1) {
            for (Character player : AbstractSearch.ACTIVE_PLAYERS) {
                for (Heuristic heuristic : BUILDING_HEURISTICS) {
                    utility[MapUtil.playerToInt(player)] += heuristic.executeHeuristic(map, player);
                }
            }
        } else {
            for (Character player : AbstractSearch.ACTIVE_PLAYERS) {
                for (Heuristic heuristic : BOMBING_HEURISTICS) {
                    utility[MapUtil.playerToInt(player)] += heuristic.executeHeuristic(map, player);
                }
            }
        }

        return utility;
    }

    public static double heuristicValue(Map map, char player) {
        return SORTING_HEURISTIC.executeHeuristic(map, player);
    }
}