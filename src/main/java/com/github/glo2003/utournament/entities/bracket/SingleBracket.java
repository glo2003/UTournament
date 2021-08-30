package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public class SingleBracket implements Bracket {
    private final Participant participantOne;
    private final Participant participantTwo;
    private Participant winner;

    public SingleBracket(Participant participantOne, Participant participantTwo) {
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
    }

    @Override
    public Optional<Participant> getWinner() {
        return Optional.ofNullable(winner);
    }

    @Override
    public void accept(BracketVisitor visitor) {
        visitor.visit(this);
    }

    public Participant getParticipantOne() {
        return participantOne;
    }

    public Participant getParticipantTwo() {
        return participantTwo;
    }
}
