package com.github.glo2003.utournament.entities.bracket.exceptions;

public class InvalidBracketIdException extends RuntimeException {
    public InvalidBracketIdException(String id) {
        super(id + " is not a valid bracket id");
    }
}
