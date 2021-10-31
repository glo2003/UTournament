package com.github.glo2003.utournament.entities.bracket.visitors;

import com.github.glo2003.utournament.entities.bracket.*;

import java.util.function.Consumer;

public class MapBracketVisitor implements BracketVisitor {
    private final Consumer<Bracket> f;

    public MapBracketVisitor(Consumer<Bracket> f) {
        this.f = f;
    }

    @Override
    public void visit(ByeBracket bracket) {
        f.accept(bracket);
    }

    @Override
    public void visit(SingleBracket bracket) {
        f.accept(bracket);
    }

    @Override
    public void visit(IntermediateBracket bracket) {
        f.accept(bracket);
        bracket.getBracketOne().accept(this);
        bracket.getBracketTwo().accept(this);
    }
}
