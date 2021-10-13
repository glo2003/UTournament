package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;

import java.util.Optional;

public class ByeBracket extends Bracket {
    private final Participant participant;

    public ByeBracket(BracketId id, Participant participant) {
        super(id);
        this.participant = participant;
    }

    public Participant getParticipant() {
        return participant;
    }

    @Override
    public Optional<Participant> getWinner() {
        return Optional.of(this.participant);
    }

    @Override
    public void accept(BracketVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void win(Participant participant) {
        throw new BracketAlreadyPlayedException(this);
    }
}
