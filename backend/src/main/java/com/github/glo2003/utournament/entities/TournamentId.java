package com.github.glo2003.utournament.entities;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TournamentId {
    public static AtomicInteger idGenerator = new AtomicInteger();

    private final int id;

    public TournamentId() {
        id = idGenerator.getAndIncrement();
    }

    public TournamentId(int id) {
        this.id = id;
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
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
