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
import com.github.glo2003.utournament.entities.bracket.IntermediateBracket;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static spark.Spark.stop;

class TournamentRessourceEnd2EndTest {

    private static final String BASE_URL = "/tournaments";
    private static final String INVALID_TOURNAMENT_ID = "123-test-invalid";
    private static final TournamentId A_TOURNAMENT_ID = new TournamentId();
    private static final String INVALID_BRACKET_ID = "invalid-123";
    private static final BracketId A_BRACKET_ID = new BracketId();

    static Gson gson = new Gson();

    @BeforeAll
    static void setUp() {
        UTournament.main(new String[0]);
    }

    @AfterAll
    static void tearDown() {
        stop();
    }

    @Test
    void healthReturnsOk() {
        Response response = when().get(BASE_URL + "/health");

        response.then().statusCode(200);
    }

    @Test
    void canCreateTournamentHasLocationHeader() {
        TournamentCreation tournamentCreation = new TournamentCreation();
        tournamentCreation.name = "Smash";
        tournamentCreation.participants = ParticipantTestUtils.createParticipantDtos(16);

        Response response = given().body(tournamentCreation).
                when().post(BASE_URL + "/");

        response.then().statusCode(200).header("Location", not(emptyOrNullString()));
    }

    @Test
    void namesNotUniqueInCreationReturnsBadRequest() {
        List<ParticipantDto> participantDtos = ParticipantTestUtils.createParticipantDtos(16);
        participantDtos.add(participantDtos.get(0));
        TournamentCreation tournamentCreation = new TournamentCreation();
        tournamentCreation.name = "Smash";
        tournamentCreation.participants = participantDtos;

        Response response = given().body(tournamentCreation).
                when().post(BASE_URL + "/");

        response.then().statusCode(400);
    }

    @Test
    void canGetTournament() throws Exception {
        String tournamentId = createTournament();

        Response response = when().get(BASE_URL + "/" + tournamentId);

        response.then().statusCode(200);
        TournamentResponse tournamentResponse = gson.fromJson(response.getBody().print(), TournamentResponse.class);
        hasNoNullField(tournamentResponse);
    }

    @Test
    void invalidIdReturnsBadRequest() {
        Response response = when().get(BASE_URL + "/" + INVALID_TOURNAMENT_ID);

        response.then().statusCode(400);
    }

    @Test
    void notFoundTournamentReturnsNotFound() {
        Response response = when().get(BASE_URL + "/" + A_TOURNAMENT_ID);

        response.then().statusCode(404);
    }

    @Test
    void canDeleteTournament() {
        String tounamentId = createTournament();

        Response response = when().delete(BASE_URL + "/" + tounamentId);

        response.then().statusCode(200);
    }

    @Test
    void canPlayBracket() {
        String tournamentId = createTournament();
        Response getResponse = when().get(BASE_URL + "/" + tournamentId);
        SingleBracketDto bracket = getPlayableSingleBracket(getResponse);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = bracket.participantOne;

        Response response = given().body(winner)
                .when().post(BASE_URL + "/" + tournamentId + "/brackets/" + bracketId);

        response.then().statusCode(200);
    }

    @Test
    void bracketAlreadyPlayedReturnsBadRequest() {
        String tournamentId = createTournament();
        Response getResponse = when().get(BASE_URL + "/" + tournamentId);
        SingleBracketDto bracket = getPlayableSingleBracket(getResponse);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = bracket.participantOne;
        String bracketURL = BASE_URL + "/" + tournamentId + "/brackets/" + bracketId;
        given().body(winner).when().post(bracketURL);

        Response response = given().body(winner)
                .when().post(bracketURL);

        response.then().statusCode(400);
    }

    @Test
    void bracketNotPlayableReturnsBadRequest() {
        String tournamentId = createTournament();
        Response getResponse = when().get(BASE_URL + "/" + tournamentId);
        IntermediateBracketDto bracket = getNonPlayableSingleBracket(getResponse);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = new ParticipantDto();

        Response response = given().body(winner)
                .when().post(BASE_URL + "/" + tournamentId + "/brackets/" + bracketId);

        response.then().statusCode(400);
    }

    @Test
    void invalidBracketIdReturnsBadRequest() {
        String tournamentId = createTournament();
        ParticipantDto winner = new ParticipantDto();

        Response response = given().body(winner)
                .when().post(BASE_URL + "/" + tournamentId + "/brackets/" + INVALID_BRACKET_ID);

        response.then().statusCode(400);
    }

    @Test
    void bracketNotFoundReturnsNotFound() {
        String tournamentId = createTournament();
        ParticipantDto winner = new ParticipantDto();

        Response response = given().body(winner)
                .when().post(BASE_URL + "/" + tournamentId + "/brackets/" + A_BRACKET_ID);

        response.then().statusCode(404);
    }

    @Test
    void participantNotInBracketReturnsBadRequest() {
        String tournamentId = createTournament();
        Response getResponse = when().get(BASE_URL + "/" + tournamentId);
        SingleBracketDto bracket = getPlayableSingleBracket(getResponse);
        String bracketId = bracket.bracketId;
        ParticipantDto winner = new ParticipantDto();

        Response response = given().body(winner)
                .when().post(BASE_URL + "/" + tournamentId + "/brackets/" + bracketId);

        response.then().statusCode(400);
    }

    private String createTournament() {
        TournamentCreation tournamentCreation = new TournamentCreation();
        tournamentCreation.name = "Smash";
        tournamentCreation.participants = ParticipantTestUtils.createParticipantDtos(16);
        return given().body(tournamentCreation).
                when().post(BASE_URL + "/").getHeader("Location");
    }

    private SingleBracketDto getPlayableSingleBracket(Response response) {
        Map<?, ?> map = gson.fromJson(response.getBody().print(), Map.class);
        return gson.fromJson(gson.toJson(((List) map.get("playable")).get(0)), SingleBracketDto.class);
    }

    private IntermediateBracketDto getNonPlayableSingleBracket(Response response) {
        Map<?, ?> map = gson.fromJson(response.getBody().print(), Map.class);
        return gson.fromJson(gson.toJson(map.get("bracket")), IntermediateBracketDto.class);
    }

    private <T> void hasNoNullField(T obj) throws Exception {
        Class<T> cls = (Class<T>) obj.getClass();
        for (Field f : cls.getDeclaredFields()) {
            if (!Modifier.isTransient(f.getModifiers()))
                assertThat(f.get(obj)).isNotNull();
        }
    }
}