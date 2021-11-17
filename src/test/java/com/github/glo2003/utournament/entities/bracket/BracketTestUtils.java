package com.github.glo2003.utournament.entities.bracket;


import java.util.List;
import java.util.stream.Collectors;

import static com.github.glo2003.utournament.entities.ParticipantTestUtils.createParticipants;

public class BracketTestUtils {
    public static List<Bracket> createBrackets(int num) {
        return createParticipants(num).stream()
                .map(p -> new ByeBracket(new BracketId(), p))
                .collect(Collectors.toList());
    }
}