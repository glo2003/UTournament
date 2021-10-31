package com.github.glo2003.utournament.entities.exceptions;

public class InvalidTournamentIdException extends RuntimeException {
    public InvalidTournamentIdException(String id) {
        super(id + " is not a valid tournament id");
    }
}
