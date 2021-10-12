package com.github.glo2003.utournament.application.exceptions;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(String tournamentId) {
        super("Tournament with id " + tournamentId + " was not found");
    }
}
