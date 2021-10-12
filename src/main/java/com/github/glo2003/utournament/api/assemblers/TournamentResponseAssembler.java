package com.github.glo2003.utournament.api.assemblers;

import com.github.glo2003.utournament.api.response.TournamentResponse;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;

import java.util.List;

public class TournamentResponseAssembler {
    public TournamentResponse toDto(TournamentDto tournamentDto, List<BracketDto> playable) {
        TournamentResponse tournamentResponse = new TournamentResponse();

        tournamentResponse.tournamentId = tournamentDto.tournamentId;
        tournamentResponse.name = tournamentDto.name;
        tournamentResponse.participants = tournamentDto.participants;
        tournamentResponse.bracket = tournamentDto.bracket;
        tournamentResponse.playable = playable;

        return tournamentResponse;
    }
}
