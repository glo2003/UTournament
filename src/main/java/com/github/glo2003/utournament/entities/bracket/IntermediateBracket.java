package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public class IntermediateBracket implements Bracket {
    private final Bracket bracketOne;
    private final Bracket bracketTwo;

    public IntermediateBracket(Bracket bracketOne, Bracket bracketTwo) {
        this.bracketOne = bracketOne;
        this.bracketTwo = bracketTwo;
    }

    @Override
    public Optional<Participant> getWinner() {
        return Optional.empty(); // TODO
    }

    @Override
    public void accept(BracketVisitor visitor) {
        visitor.visit(this);
    }

    public Bracket getBracketOne() {
        return bracketOne;
    }

    public Bracket getBracketTwo() {
        return bracketTwo;
    }
}
