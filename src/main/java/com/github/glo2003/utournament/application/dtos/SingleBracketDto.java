package com.github.glo2003.utournament.application.dtos;

import java.util.Objects;

public class SingleBracketDto extends BracketDto {
    public ParticipantDto participantOne;
    public ParticipantDto participantTwo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleBracketDto that = (SingleBracketDto) o;
        return Objects.equals(participantOne, that.participantOne) && Objects.equals(participantTwo, that.participantTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participantOne, participantTwo);
    }
}
