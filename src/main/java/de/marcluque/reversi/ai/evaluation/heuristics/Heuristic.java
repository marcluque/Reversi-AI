package de.marcluque.reversi.ai.evaluation.heuristics;

import de.marcluque.reversi.map.Map;

public interface Heuristic {
    
    void initHeuristic(Map map);

    double executeHeuristic(Map map);
}