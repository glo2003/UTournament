package com.github.glo2003.utournament.api;

import com.github.glo2003.utournament.UTournament;
import com.github.glo2003.utournament.api.requests.TournamentCreation;
import com.github.glo2003.utournament.api.response.TournamentResponse;
import com.github.glo2003.utournament.application.dtos.IntermediateBracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.SingleBracketDto;
import com.github.glo2003.utournament.entities.ParticipantTestUtils;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.bracket.BracketId;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.glo2003.utournament.api.TournamentEnd2EndTestUtils.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static spark.Spark.stop;

class TournamentResourceEnd2EndTest {

    private static final String TOURNAMENT_NAME = "Rocket League";
    private static final String INVALID_TOURNAMENT_ID = "123-test-invalid";
    private static final String A_TOURNAMENT_ID = new TournamentId().toString();
    private static final String INVALID_BRACKET_ID = "invalid-123";
    private static final String A_BRACKET_ID = new BracketId().toString();
    private static final int NUM_PARTICIPANTS = 16;

    List<ParticipantDto> participantDtos;

    @BeforeAll
    static void startServer() {
        UTournament.main(new String[0]);
    }

    @BeforeEach
    void setUp() {
        participantDtos = ParticipantTestUtils.createParticipantDtos(NUM_PARTICIPANTS);
    }

    @AfterAll
    static void tearDownServer() {
        stop();
    }

    @Test
    void healthReturnsOk() {
        Response response = getHealth();

        response.then().statusCode(200);
    }

    @Test
    void canCreateTournamentHasLocationHeader() {
        TournamentCreation tournamentCreation = new TournamentCreation();
        tournamentCreation.name = "Smash";
        tournamentCreation.participants = ParticipantTestUtils.createParticipantDtos(NUM_PARTICIPANTS);

        Response response = createTournament(TOURNAMENT_NAME, participantDtos);

        response.then().statusCode(200).header("Location", not(emptyOrNullString()));
    }

    @Test
    void namesNotUniqueInCreationReturnsBadRequest() {
        List<ParticipantDto> participantDtos = ParticipantTestUtils.createParticipantDtosWithDuplicates(NUM_PARTICIPANTS);

        Response response = createTournament(TOURNAMENT_NAME, participantDtos);

        response.then().statusCode(400);
    }

    @Test
    void canGetTournament() throws Exception {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);

        Response response = getTournament(tournamentId);

        response.then().statusCode(200);
        TournamentResponse tournamentResponse = parseTournamentResponse(response);
        hasNoNullField(tournamentResponse);
    }

    @Test
    void invalidIdReturnsBadRequest() {
        Response response = getTournament(INVALID_TOURNAMENT_ID);

        response.then().statusCode(400);
    }

    @Test
    void notFoundTournamentReturnsNotFound() {
        Response response = getTournament(A_TOURNAMENT_ID);

        response.then().statusCode(404);
    }

    @Test
    void canDeleteTournament() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);

        Response response = deleteTournament(tournamentId);

        response.then().statusCode(200);
    }

    @Test
    void canPlayBracket() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        SingleBracketDto bracket = getPlayableSingleBracket(tournamentId);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = bracket.participantOne;

        Response response = playBracket(tournamentId, bracketId, winner);

        response.then().statusCode(200);
    }

    @Test
    void bracketAlreadyPlayedReturnsBadRequest() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        SingleBracketDto bracket = getPlayableSingleBracket(tournamentId);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = bracket.participantOne;
        playBracket(tournamentId, bracketId, winner);

        Response response = playBracket(tournamentId, bracketId, winner);

        response.then().statusCode(400);
    }

    @Test
    void bracketNotPlayableReturnsBadRequest() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        IntermediateBracketDto bracket = getNonPlayableSingleBracket(tournamentId);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = new ParticipantDto();

        Response response = playBracket(tournamentId, bracketId, winner);

        response.then().statusCode(400);
    }

    @Test
    void invalidBracketIdReturnsBadRequest() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        ParticipantDto winner = new ParticipantDto();

        Response response = playBracket(tournamentId, INVALID_BRACKET_ID, winner);

        response.then().statusCode(400);
    }

    @Test
    void bracketNotFoundReturnsNotFound() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        ParticipantDto winner = new ParticipantDto();

        Response response = playBracket(tournamentId, A_BRACKET_ID, winner);

        response.then().statusCode(404);
    }

    @Test
    void participantNotInBracketReturnsBadRequest() {
        String tournamentId = createTournamentGetId(TOURNAMENT_NAME, participantDtos);
        SingleBracketDto bracket = getPlayableSingleBracket(tournamentId);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = new ParticipantDto();

        Response response = playBracket(tournamentId, bracketId, winner);

        response.then().statusCode(400);
    }

}