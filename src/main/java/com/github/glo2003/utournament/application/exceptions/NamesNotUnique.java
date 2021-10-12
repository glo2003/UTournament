package com.github.glo2003.utournament.application.exceptions;

public class NamesNotUnique extends RuntimeException {
    public NamesNotUnique() {
        super("Provided names are not unique");
    }
}
