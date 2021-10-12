package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BracketFactoryTest {

    private BracketFactory bracketFactory;

    private List<Participant> createParticipants(int num) {
        return IntStream.range(0, num)
                .mapToObj(i -> new Participant(Integer.toString(i)))
                .collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        bracketFactory = new BracketFactory();
    }

    @Test()
    void onZeroParticipant_createBracketShouldThrow() {
        List<Participant> participants = createParticipants(0);

        assertThrows(BracketCreationException.class, () -> {
            bracketFactory.createBracket(participants);
        });
    }

    @Test
    void onOneParticipant_createByeBracket() {
        List<Participant> participants = createParticipants(1);

        Bracket bracket = bracketFactory.createBracket(participants);

        assertThat(bracket).isInstanceOf(ByeBracket.class);
        ByeBracket byeBracket = (ByeBracket) bracket;
        assertThat(byeBracket.getParticipant()).isEqualTo(participants.get(0));
    }

    @Test
    void onTwoParticipants_createSingleBracket() {
        List<Participant> participants = createParticipants(2);

        Bracket bracket = bracketFactory.createBracket(participants);

        assertThat(bracket).isInstanceOf(SingleBracket.class);
        SingleBracket singleBracket = (SingleBracket) bracket;
        assertThat(singleBracket.getParticipantOne()).isEqualTo(participants.get(0));
        assertThat(singleBracket.getParticipantTwo()).isEqualTo(participants.get(1));
    }

    @Test
    void onMoreThanTwoParticipants_createIntermediateBracket() {
        List<Participant> participants = createParticipants(11);

        Bracket bracket = bracketFactory.createBracket(participants);

        assertThat(bracket).isInstanceOf(IntermediateBracket.class);
    }

    @Test
    void createdBracketShouldContainEveryParticipant() {
        List<Participant> participants = createParticipants(11);

        IntermediateBracket bracket = (IntermediateBracket) bracketFactory.createBracket(participants);

        boolean allPresent = participants.stream().allMatch(participant -> isInBracket(participant, bracket));
        assertThat(allPresent).isTrue();
    }

    boolean isInBracket(Participant participant, Bracket bracket) throws RuntimeException {
        if (bracket instanceof IntermediateBracket) {
            IntermediateBracket intermediateBracket = (IntermediateBracket) bracket;
            return isInBracket(participant, intermediateBracket.getBracketOne()) || isInBracket(participant, intermediateBracket.getBracketTwo());
        } else if (bracket instanceof SingleBracket) {
            SingleBracket singleBracket = (SingleBracket) bracket;
            return singleBracket.getParticipantOne().equals(participant) || singleBracket.getParticipantTwo().equals(participant);
        } else if (bracket instanceof ByeBracket) {
            ByeBracket byeBracket = (ByeBracket) bracket;
            return byeBracket.getParticipant().equals(participant);
        } else {
            throw new RuntimeException("Invalid bracket class");
        }
    }

    @Test
    void onPowerOfTwoNumberOfParticipants_thenCreateTreeOfSingleBrackets() {
        List<Participant> participants = createParticipants(8);

        IntermediateBracket bracket = (IntermediateBracket) bracketFactory.createBracket(participants);

        assertThat(areAllBracketsSingle(bracket)).isTrue();
    }

    boolean areAllBracketsSingle(Bracket bracket) {
        if (bracket instanceof IntermediateBracket) {
            IntermediateBracket intermediateBracket = (IntermediateBracket) bracket;
            return areAllBracketsSingle(intermediateBracket.getBracketOne()) && areAllBracketsSingle(intermediateBracket.getBracketTwo());
        } else {
            return bracket instanceof SingleBracket;
        }
    }

    @Test
    void onNonPowerOfTwoNumberOfParticipants_thenCreateAtLeastOneByeBracket() {
        List<Participant> participants = createParticipants(9);

        IntermediateBracket bracket = (IntermediateBracket) bracketFactory.createBracket(participants);

        assertThat(existAByeBracket(bracket)).isTrue();
    }

    private boolean existAByeBracket(Bracket bracket) {
        if (bracket instanceof IntermediateBracket) {
            IntermediateBracket intermediateBracket = (IntermediateBracket) bracket;
            return existAByeBracket(intermediateBracket.getBracketOne()) || existAByeBracket(intermediateBracket.getBracketTwo());
        } else {
            return bracket instanceof ByeBracket;
        }
    }
}