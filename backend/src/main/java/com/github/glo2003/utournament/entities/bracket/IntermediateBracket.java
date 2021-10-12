package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketNotPlayableException;
import com.github.glo2003.utournament.entities.bracket.exceptions.ParticipantNotInBracketException;

import java.util.Optional;

public class IntermediateBracket extends Bracket {
    private final Bracket bracketOne;
    private final Bracket bracketTwo;
    private Participant winner;

    public IntermediateBracket(BracketId id, Bracket bracketOne, Bracket bracketTwo) {
        super(id);
        this.bracketOne = bracketOne;
        this.bracketTwo = bracketTwo;
        this.winner = null;
    }

    public Bracket getBracketOne() {
        return bracketOne;
    }

    public Bracket getBracketTwo() {
        return bracketTwo;
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
        Optional<Participant> participantOne = bracketOne.getWinner();
        Optional<Participant> participantTwo = bracketTwo.getWinner();
        if (participantOne.isEmpty() || participantTwo.isEmpty()) {
            throw new BracketNotPlayableException(this);
        } else if (participantOne.get().equals(participant) || participantTwo.get().equals(participant)) {
            winner = participant;
        } else {
            throw new ParticipantNotInBracketException(participant);
        }
    }
}
