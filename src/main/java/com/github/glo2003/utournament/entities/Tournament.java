package com.github.glo2003.utournament.entities;

import com.github.glo2003.utournament.entities.bracket.Bracket;

import java.util.List;
import java.util.Objects;

public class Tournament {
    private final TournamentId tournamentId;
    private final String name;
    private final List<Participant> participants;
    private final Bracket bracket;

    public Tournament(TournamentId tournamentId, String name, List<Participant> participants, Bracket bracket) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.participants = participants;
        this.bracket = bracket;
    }

    public TournamentId getTournamentId() {
        return tournamentId;
    }

    public String getName() {
        return name;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public Bracket getBracket() {
        return bracket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(tournamentId, that.tournamentId) && Objects.equals(name, that.name) && Objects.equals(participants, that.participants) && Objects.equals(bracket, that.bracket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, name, participants, bracket);
    }
}
