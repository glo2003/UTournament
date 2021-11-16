package com.github.glo2003.utournament.entities.bracket.visitors;


import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WinBracketVisitorTest {

    private static BracketId BRACKET_ID = new BracketId();
    private static BracketId BRACKET_ID_OTHER = new BracketId();
    private static Participant WINNER = new Participant("Alexander");

    WinBracketVisitor winBracketVisitor;

    @Mock
    ByeBracket byeBracket;

    @Mock
    SingleBracket singleBracket;

    @Mock
    IntermediateBracket intermediateBracket;

    @Mock
    Bracket bracketOne;

    @Mock
    Bracket bracketTwo;

    @BeforeEach
    void setUp(){
        winBracketVisitor = new WinBracketVisitor(BRACKET_ID, WINNER);
    }

    @Test
    void callWinsOnByeBracketWithBracketId() {
        callsWinOnBracketWithWrongBracketId(byeBracket);
    }

    @Test
    void callWinsOnSingleBracketWithBracketId() {
        callsWinOnBracketWithWrongBracketId(singleBracket);
    }

    @Test
    void callWinsOnIntermediateBracketWithBracketId() {
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);

        callsWinOnBracketWithWrongBracketId(intermediateBracket);
    }

    @Test
    void visitIntermediateBracketChildrenWithBracketId() {
        when(intermediateBracket.getBracketId()).thenReturn(BRACKET_ID_OTHER);
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);
        doCallRealMethod().when(intermediateBracket).accept(winBracketVisitor);

        intermediateBracket.accept(winBracketVisitor);

        verify(bracketOne).accept(winBracketVisitor);
        verify(bracketTwo).accept(winBracketVisitor);
    }

    @Test
    void doNotCallWinsOnByeBracketWithWrongBracketId() {
        doNotCallsWinOnBracketWithWrongId(byeBracket);
    }

    @Test
    void doNotCallWinsOnSingleBracketWithWrongBracketId() {
        doNotCallsWinOnBracketWithWrongId(singleBracket);
    }

    @Test
    void doNotCallWinsOnIntermediateBracketWithWrongBracketId() {
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);

        doNotCallsWinOnBracketWithWrongId(intermediateBracket);
    }

    @Test
    void visitIntermediateBracketChildrenWithWrongBracketId() {
        when(intermediateBracket.getBracketId()).thenReturn(BRACKET_ID_OTHER);
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);
        doCallRealMethod().when(intermediateBracket).accept(winBracketVisitor);

        intermediateBracket.accept(winBracketVisitor);

        verify(bracketOne).accept(winBracketVisitor);
        verify(bracketTwo).accept(winBracketVisitor);
    }

    @Test
    void hasWonIsTrueWhenWon() {
        when(byeBracket.getBracketId()).thenReturn(BRACKET_ID);
        doCallRealMethod().when(byeBracket).accept(winBracketVisitor);

        byeBracket.accept(winBracketVisitor);

        assertThat(winBracketVisitor.hasWon()).isTrue();
    }

    @Test
    void hasWonIsFalseWhenNotWon() {
        when(byeBracket.getBracketId()).thenReturn(BRACKET_ID_OTHER);
        doCallRealMethod().when(byeBracket).accept(winBracketVisitor);

        byeBracket.accept(winBracketVisitor);

        assertThat(winBracketVisitor.hasWon()).isFalse();
    }

    void callsWinOnBracketWithWrongBracketId(Bracket bracket) {
        when(bracket.getBracketId()).thenReturn(BRACKET_ID);
        doCallRealMethod().when(bracket).accept(winBracketVisitor);

        bracket.accept(winBracketVisitor);

        verify(bracket).win(WINNER);
    }

    void doNotCallsWinOnBracketWithWrongId(Bracket bracket) {
        when(bracket.getBracketId()).thenReturn(BRACKET_ID_OTHER);
        doCallRealMethod().when(bracket).accept(winBracketVisitor);

        bracket.accept(winBracketVisitor);

        verify(bracket, never()).win(WINNER);
    }
}