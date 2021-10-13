package com.github.glo2003.utournament.application.exceptions;

import com.github.glo2003.utournament.entities.TournamentId;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(TournamentId tournamentId) {
        super("Tournament with id " + tournamentId + " was not found");
    }
}
