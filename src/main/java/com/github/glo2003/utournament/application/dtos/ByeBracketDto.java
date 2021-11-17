package com.github.glo2003.utournament.application.dtos;

import java.util.Objects;

public class ByeBracketDto extends BracketDto {
    public ParticipantDto participant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ByeBracketDto that = (ByeBracketDto) o;
        return Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participant);
    }
}
