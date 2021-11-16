package com.github.glo2003.utournament.entities;


import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentFactoryTest {

    private static final String TOURNAMENT_NAME = "Smash";

    @Mock
    BracketFactory bracketFactory;

    @Mock
    List<Participant> participants;

    @Mock
    Bracket bracket;

    @InjectMocks
    TournamentFactory tournamentFactory;

    @Test
    void canCreateTournament() {
        when(bracketFactory.createBracket(participants)).thenReturn(bracket);

        Tournament tournament = tournamentFactory.createTournament(TOURNAMENT_NAME, participants);

        assertThat(tournament.getName()).isEqualTo(TOURNAMENT_NAME);
        assertThat(tournament.getBracket()).isEqualTo(bracket);
        assertThat(tournament.getParticipants()).isEqualTo(participants);
    }
}