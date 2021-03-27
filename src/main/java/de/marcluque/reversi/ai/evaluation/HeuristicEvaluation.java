package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.util.MapUtil;

import java.util.*;

/*
 * Created with <3 by marcluque, March 2021
 */
public class HeuristicEvaluation {

    private static List<Heuristic> buildingHeuristics = new ArrayList<>();

    private static final List<Heuristic> MAXIMIZATION_HEURISTICS = new ArrayList<>();

    private static final List<Heuristic> BOMBING_HEURISTICS = new ArrayList<>();

    private static Heuristic sortingHeuristic;

    public static void setSortingHeuristic(Heuristic heuristic) {
        sortingHeuristic = heuristic;
    }

    public static void addBuildingHeuristic(Heuristic heuristic) {
        buildingHeuristics.add(heuristic);
    }

    public static void addMaximizationHeuristic(Heuristic heuristic) {
        MAXIMIZATION_HEURISTICS.add(heuristic);
    }

    public static void addBombingHeuristic(Heuristic heuristic) {
        BOMBING_HEURISTICS.add(heuristic);
    }

    public static void initHeuristics(Map map) {
        for (Heuristic BUILDING_HEURISTIC : buildingHeuristics) {
            BUILDING_HEURISTIC.initHeuristic(map);
        }

        for (Heuristic heuristic : BOMBING_HEURISTICS) {
            heuristic.initHeuristic(map);
        }
    }

    public static void activateStoneMaximization() {
        buildingHeuristics = new ArrayList<>(MAXIMIZATION_HEURISTICS);
    }

    public static double utility(Map map) {
        double sum = 0;

        if (Map.getPhase() == 1) {
            for (Heuristic heuristic : buildingHeuristics) {
                sum += heuristic.getWeight() * heuristic.executeHeuristic(map, AbstractSearch.MAX);
            }
        } else {
            for (Heuristic heuristic : BOMBING_HEURISTICS) {
                sum += heuristic.getWeight() * heuristic.executeHeuristic(map, AbstractSearch.MAX);
            }
        }

        return sum;
    }

    public static double[] nPlayerUtility(Map map) {
        // 0 is left empty, players are used as indices
        double[] utility = new double[Map.getNumberOfPlayers() + 1];

        if (Map.getPhase() == 1) {
            for (Character player : AbstractSearch.ACTIVE_PLAYERS) {
                for (Heuristic heuristic : buildingHeuristics) {
                    utility[MapUtil.playerToInt(player)] += heuristic.getWeight() * heuristic.executeHeuristic(map, player);
                }
            }
        } else {
            for (Character player : AbstractSearch.ACTIVE_PLAYERS) {
                for (Heuristic heuristic : BOMBING_HEURISTICS) {
                    utility[MapUtil.playerToInt(player)] += heuristic.getWeight() * heuristic.executeHeuristic(map, player);
                }
            }
        }

        return utility;
    }

    public static double heuristicValue(Map map) {
        return sortingHeuristic.executeHeuristic(map, AbstractSearch.MAX);
    }
}