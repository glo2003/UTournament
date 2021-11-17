package com.github.glo2003.utournament.application.dtos;

import java.util.Objects;

public class IntermediateBracketDto extends BracketDto {
    public BracketDto bracketOne;
    public BracketDto bracketTwo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IntermediateBracketDto that = (IntermediateBracketDto) o;
        return Objects.equals(bracketOne, that.bracketOne) && Objects.equals(bracketTwo, that.bracketTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bracketOne, bracketTwo);
    }
}
