package com.github.glo2003.utournament.entities;

import com.github.glo2003.utournament.entities.exceptions.InvalidTournamentIdException;

import java.util.Objects;
import java.util.UUID;

public class TournamentId {
    private final UUID id;

    public TournamentId() {
        id = UUID.randomUUID();
    }

    private TournamentId(UUID id) {
        this.id = id;
    }

    public static TournamentId fromString(String id) {
        try {
            return new TournamentId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new InvalidTournamentIdException(id);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentId that = (TournamentId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
