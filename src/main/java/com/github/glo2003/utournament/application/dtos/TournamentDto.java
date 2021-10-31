package com.github.glo2003.utournament.application.dtos;

import java.util.List;

public class TournamentDto {
    public String tournamentId;
    public String name;
    public List<ParticipantDto> participants;
    public BracketDto bracket;
}
