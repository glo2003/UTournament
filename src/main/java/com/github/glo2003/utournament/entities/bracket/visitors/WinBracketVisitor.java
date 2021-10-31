package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.*;

public class WinBracketVisitor implements BracketVisitor {
    private final BracketId bracketId;
    private final Participant winner;
    private boolean hasWon;

    public WinBracketVisitor(BracketId bracketId, Participant winner) {
        this.bracketId = bracketId;
        this.winner = winner;
        this.hasWon = false;
    }

    private void winBracket(Bracket bracket) {
        if (bracket.getBracketId().equals(bracketId)) {
            bracket.win(winner);
            hasWon = true;
        }
    }

    public boolean getHasWon() {
        return hasWon;
    }

    @Override
    public void visit(ByeBracket bracket) {
        winBracket(bracket);
    }

    @Override
    public void visit(SingleBracket bracket) {
        winBracket(bracket);
    }

    @Override
    public void visit(IntermediateBracket bracket) {
        winBracket(bracket);
        bracket.getBracketOne().accept(this);
        bracket.getBracketTwo().accept(this);
    }
}
