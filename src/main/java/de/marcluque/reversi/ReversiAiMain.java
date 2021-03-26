package de.marcluque.reversi;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneCountHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneParityHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.PredecessorHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.StrongestOpponentHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.SuccessorHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.CornerHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.MobilityHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.StabilityHeuristic;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.network.Client;

/*
 * Created with <3 by marcluque, March 2021
 */
public class ReversiAiMain {

    public static void main(String[] args) {
        HeuristicEvaluation.addBuildingHeuristic(new StoneCountHeuristic(0.2));
        HeuristicEvaluation.addBuildingHeuristic(new StoneParityHeuristic(0.2));
        HeuristicEvaluation.addBuildingHeuristic(new CornerHeuristic(0.2));
        HeuristicEvaluation.addBuildingHeuristic(new MobilityHeuristic(0.2));
        HeuristicEvaluation.addBuildingHeuristic(new StabilityHeuristic(0.2));

        HeuristicEvaluation.addMaximizationHeuristic(new StoneParityHeuristic(1));

        HeuristicEvaluation.setSortingHeuristic(new StoneParityHeuristic(1));

        HeuristicEvaluation.addBombingHeuristic(new PredecessorHeuristic(1d/3));
        HeuristicEvaluation.addBombingHeuristic(new StrongestOpponentHeuristic(1d/3));
        HeuristicEvaluation.addBombingHeuristic(new SuccessorHeuristic(1d/3));

        Rules.moveThresholdFullGameTreeSearch = 10;

        new Client("127.0.0.1", 8080).start();
    }
}