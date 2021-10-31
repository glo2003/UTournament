package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.exceptions.InvalidTournamentIdException;

import java.util.Objects;
import java.util.UUID;

public class BracketId {
    private final UUID id;

    public BracketId() {
        id = UUID.randomUUID();
    }

    private BracketId(UUID id) {
        this.id = id;
    }

    public static BracketId fromString(String id) {
        try {
            return new BracketId(UUID.fromString(id));
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
        BracketId bracketId = (BracketId) o;
        return Objects.equals(id, bracketId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
