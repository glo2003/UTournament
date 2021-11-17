package com.github.glo2003.utournament.application.dtos;

import java.util.List;
import java.util.Objects;

public class TournamentDto {
    public String tournamentId;
    public String name;
    public List<ParticipantDto> participants;
    public BracketDto bracket;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentDto that = (TournamentDto) o;
        return Objects.equals(tournamentId, that.tournamentId) && Objects.equals(name, that.name) && Objects.equals(participants, that.participants) && Objects.equals(bracket, that.bracket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentId, name, participants, bracket);
    }
}
