package de.marcluque.reversi.ai.evaluation;

import de.marcluque.reversi.ai.evaluation.heuristics.Heuristic;
import de.marcluque.reversi.map.Map;

import java.util.LinkedList;
import java.util.List;

public class Evaluation {

    private static final List<Heuristic> BUILDING_HEURISTICS = new LinkedList<>();

    private static final List<Heuristic> BOMBING_HEURISTICS = new LinkedList<>();

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

    public static double utility(Map map) {
        if (Map.getPhase() == 1) {
            return BUILDING_HEURISTICS.stream()
                    .mapToDouble(heuristic -> heuristic.executeHeuristic(map))
                    .sum();
        } else {
            return BOMBING_HEURISTICS.stream()
                    .mapToDouble(heuristic -> heuristic.executeHeuristic(map))
                    .sum();
        }
    }
}