package com.github.glo2003.utournament.api;

import com.github.glo2003.utournament.api.requests.TournamentCreation;
import com.github.glo2003.utournament.api.response.TournamentResponse;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.IntermediateBracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.SingleBracketDto;
import com.google.gson.Gson;
import io.restassured.response.Response;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class TournamentEnd2EndTestUtils {

    public static final String BASE_URL = "/tournaments";

    public static final Gson gson = new Gson();

    public static Response getHealth() {
        return when().get(BASE_URL + "/health");
    }

    public static String createTournamentGetId(String tournamentName, List<ParticipantDto> participantDtos) {
        Response response = createTournament(tournamentName, participantDtos);
        return response.getHeader("Location");
    }

    public static Response createTournament(String tournamentName, List<ParticipantDto> participantDtos) {
        TournamentCreation tournamentCreation = new TournamentCreation();
        tournamentCreation.name = tournamentName;
        tournamentCreation.participants = participantDtos;
        return given().body(tournamentCreation).
                when().post(BASE_URL + "/");
    }

    public static Response getTournament(String tournamentId) {
        return when().get(BASE_URL + "/" + tournamentId);
    }

    public static TournamentResponse parseTournamentResponse(Response response) {
        return gson.fromJson(response.getBody().print(), TournamentResponse.class);
    }

    public static List<BracketDto> getPlayableBrackets(String tournamentId) {
        Response response = getTournament(tournamentId);
        TournamentResponse tournamentResponse = parseTournamentResponse(response);
        return tournamentResponse.playable;
    }

    public static SingleBracketDto getPlayableSingleBracket(String tournamentId) {
        Response response = when().get(BASE_URL + "/" + tournamentId);
        Map<?, ?> map = gson.fromJson(response.getBody().print(), Map.class);
        return gson.fromJson(gson.toJson(((List) map.get("playable")).get(0)), SingleBracketDto.class);
    }

    public static IntermediateBracketDto getNonPlayableSingleBracket(String tournamentId) {
        Response response = when().get(BASE_URL + "/" + tournamentId);
        Map<?, ?> map = gson.fromJson(response.getBody().print(), Map.class);
        return gson.fromJson(gson.toJson(map.get("bracket")), IntermediateBracketDto.class);
    }

    public static Response playBracket(String tournamentId, String bracketId, ParticipantDto winner) {
        return given().body(winner).when().post(BASE_URL + "/" + tournamentId + "/brackets/" + bracketId);
    }

    public static Response deleteTournament(String tournamentId) {
        return when().delete(BASE_URL + "/" + tournamentId);
    }

    public static <T> void hasNoNullField(T obj) throws Exception {
        Class<T> cls = (Class<T>) obj.getClass();
        for (Field f : cls.getDeclaredFields()) {
            if (!Modifier.isTransient(f.getModifiers()))
                assertThat(f.get(obj)).isNotNull();
        }
    }
}
