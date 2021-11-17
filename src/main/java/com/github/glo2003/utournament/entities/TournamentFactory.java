package com.github.glo2003.utournament.entities;

import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.entities.exceptions.NamesNotUniqueException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TournamentFactory {
    private final BracketFactory bracketFactory;

    public TournamentFactory(BracketFactory bracketFactory) {
        this.bracketFactory = bracketFactory;
    }

    public Tournament createTournament(String name, List<Participant> participants) {
        verifyParticipantsUniques(participants);

        Bracket bracket = bracketFactory.createBracket(participants);
        TournamentId id = new TournamentId();
        return new Tournament(id, name, participants, bracket);
    }

    private void verifyParticipantsUniques(List<Participant> participants) {
        Set<String> names = new HashSet<>();
        for (Participant p : participants) {
            if (names.contains(p.getName())) {
                throw new NamesNotUniqueException();
            }
            names.add(p.getName());
        }
    }
}
