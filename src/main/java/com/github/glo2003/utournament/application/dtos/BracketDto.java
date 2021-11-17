package com.github.glo2003.utournament.application.dtos;

import java.util.Objects;

public class BracketDto {
    public String bracketId;
    public ParticipantDto winner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BracketDto that = (BracketDto) o;
        return Objects.equals(bracketId, that.bracketId) && Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bracketId, winner);
    }
}
