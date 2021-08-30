package com.github.glo2003.utournament.entities.bracket;

public interface BracketVisitor {
    void visit(ByeBracket bracket);

    void visit(SingleBracket bracket);

    void visit(IntermediateBracket bracket);
}
