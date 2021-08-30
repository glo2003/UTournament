package com.github.glo2003.utournament.infrastructure.persistence;

import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

class TournamentRepositoryInMemoryTest {
    private TournamentRepositoryInMemory repository;
    private Tournament tournament;
    private TournamentId tournamentId;

    @BeforeEach
    void setUp() {
        repository = new TournamentRepositoryInMemory();
        tournament = new Tournament(new TournamentId());
        tournamentId = tournament.getId();
    }

    @Test
    void onNonPresentTournament_returnNone() {
        Optional<Tournament> tournament = repository.get(tournamentId);
        assertThat(tournament.isPresent()).isFalse();
    }

    @Test
    void canGetBackAddedTournament() {
        repository.save(tournament);

        Optional<Tournament> gottenTournament = repository.get(tournamentId);

        assertThat(gottenTournament.isPresent()).isTrue();
        assertThat(gottenTournament.get()).isEqualTo(tournament);
    }

    @Test
    void canDeleteTournament() {
        repository.save(tournament);

        repository.remove(tournament);

        Optional<Tournament> tournament = repository.get(tournamentId);
        assertThat(tournament.isPresent()).isFalse();
    }

    @Test
    void canDeleteNonPresentTournament() {
        repository.remove(tournament);

        Optional<Tournament> tournament = repository.get(tournamentId);
        assertThat(tournament.isPresent()).isFalse();
    }
}