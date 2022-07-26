package evaluation.heuristics;

import de.marcluque.reversi.ai.evaluation.heuristics.StoneParityHeuristic;
import de.marcluque.reversi.map.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/*
 * Created with <3 by marcluque, March 2021
 */
public class StoneParityTest {

    private static Stream<Arguments> stoneParity() {
        // Arguments follow the structure: (number-of-stones, player, expected-heuristic-value)
        return Stream.of(
                Arguments.of(new int[]{-1, 100, 0, 0, 0}, '1', 1.0),
                Arguments.of(new int[]{-1, 25, 25, 25, 25}, '1', 0.0),
                Arguments.of(new int[]{-1, 0, 50, 25, 25}, '1', 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("stoneParity")
    void testStoneParityHeuristic(int[] numberOfStones, char player, double expectedHeuristicValue) {
        Map map = new Map(new char[][]{}, new int[]{}, numberOfStones, new int[]{});
        var stoneParityHeuristic = new StoneParityHeuristic(1);
        var actualHeuristicValue = stoneParityHeuristic.executeHeuristic(map, player);
        Assertions.assertEquals(expectedHeuristicValue, actualHeuristicValue);
    }
}