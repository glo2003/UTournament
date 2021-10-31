package com.github.glo2003.utournament.entities.bracket.exceptions;

import com.github.glo2003.utournament.entities.bracket.Bracket;

public class BracketNotPlayableException extends RuntimeException {
    public BracketNotPlayableException(Bracket bracket) {
        super("Bracket " + bracket.getBracketId().toString() + " is not playable");
    }
}
