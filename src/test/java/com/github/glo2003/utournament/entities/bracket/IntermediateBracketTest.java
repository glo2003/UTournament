package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketNotPlayableException;
import com.github.glo2003.utournament.entities.bracket.exceptions.ParticipantNotInBracketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntermediateBracketTest {

    private static final BracketId BRACKET_ID = new BracketId();
    private static final Participant PARTICIPANT_ONE = new Participant("Juliette");
    private static final Participant WINNER = PARTICIPANT_ONE;
    private static final Participant PARTICIPANT_TWO = new Participant("Arnaud");
    private static final Participant PARTICIPANT_NOT_IN_BRACKET = new Participant("Tommy");

    IntermediateBracket intermediateBracket;

    @Mock
    Bracket bracketOne;

    @Mock
    Bracket bracketTwo;

    @Mock
    BracketVisitor bracketVisitor;

    @BeforeEach
    void setUp() {
        intermediateBracket = new IntermediateBracket(BRACKET_ID, bracketOne, bracketTwo);
    }

    @Test
    void canAcceptVisitor() {
        intermediateBracket.accept(bracketVisitor);

        verify(bracketVisitor).visit(intermediateBracket);
    }

    @Test
    void thereIsNoWinnerInitially() {
        assertThat(intermediateBracket.getWinner().isPresent()).isFalse();
    }

    @Test
    void participantNotInBracketThrows() {
        when(bracketOne.getWinner()).thenReturn(Optional.of(PARTICIPANT_ONE));
        when(bracketTwo.getWinner()).thenReturn(Optional.of(PARTICIPANT_TWO));

        assertThrows(ParticipantNotInBracketException.class, () -> intermediateBracket.win(PARTICIPANT_NOT_IN_BRACKET));
    }

    @Test
    void canWinBracket() {
        when(bracketOne.getWinner()).thenReturn(Optional.of(WINNER));
        when(bracketTwo.getWinner()).thenReturn(Optional.of(PARTICIPANT_TWO));

        intermediateBracket.win(WINNER);

        Optional<Participant> winner = intermediateBracket.getWinner();
        assertThat(winner.isPresent()).isTrue();
        assertThat(winner.get()).isEqualTo(WINNER);
    }

    @Test
    void cannotWinAlreadyWonBracket() {
        when(bracketOne.getWinner()).thenReturn(Optional.of(WINNER));
        when(bracketTwo.getWinner()).thenReturn(Optional.of(PARTICIPANT_TWO));
        intermediateBracket.win(WINNER);

        assertThrows(BracketAlreadyPlayedException.class, () -> intermediateBracket.win(WINNER));
    }

    @Test
    void cannotPlayNotPlayableBracket() {
        assertThrows(BracketNotPlayableException.class, () -> intermediateBracket.win(WINNER));
    }

    @Test
    void cannotPlayWhenMissingWinnerOfBracketTwo() {
        when(bracketOne.getWinner()).thenReturn(Optional.of(PARTICIPANT_ONE));

        assertThrows(BracketNotPlayableException.class, () -> intermediateBracket.win(WINNER));
    }

    @Test
    void cannotPlayWhenMissingWinnerOfBracketOne() {
        when(bracketTwo.getWinner()).thenReturn(Optional.of(PARTICIPANT_TWO));

        assertThrows(BracketNotPlayableException.class, () -> intermediateBracket.win(WINNER));
    }
}