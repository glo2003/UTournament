package com.github.glo2003.utournament.application.exceptions;

public class NamesNotUniqueException extends RuntimeException {
    public NamesNotUniqueException() {
        super("Provided names are not unique");
    }
}
