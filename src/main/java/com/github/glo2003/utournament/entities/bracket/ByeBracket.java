package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public class ByeBracket implements Bracket {

    private final Participant participant;

    public ByeBracket(Participant participant) {
        this.participant = participant;
    }

    @Override
    public Optional<Participant> getWinner() {
        return Optional.of(this.participant);
    }

    @Override
    public void accept(BracketVisitor visitor) {
        visitor.visit(this);
    }

    public Participant getParticipant() {
        return participant;
    }
}
