package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.bracket.*;

import java.util.ArrayList;
import java.util.List;

public class FindPlayableBracketsVisitor implements BracketVisitor {

    private final List<Bracket> playable;

    public FindPlayableBracketsVisitor() {
        this.playable = new ArrayList<>();
    }

    public List<Bracket> getPlayable() {
        return playable;
    }

    @Override
    public void visit(ByeBracket bracket) {
        if (bracket.getWinner().isEmpty()) {
            playable.add(bracket);
        }
    }

    @Override
    public void visit(SingleBracket bracket) {
        if (bracket.getWinner().isEmpty()) {
            playable.add(bracket);
        }
    }

    @Override
    public void visit(IntermediateBracket bracket) {
        if (bracket.getWinner().isEmpty()) {
            boolean hasWinnerOne = bracket.getBracketOne().getWinner().isPresent();
            boolean hasWinnerTwo = bracket.getBracketTwo().getWinner().isPresent();

            if (hasWinnerOne && hasWinnerTwo) {
                playable.add(bracket);
            } else {
                bracket.getBracketOne().accept(this);
                bracket.getBracketTwo().accept(this);
            }
        }
    }
}
