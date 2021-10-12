package com.github.glo2003.utournament.entities.bracket;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketCreationException;

import java.util.ArrayList;
import java.util.List;

public class BracketFactory {
    public Bracket createBracket(List<Participant> participants) throws BracketCreationException {
        return createBracketHelper(participants);
    }

    private Bracket createBracketHelper(List<Participant> participants) throws BracketCreationException {
        final int n = participants.size();

        if (n == 0) {
            throw new BracketCreationException("Cannot create a bracket with 0 participants");
        } else if (n == 1) {
            Participant participant = participants.get(0);
            return new ByeBracket(new BracketId(), participant);
        } else if (n == 2) {
            Participant p1 = participants.get(0);
            Participant p2 = participants.get(1);
            return new SingleBracket(new BracketId(), p1, p2);
        } else {
            final int split = (int) Math.ceil((float) n / 2.f);
            List<Participant> bracketOneParticipants = new ArrayList<>();
            List<Participant> bracketTwoParticipants = new ArrayList<>();
            for (int i = 0; i < split; i++) {
                bracketOneParticipants.add(participants.get(i));
            }
            for (int i = split; i < n; i++) {
                bracketTwoParticipants.add(participants.get(i));
            }
            Bracket bracketOne = this.createBracket(bracketOneParticipants);
            Bracket bracketTwo = this.createBracket(bracketTwoParticipants);
            return new IntermediateBracket(new BracketId(), bracketOne, bracketTwo);
        }
    }
}
