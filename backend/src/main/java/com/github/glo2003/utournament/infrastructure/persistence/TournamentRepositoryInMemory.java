package com.github.glo2003.utournament.infrastructure.persistence;

import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.TournamentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TournamentRepositoryInMemory implements TournamentRepository {

    private final Map<TournamentId, Tournament> database;

    public TournamentRepositoryInMemory() {
        database = new HashMap<>();
    }

    @Override
    public Optional<Tournament> get(TournamentId id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void save(Tournament tournament) {
        TournamentId tournamentId = tournament.getId();
        database.put(tournamentId, tournament);
    }

    @Override
    public void remove(TournamentId tournamentId) {
        database.remove(tournamentId);
    }
}
