package com.github.glo2003.utournament.entities;

import java.util.Optional;

public interface TournamentRepository {
    Optional<Tournament> get(TournamentId id);

    void save(Tournament tournament);

    void remove(TournamentId tournamentId);
}
