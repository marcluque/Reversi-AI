package de.datasecs.reversi.ai.evaluation.rules;

public interface Rule {

    boolean getResult();

    boolean updateRule();
}
