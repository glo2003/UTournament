package com.github.glo2003.utournament.entities.bracket.exceptions;

public class WinBracketVisitorNotStartedException extends RuntimeException {
    public WinBracketVisitorNotStartedException() {
        super("The visitor was not started");
    }
}
