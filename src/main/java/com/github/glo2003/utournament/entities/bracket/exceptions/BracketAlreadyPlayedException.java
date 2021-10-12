package com.github.glo2003.utournament.entities.bracket.exceptions;

import com.github.glo2003.utournament.entities.bracket.Bracket;

public class BracketAlreadyPlayedException extends RuntimeException {
    public BracketAlreadyPlayedException(Bracket bracket) {
        super("Bracket " + bracket.getId().toString() + " was already played");
    }
}
