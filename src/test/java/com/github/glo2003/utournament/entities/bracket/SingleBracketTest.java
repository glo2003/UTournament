package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
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

@ExtendWith(MockitoExtension.class)
class SingleBracketTest {

    private static final BracketId BRACKET_ID = new BracketId();
    private static final Participant PARTICIPANT_ONE = new Participant("Juliette");
    private static final Participant WINNER = PARTICIPANT_ONE;
    private static final Participant PARTICIPANT_TWO = new Participant("Arnaud");
    private static final Participant PARTICIPANT_NOT_IN_BRACKET = new Participant("Tommy");

    SingleBracket singleBracket;

    @Mock
    BracketVisitor bracketVisitor;

    @BeforeEach
    void setUp() {
        singleBracket = new SingleBracket(BRACKET_ID, PARTICIPANT_ONE, PARTICIPANT_TWO);
    }

    @Test
    void canAcceptVisitor() {
        singleBracket.accept(bracketVisitor);

        verify(bracketVisitor).visit(singleBracket);
    }

    @Test
    void thereIsNoWinnerInitially() {
        assertThat(singleBracket.getWinner().isPresent()).isFalse();
    }

    @Test
    void participantNotInBracketThrows() {
        assertThrows(ParticipantNotInBracketException.class, () -> singleBracket.win(PARTICIPANT_NOT_IN_BRACKET));
    }

    @Test
    void canWinBracket() {
        singleBracket.win(WINNER);

        Optional<Participant> winner = singleBracket.getWinner();
        assertThat(winner.isPresent()).isTrue();
        assertThat(winner.get()).isEqualTo(WINNER);
    }

    @Test
    void cannotWinAlreadyWonBracket() {
        singleBracket.win(WINNER);

        assertThrows(BracketAlreadyPlayedException.class, () -> singleBracket.win(WINNER));
    }
}