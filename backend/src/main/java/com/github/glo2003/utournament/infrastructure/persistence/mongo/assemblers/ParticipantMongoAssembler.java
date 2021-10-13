package com.github.glo2003.utournament.infrastructure.persistence.mongo.assemblers;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos.ParticipantMongoDto;

public class ParticipantMongoAssembler {
    public ParticipantMongoDto toDto(Participant participant) {
        ParticipantMongoDto dto = new ParticipantMongoDto();

        dto.name = participant.getName();

        return dto;
    }

    public Participant fromDto(ParticipantMongoDto dto) {
        return new Participant(dto.name);
    }
}
