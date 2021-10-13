package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
import com.github.glo2003.utournament.entities.bracket.exceptions.ParticipantNotInBracketException;

import java.util.Optional;

public class SingleBracket extends Bracket {
    private final Participant participantOne;
    private final Participant participantTwo;
    private Participant winner;

    public SingleBracket(BracketId id, Participant participantOne, Participant participantTwo) {
        super(id);
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
        this.winner = null;
    }

    public SingleBracket(BracketId id, Participant participantOne, Participant participantTwo, Participant winner) {
        this(id, participantOne, participantTwo);
        this.winner = winner;
    }

    public Participant getParticipantOne() {
        return participantOne;
    }

    public Participant getParticipantTwo() {
        return participantTwo;
    }

    @Override
    public Optional<Participant> getWinner() {
        return Optional.ofNullable(winner);
    }

    @Override
    public void accept(BracketVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void win(Participant participant) {
        if (winner != null) {
            throw new BracketAlreadyPlayedException(this);
        }
        if (getParticipantOne().equals(participant) || getParticipantTwo().equals(participant)) {
            winner = participant;
        } else {
            throw new ParticipantNotInBracketException(participant);
        }
    }
}
