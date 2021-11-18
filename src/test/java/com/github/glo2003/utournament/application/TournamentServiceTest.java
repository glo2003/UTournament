package com.github.glo2003.utournament.application;

import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ByeBracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.application.exceptions.BracketNotFoundException;
import com.github.glo2003.utournament.application.exceptions.TournamentNotFoundException;
import com.github.glo2003.utournament.entities.*;
import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketId;
import com.github.glo2003.utournament.entities.bracket.ByeBracket;
import com.github.glo2003.utournament.entities.bracket.exceptions.InvalidBracketIdException;
import com.github.glo2003.utournament.entities.bracket.visitors.FindPlayableBracketsVisitor;
import com.github.glo2003.utournament.entities.bracket.visitors.WinBracketVisitor;
import com.github.glo2003.utournament.entities.exceptions.InvalidTournamentIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.glo2003.utournament.entities.bracket.BracketTestUtils.createBrackets;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    private static final String TOURNAMENT_NAME = "Smash";
    private static final int NUM_PARTICIPANT = 10;
    private static final TournamentId TOURNAMENT_ID = new TournamentId();
    private static final String TOURNAMENT_ID_STRING = TOURNAMENT_ID.toString();
    private static final int NUM_BRACKETS = 5;
    private static final ParticipantDto WINNER_DTO = new ParticipantDto();
    private static final Participant PARTICIPANT = new Participant("John");
    private static final String INVALID_ID_STRING = "invalid-id";

    TournamentService tournamentService;

    @Mock
    TournamentFactory tournamentFactory;

    @Mock
    TournamentRepository tournamentRepository;

    @Mock
    FindPlayableBracketsVisitor findPlayableBracketsVisitor;

    @Mock
    WinBracketVisitor winBracketVisitor;

    Tournament tournament;
    TournamentDto tournamentDto;
    List<Participant> participants;
    List<ParticipantDto> participantDtos;

    @BeforeEach
    void setUp() {
        participants = ParticipantTestUtils.createParticipants(NUM_PARTICIPANT);
        participantDtos = ParticipantTestUtils.createParticipantDtos(NUM_PARTICIPANT);

        BracketId id = new BracketId();
        tournament = new Tournament(TOURNAMENT_ID, TOURNAMENT_NAME, participants, new ByeBracket(id, participants.get(0)));

        tournamentDto = new TournamentDto();
        tournamentDto.name = TOURNAMENT_NAME;
        tournamentDto.tournamentId = TOURNAMENT_ID_STRING;
        tournamentDto.participants = participantDtos;

        ByeBracketDto bracketDto = new ByeBracketDto();
        bracketDto.bracketId = id.toString();
        bracketDto.winner = participantDtos.get(0);
        bracketDto.participant = participantDtos.get(0);

        tournamentDto.bracket = bracketDto;

        tournamentService = new TournamentService(tournamentFactory,
                tournamentRepository,
                findPlayableBracketsVisitor,
                winBracketVisitor);
    }

    @Test
    void canCreateTournament() {
        when(tournamentFactory.createTournament(TOURNAMENT_NAME, participants)).thenReturn(tournament);

        TournamentId createdId = tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);

        assertThat(createdId).isEqualTo(TOURNAMENT_ID);
    }

    @Test
    void createdTournamentIsSaved() {
        when(tournamentFactory.createTournament(TOURNAMENT_NAME, participants)).thenReturn(tournament);

        tournamentService.createTournament(TOURNAMENT_NAME, participantDtos);

        verify(tournamentRepository).save(tournament);
    }

    @Test
    void throwsWhenTournamentNotFound() {
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.getTournament(TOURNAMENT_ID_STRING));
    }

    @Test
    void throwsWhenInvalidTournamentId() {
        assertThrows(InvalidTournamentIdException.class,
                () -> tournamentService.getTournament(INVALID_ID_STRING));
    }

    @Test
    void canGetTournamentFromId() {
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));

        TournamentDto gottenTournament = tournamentService.getTournament(TOURNAMENT_ID_STRING);

        assertThat(gottenTournament).isEqualTo(tournamentDto);
    }

    @Test
    void canGetPlayableBrackets() {
        ByeBracket bracket = mock(ByeBracket.class);
        Tournament tournament = new Tournament(
                this.tournament.getTournamentId(),
                this.tournament.getName(),
                this.tournament.getParticipants(),
                bracket);
        List<Bracket> brackets = createBrackets(NUM_BRACKETS);
        doCallRealMethod().when(bracket).accept(findPlayableBracketsVisitor);
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));
        when(findPlayableBracketsVisitor.getPlayable()).thenReturn(brackets);

        List<BracketDto> bracketDtos = tournamentService.getPlayableBrackets(TOURNAMENT_ID_STRING);

        List<String> bracketDtoIds = bracketDtos.stream().map(b -> b.bracketId).collect(Collectors.toList());
        List<String> bracketIds = brackets.stream().map(b -> b.getBracketId().toString()).collect(Collectors.toList());
        assertThat(bracketDtoIds).containsExactlyElementsIn(bracketIds);
    }

    @Test
    void canWinBracket() {
        BracketId bracketId = new BracketId();
        String bracketIdString = bracketId.toString();
        ByeBracket bracket = new ByeBracket(bracketId, PARTICIPANT);
        Tournament tournament = new Tournament(
                this.tournament.getTournamentId(),
                this.tournament.getName(),
                this.tournament.getParticipants(),
                bracket);
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));
        when(winBracketVisitor.hasWon()).thenReturn(true);

        tournamentService.winBracket(TOURNAMENT_ID_STRING, bracketIdString, WINNER_DTO);

        verify(winBracketVisitor).visit(bracket);
    }

    @Test
    void tournamentIsSavedAfterWin() {
        BracketId bracketId = new BracketId();
        String bracketIdString = bracketId.toString();
        ByeBracket bracket = mock(ByeBracket.class);
        Tournament tournament = new Tournament(
                this.tournament.getTournamentId(),
                this.tournament.getName(),
                this.tournament.getParticipants(),
                bracket);
        ParticipantDto winnerDto = new ParticipantDto();
        doCallRealMethod().when(bracket).accept(winBracketVisitor);
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));
        when(winBracketVisitor.hasWon()).thenReturn(true);

        tournamentService.winBracket(TOURNAMENT_ID_STRING, bracketIdString, winnerDto);

        verify(tournamentRepository).save(tournament);
    }

    @Test
    void throwWhenWinningBracketNotFound() {
        BracketId bracketId = new BracketId();
        String bracketIdString = bracketId.toString();
        ByeBracket bracket = mock(ByeBracket.class);
        Tournament tournament = new Tournament(
                this.tournament.getTournamentId(),
                this.tournament.getName(),
                this.tournament.getParticipants(),
                bracket);
        ParticipantDto winnerDto = new ParticipantDto();
        doCallRealMethod().when(bracket).accept(winBracketVisitor);
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));
        when(winBracketVisitor.hasWon()).thenReturn(false);

        assertThrows(BracketNotFoundException.class,
                () -> tournamentService.winBracket(TOURNAMENT_ID_STRING, bracketIdString, winnerDto));
        verify(tournamentRepository, never()).save(tournament);
    }

    @Test
    void throwsWhenInvalidBracketId() {
        when(tournamentRepository.get(TOURNAMENT_ID)).thenReturn(Optional.of(tournament));

        assertThrows(InvalidBracketIdException.class,
                () -> tournamentService.winBracket(TOURNAMENT_ID_STRING, INVALID_ID_STRING, WINNER_DTO));
    }

    @Test
    void canDeleteTournament() {
        tournamentService.deleteTournament(TOURNAMENT_ID_STRING);

        verify(tournamentRepository).remove(TOURNAMENT_ID);
    }
}