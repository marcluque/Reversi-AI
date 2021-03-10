package de.datasecs.reversi.ai.evaluation.rules;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    public enum StdRules {
        OVERRIDE_STONES(0);

        private final int index;

        StdRules(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    // 0 = allowOverrideStones
    private static List<Rule> activeRules = new ArrayList<>();

    public static void updateRules() {
        activeRules.forEach(Rule::updateRule);
    }

    public static boolean getRule(int index) {
        return activeRules.get(0).getResult();
    }
}
