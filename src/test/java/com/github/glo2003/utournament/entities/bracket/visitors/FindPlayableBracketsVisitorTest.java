package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindPlayableBracketsVisitorTest {

    private static final Participant WINNER = new Participant("Alexander");

    FindPlayableBracketsVisitor findPlayableBracketsVisitor;

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
        findPlayableBracketsVisitor = new FindPlayableBracketsVisitor();
    }

    @Test
    void byeBracketIsNotPlayable() {
        doCallRealMethod().when(byeBracket).accept(findPlayableBracketsVisitor);

        byeBracket.accept(findPlayableBracketsVisitor);

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).doesNotContain(byeBracket);
    }

    @Test
    void singleBracketIsPlayableWhenNoWinner() {
        doCallRealMethod().when(singleBracket).accept(findPlayableBracketsVisitor);
        when(singleBracket.getWinner()).thenReturn(Optional.empty());

        singleBracket.accept(findPlayableBracketsVisitor);

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).contains(singleBracket);
    }

    @Test
    void singleBracketIsNotPlayableWhenWon() {
        doCallRealMethod().when(singleBracket).accept(findPlayableBracketsVisitor);
        when(singleBracket.getWinner()).thenReturn(Optional.of(WINNER));

        singleBracket.accept(findPlayableBracketsVisitor);

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).doesNotContain(singleBracket);
    }

    @Test
    void intermediateBracketIsPlayableWhenNoWinnerAndChildrenPlayed() {
        doCallRealMethod().when(intermediateBracket).accept(findPlayableBracketsVisitor);
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);
        when(bracketOne.getWinner()).thenReturn(Optional.of(WINNER));
        when(bracketTwo.getWinner()).thenReturn(Optional.of(WINNER));
        when(intermediateBracket.getWinner()).thenReturn(Optional.empty());

        intermediateBracket.accept(findPlayableBracketsVisitor);

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).contains(intermediateBracket);
    }

    @Test
    void intermediateBracketIsNotPlayableWhenWon() {
        doCallRealMethod().when(intermediateBracket).accept(findPlayableBracketsVisitor);
        when(intermediateBracket.getWinner()).thenReturn(Optional.of(WINNER));

        intermediateBracket.accept(findPlayableBracketsVisitor);

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).doesNotContain(intermediateBracket);
    }

    @Test
    void visitIntermediateBracketChildrenWhenNotPlayed() {
        doCallRealMethod().when(intermediateBracket).accept(findPlayableBracketsVisitor);
        when(intermediateBracket.getBracketOne()).thenReturn(bracketOne);
        when(intermediateBracket.getBracketTwo()).thenReturn(bracketTwo);
        when(bracketOne.getWinner()).thenReturn(Optional.empty());
        when(bracketTwo.getWinner()).thenReturn(Optional.empty());
        when(intermediateBracket.getWinner()).thenReturn(Optional.empty());

        intermediateBracket.accept(findPlayableBracketsVisitor);

        verify(bracketOne).accept(findPlayableBracketsVisitor);
        verify(bracketTwo).accept(findPlayableBracketsVisitor);
    }

    @Test
    void canResetVisitor() {
        doCallRealMethod().when(singleBracket).accept(findPlayableBracketsVisitor);
        when(singleBracket.getWinner()).thenReturn(Optional.empty());
        singleBracket.accept(findPlayableBracketsVisitor);

        findPlayableBracketsVisitor.reset();

        List<Bracket> playable = findPlayableBracketsVisitor.getPlayable();
        assertThat(playable).isEmpty();
    }
}