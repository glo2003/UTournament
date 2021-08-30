package com.github.glo2003.utournament.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class TournamentId {
    public static AtomicInteger idGenerator = new AtomicInteger();

    private int id;

    public TournamentId() {
        id = idGenerator.getAndIncrement();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
