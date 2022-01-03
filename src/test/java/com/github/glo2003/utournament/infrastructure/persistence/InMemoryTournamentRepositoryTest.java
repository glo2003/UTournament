package com.github.glo2003.utournament.infrastructure.persistence;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.bracket.BracketId;
import com.github.glo2003.utournament.entities.bracket.ByeBracket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

class InMemoryTournamentRepositoryTest {
    private InMemoryTournamentRepository repository;
    private Tournament tournament;
    private TournamentId tournamentId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTournamentRepository();
        List<Participant> participants = List.of();
        String tournamentName = "smash";
        tournament = new Tournament(new TournamentId(),
                tournamentName,
                participants,
                new ByeBracket(new BracketId(), new Participant("Alice")));
        tournamentId = tournament.getTournamentId();
    }

    @Test
    void returnTournamentWhenInRepository() {
        repository.save(tournament);

        Optional<Tournament> gottenTournament = repository.get(tournamentId);

        assertThat(gottenTournament.isPresent()).isTrue();
        assertThat(gottenTournament.get()).isEqualTo(tournament);
    }

    @Test
    void returnEmptyWhenTournamentNotInRepository() {
        Optional<Tournament> tournament = repository.get(tournamentId);

        assertThat(tournament.isEmpty()).isTrue();
    }

    @Test
    void canDeleteTournamentFromRepository() {
        repository.save(tournament);

        repository.remove(tournament.getTournamentId());

        Optional<Tournament> tournament = repository.get(tournamentId);
        assertThat(tournament.isPresent()).isFalse();
    }

    @Test
    void canDeleteTournamentNotInRepositoryWithoutError() {
        repository.remove(tournament.getTournamentId());

        Optional<Tournament> tournament = repository.get(tournamentId);
        assertThat(tournament.isPresent()).isFalse();
    }
}