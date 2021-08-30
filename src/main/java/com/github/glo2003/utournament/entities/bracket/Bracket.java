package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;

import java.util.Optional;

public interface Bracket {
    Optional<Participant> getWinner();

    void accept(BracketVisitor visitor);
}
