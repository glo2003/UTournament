package com.github.glo2003.utournament.entities.bracket;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BracketId {
    public static AtomicInteger idGenerator = new AtomicInteger();

    private int id;

    public BracketId() {
        id = idGenerator.getAndIncrement();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BracketId that = (BracketId) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
