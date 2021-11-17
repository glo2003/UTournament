package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
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
class ByeBracketTest {

    private static final BracketId BRACKET_ID = new BracketId();
    private static final Participant PARTICIPANT = new Participant("Juliette");
    private static final Participant PARTICIPANT_NOT_IN_BRACKET = new Participant("Tommy");

    ByeBracket byeBracket;

    @Mock
    BracketVisitor bracketVisitor;

    @BeforeEach
    void setUp() {
        byeBracket = new ByeBracket(BRACKET_ID, PARTICIPANT);
    }

    @Test
    void canAcceptVisitor() {
        byeBracket.accept(bracketVisitor);

        verify(bracketVisitor).visit(byeBracket);
    }

    @Test
    void winnerIsOnlyParticipant() {
        Optional<Participant> winner = byeBracket.getWinner();

        assertThat(winner.isPresent()).isTrue();
        assertThat(winner.get()).isEqualTo(PARTICIPANT);
    }

    @Test
    void byeBracketIsAlreadyWon() {
        assertThrows(BracketAlreadyPlayedException.class, () -> byeBracket.win(PARTICIPANT_NOT_IN_BRACKET));
    }
}