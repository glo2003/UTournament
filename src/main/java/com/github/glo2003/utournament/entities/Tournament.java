package com.github.glo2003.utournament.entities;

import com.github.glo2003.utournament.entities.bracket.Bracket;

import java.util.List;
import java.util.Objects;

public class Tournament {
    private final TournamentId id;
    private String name;
    private List<Participant> participants;
    private Bracket bracket;

    public Tournament(TournamentId id) {
        this.id = id;
    }

    public TournamentId getId() {
        return id;
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(participants, that.participants) && Objects.equals(bracket, that.bracket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, participants, bracket);
    }
}
