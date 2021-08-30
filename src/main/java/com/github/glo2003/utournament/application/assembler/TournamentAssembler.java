package com.github.glo2003.utournament.application.assembler;

import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.entities.Tournament;

import java.util.stream.Collectors;

public class TournamentAssembler {

    private final ParticipantAssembler participantAssembler;
    private final BracketAssembler bracketAssembler;

    public TournamentAssembler() {
        participantAssembler = new ParticipantAssembler();
        bracketAssembler = new BracketAssembler();
    }

    public TournamentDto toDto(Tournament tournament) {
        TournamentDto dto = new TournamentDto();

        dto.tournamentId = tournament.getId().toString();
        dto.name = tournament.getName();
        dto.participants = tournament.getParticipants().stream()
                .map(participantAssembler::toDto)
                .collect(Collectors.toList());
        dto.bracket = bracketAssembler.toDto(tournament.getBracket());

        return dto;
    }
}
