package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.BracketId;

public class WinBracketVisitor extends MapBracketVisitor {
    public WinBracketVisitor(BracketId id, Participant winner) {
        super((bracket -> {
            if (bracket.getId().equals(id)) {
                bracket.win(winner);
            }
        }));
    }
}
