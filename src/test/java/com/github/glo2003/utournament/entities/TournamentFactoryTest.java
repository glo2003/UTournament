package com.github.glo2003.utournament.entities;


import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.entities.exceptions.NamesNotUniqueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TournamentFactoryTest {

    private static final String TOURNAMENT_NAME = "Smash";
    private static final Participant PARTICIPANT = new Participant("Johny");
    private static final int PARTICIPANT_NUM = 11;

    @Mock
    BracketFactory bracketFactory;

    @Mock
    Bracket bracket;

    TournamentFactory tournamentFactory;
    List<Participant> participants;

    @BeforeEach
    void setUp() {
        tournamentFactory = new TournamentFactory(bracketFactory);
        participants = ParticipantTestUtils.createParticipants(PARTICIPANT_NUM);
    }

    @Test
    void canCreateTournament() {
        when(bracketFactory.createBracket(participants)).thenReturn(bracket);

        Tournament tournament = tournamentFactory.createTournament(TOURNAMENT_NAME, participants);

        assertThat(tournament.getName()).isEqualTo(TOURNAMENT_NAME);
        assertThat(tournament.getBracket()).isEqualTo(bracket);
        assertThat(tournament.getParticipants()).isEqualTo(participants);
    }

    @Test
    void participantNamesMustBeUniques() {
        List<Participant> participantsWithDuplicate = List.of(PARTICIPANT, PARTICIPANT);

        assertThrows(NamesNotUniqueException.class,
                () -> tournamentFactory.createTournament(TOURNAMENT_NAME, participantsWithDuplicate));
    }
}