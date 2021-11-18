package com.github.glo2003.utournament.application;

import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.SingleBracketDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.application.exceptions.TournamentNotFoundException;
import com.github.glo2003.utournament.entities.*;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.entities.bracket.visitors.FindPlayableBracketsVisitor;
import com.github.glo2003.utournament.entities.bracket.visitors.WinBracketVisitor;
import com.github.glo2003.utournament.infrastructure.persistence.InMemoryTournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TournamentServiceIntegratedTest {

    private static final String TOURNAMENT_NAME = "Smash";
    private static final int NUM_PARTICIPANTS = 16;

    TournamentService tournamentService;
    List<Participant> participants;
    List<ParticipantDto> participantDtos;

    @BeforeEach
    void setUp() {
        BracketFactory bracketFactory = new BracketFactory();
        TournamentFactory tournamentFactory = new TournamentFactory(bracketFactory);
        TournamentRepository tournamentRepository = new InMemoryTournamentRepository();
        FindPlayableBracketsVisitor findPlayableBracketsVisitor = new FindPlayableBracketsVisitor();
        WinBracketVisitor winBracketVisitor = new WinBracketVisitor();
        tournamentService = new TournamentService(tournamentFactory,
                tournamentRepository,
                findPlayableBracketsVisitor,
                winBracketVisitor);
        participants = ParticipantTestUtils.createParticipants(NUM_PARTICIPANTS);
        participantDtos = ParticipantTestUtils.createParticipantDtos(NUM_PARTICIPANTS);
    }

    @Test
    void canGetCreatedTournament() {
        TournamentId tournamentId = tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);
        String tournamentIdString = tournamentId.toString();

        TournamentDto tournamentDto = tournamentService.getTournament(tournamentIdString);

        assertThat(tournamentDto.tournamentId).isEqualTo(tournamentIdString);
    }

    @Test
    void canDeleteTournament() {
        TournamentId tournamentId = tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);
        String tournamentIdString = tournamentId.toString();

        tournamentService.deleteTournament(tournamentIdString);

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.getTournament(tournamentIdString));
    }

    @Test
    void canGetPlayableBrackets() {
        TournamentId tournamentId = tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);
        String tournamentIdString = tournamentId.toString();

        List<BracketDto> playableBrackets = tournamentService.getPlayableBrackets(tournamentIdString);

        assertThat(playableBrackets).isNotEmpty();
    }

    @Test
    void canWinABracket() {
        TournamentId tournamentId = tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);
        String tournamentIdString = tournamentId.toString();
        List<BracketDto> playableBrackets = tournamentService.getPlayableBrackets(tournamentIdString);
        SingleBracketDto bracketToWin = (SingleBracketDto) playableBrackets.get(0);
        ParticipantDto winnerDto = bracketToWin.participantOne;

        tournamentService.winBracket(tournamentIdString, bracketToWin.bracketId, winnerDto);

        List<BracketDto> playableBracketsAfterWin = tournamentService.getPlayableBrackets(tournamentIdString);
        assertThat(playableBracketsAfterWin).doesNotContain(bracketToWin);
    }
}