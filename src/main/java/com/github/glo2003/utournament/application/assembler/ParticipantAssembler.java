package com.github.glo2003.utournament.application.assembler;

import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.entities.Participant;

public class ParticipantAssembler {
    public ParticipantDto toDto(Participant participant) {
        ParticipantDto dto = new ParticipantDto();

        dto.name = participant.getName();

        return dto;
    }
}
