package de.marcluque.reversi;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneCountHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.StoneParityHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.PredecessorHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.StrongestOpponentHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.bombing.SuccessorHeuristic;
import de.marcluque.reversi.ai.evaluation.heuristics.building.*;
import de.marcluque.reversi.ai.evaluation.Rules;
import de.marcluque.reversi.ai.moves.AbstractMove;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.network.Client;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.InputParser;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;

import java.util.ArrayList;
import java.util.List;

/*
 * Created with <3 by marcluque, March 2021
 */
public class ReversiAiMain {

    public static void main(String[] args) {
        if (args.length > 0) {
            InputParser.setUserChoice(true);
            InputParser.parse(args);
        }

        // BUILDING
        HeuristicEvaluation.addBuildingHeuristic(new StoneCountHeuristic(0.2));
        //HeuristicEvaluation.addBuildingHeuristic(new StoneParityHeuristic(0.1));
        HeuristicEvaluation.addBuildingHeuristic(new CornerHeuristic(0.8));
        //HeuristicEvaluation.addBuildingHeuristic(new MobilityHeuristic(0.05));
        //HeuristicEvaluation.addBuildingHeuristic(new StabilityHeuristic(0.05));

        // TODO: THESE WEIGHTS NEED TO BE DYNAMIC: We only include bomb heuristic if: bombs > 0, same for override
        //       If bombs exhibit very high bomb power (see Metrics), we want to prioritize or even focus on them

        // TODO: THESE HEURISTICS SHOULD NOT BE USED IN THE SEARCH TREE, BUT MERELY IN THE ROOT
        //HeuristicEvaluation.addBuildingHeuristic(new BonusTileHeuristic(0.2));

        // FINAL PHASE
        HeuristicEvaluation.addMaximizationHeuristic(new StoneCountHeuristic(1));

        // SORTING
        HeuristicEvaluation.setSortingHeuristic(new StoneCountHeuristic(0.4));
        HeuristicEvaluation.setSortingHeuristic(new CornerHeuristic(0.6));

        // BOMBING
        HeuristicEvaluation.addBombingHeuristic(new StoneCountHeuristic(0.15));
        HeuristicEvaluation.addBombingHeuristic(new StoneParityHeuristic(0.4));
        HeuristicEvaluation.addBombingHeuristic(new PredecessorHeuristic(0.15));
        HeuristicEvaluation.addBombingHeuristic(new StrongestOpponentHeuristic(0.15));
        HeuristicEvaluation.addBombingHeuristic(new SuccessorHeuristic(0.15));

        Rules.moveThresholdFullGameTreeSearch = 10;

        new Client("127.0.0.1", 8080).start();
    }
}