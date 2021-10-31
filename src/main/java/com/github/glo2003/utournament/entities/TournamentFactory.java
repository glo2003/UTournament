package com.github.glo2003.utournament.entities;

import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;

import java.util.List;

public class TournamentFactory {
    private final BracketFactory bracketFactory;

    public TournamentFactory(BracketFactory bracketFactory) {
        this.bracketFactory = bracketFactory;
    }

    public Tournament createTournament(String name, List<Participant> participants) {
        Bracket bracket = bracketFactory.createBracket(participants);
        TournamentId id = new TournamentId();
        return new Tournament(id, name, participants, bracket);
    }
}
