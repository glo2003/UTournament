package com.github.glo2003.utournament.entities.bracket.exceptions;

import com.github.glo2003.utournament.entities.Participant;

public class ParticipantNotInBracketException extends RuntimeException {
    public ParticipantNotInBracketException(Participant participant) {
        super("Participant " + participant.getName() + " is not competing in that bracket");
    }
}
