package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public abstract class Bracket {
    private final BracketId id;

    public Bracket(BracketId id) {
        this.id = id;
    }

    public BracketId getId() {
        return id;
    }

    public abstract Optional<Participant> getWinner();

    public abstract void accept(BracketVisitor visitor);

    public abstract void win(Participant participant);
}
