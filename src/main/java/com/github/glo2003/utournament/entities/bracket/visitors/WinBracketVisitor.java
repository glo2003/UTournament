package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.*;
import com.github.glo2003.utournament.entities.bracket.exceptions.WinBracketVisitorNotStartedException;

public class WinBracketVisitor implements BracketVisitor {
    private BracketId bracketId;
    private Participant winner;
    private boolean hasWon;

    public WinBracketVisitor() {
        bracketId = null;
        winner = null;
        hasWon = false;
    }

    public void start(BracketId bracketId, Participant winner) {
        this.bracketId = bracketId;
        this.winner = winner;
        this.hasWon = false;
    }

    public void reset() {
        this.bracketId = null;
        this.winner = null;
        this.hasWon = false;
    }

    public boolean hasWon() {
        return hasWon;
    }

    @Override
    public void visit(ByeBracket bracket) {
        validateWasStarted();
        winBracket(bracket);
    }

    @Override
    public void visit(SingleBracket bracket) {
        validateWasStarted();
        winBracket(bracket);
    }

    @Override
    public void visit(IntermediateBracket bracket) {
        validateWasStarted();
        winBracket(bracket);
        bracket.getBracketOne().accept(this);
        bracket.getBracketTwo().accept(this);
    }

    private void validateWasStarted() {
        if (bracketId == null || winner == null) {
            throw new WinBracketVisitorNotStartedException();
        }
    }

    private void winBracket(Bracket bracket) {
        if (bracket.getBracketId().equals(bracketId)) {
            bracket.win(winner);
            hasWon = true;
        }
    }
}
