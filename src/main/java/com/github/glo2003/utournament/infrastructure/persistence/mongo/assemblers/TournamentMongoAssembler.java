package com.github.glo2003.utournament.infrastructure.persistence.mongo.assemblers;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos.TournamentMongoDto;

import java.util.List;
import java.util.stream.Collectors;

public class TournamentMongoAssembler {
    private final ParticipantMongoAssembler participantMongoAssembler;
    private final BracketMongoAssembler bracketMongoAssembler;

    public TournamentMongoAssembler() {
        participantMongoAssembler = new ParticipantMongoAssembler();
        bracketMongoAssembler = new BracketMongoAssembler();
    }

    public Tournament fromDto(TournamentMongoDto dto) {
        TournamentId id = TournamentId.fromString(dto.tournamentId);
        List<Participant> participants = dto.participants.stream()
                .map(participantMongoAssembler::fromDto)
                .collect(Collectors.toList());
        Bracket bracket = bracketMongoAssembler.fromDto(dto.bracket);
        return new Tournament(id, dto.name, participants, bracket);
    }

    public TournamentMongoDto toDto(Tournament tournament) {
        TournamentMongoDto dto = new TournamentMongoDto();

        dto.tournamentId = tournament.getTournamentId().toString();
        dto.name = tournament.getName();
        dto.participants = tournament.getParticipants().stream()
                .map(participantMongoAssembler::toDto)
                .collect(Collectors.toList());
        dto.bracket = bracketMongoAssembler.toDto(tournament.getBracket());

        return dto;
    }
}
