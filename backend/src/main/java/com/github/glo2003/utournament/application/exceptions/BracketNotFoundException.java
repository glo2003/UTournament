package com.github.glo2003.utournament.application.exceptions;

import com.github.glo2003.utournament.entities.bracket.BracketId;

public class BracketNotFoundException extends RuntimeException {
    public BracketNotFoundException(BracketId bracketId) {
        super("Bracket with id " + bracketId + " was not found");
    }
}
