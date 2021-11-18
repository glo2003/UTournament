package com.github.glo2003.utournament.entities;


import com.github.glo2003.utournament.application.dtos.ParticipantDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParticipantTestUtils {
    public static List<Participant> createParticipants(int num) {
        return IntStream.range(0, num)
                .mapToObj(i -> new Participant(Integer.toString(i)))
                .collect(Collectors.toList());
    }

    public static List<ParticipantDto> createParticipantDtos(int num) {
        return IntStream.range(0, num)
                .mapToObj(i -> {
                    ParticipantDto dto = new ParticipantDto();
                    dto.name = Integer.toString(i);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public static List<ParticipantDto> createParticipantDtosWithDuplicates(int num) {
        return IntStream.range(0, num)
                .mapToObj(i -> {
                    ParticipantDto dto = new ParticipantDto();
                    dto.name = "Josh";
                    return dto;
                })
                .collect(Collectors.toList());
    }
}