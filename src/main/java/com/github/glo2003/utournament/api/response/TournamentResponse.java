package com.github.glo2003.utournament.api.response;

import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;

import java.util.List;

public class TournamentResponse {
    public String tournamentId;
    public String name;
    public List<ParticipantDto> participants;
    public BracketDto bracket;
    public List<BracketDto> playable;
}
