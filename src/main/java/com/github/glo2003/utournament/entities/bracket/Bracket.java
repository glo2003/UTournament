package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public abstract class Bracket {
    private final BracketId bracketId;

    public Bracket(BracketId bracketId) {
        this.bracketId = bracketId;
    }

    public BracketId getBracketId() {
        return bracketId;
    }

    public abstract Optional<Participant> getWinner();

    public abstract void accept(BracketVisitor visitor);

    public abstract void win(Participant participant);
}
