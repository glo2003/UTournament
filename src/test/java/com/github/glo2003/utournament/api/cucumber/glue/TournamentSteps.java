package com.github.glo2003.utournament.api.cucumber.glue;

import com.github.glo2003.utournament.UTournament;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.SingleBracketDto;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.JavaType;
import io.cucumber.messages.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.glo2003.utournament.api.TournamentEnd2EndTestUtils.*;
import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.when;
import static spark.Spark.stop;

public class TournamentSteps {

    private static final String BASE_URL = "/tournaments";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DefaultParameterTransformer
    @DefaultDataTableEntryTransformer
    @DefaultDataTableCellTransformer
    public Object defaultTransformer(Object fromValue, Type toValueType) {
        JavaType javaType = objectMapper.constructType(toValueType);
        return objectMapper.convertValue(fromValue, javaType);
    }

    List<ParticipantDto> participantDtos;
    String tournamentId;
    String bracketId;
    ParticipantDto winner;

    @BeforeAll
    public static void startServer() throws InterruptedException {
        UTournament.main(new String[0]);
    }

    @Before
    public void setUp() {
        participantDtos = new ArrayList<>();
        tournamentId = "";
    }

    @AfterAll
    public static void tearDownServer() {
        stop();
    }

    @Given("^the following participants$")
    public void givenFollowingParticipants(final List<ParticipantDto> participantDtos) {
        this.participantDtos = participantDtos;
    }

    @Given("^a tournament named (.+)$")
    public void givenTournament(String tournamentName) {
        tournamentId = createTournamentGetId(tournamentName, participantDtos);
    }

    @When("^the user creates the tournament named (.*)$")
    public void whenCreateTournament(String tournamentName) {
        tournamentId = createTournamentGetId(tournamentName, participantDtos);
    }

    @When("^the user plays the first playable bracket$")
    public void whenPlayFirstPlayableBracket() {
        SingleBracketDto bracketDto = getPlayableSingleBracket(tournamentId);
        bracketId = bracketDto.bracketId;
        winner = bracketDto.participantTwo;
        playBracket(tournamentId, bracketId, winner);
    }

    @When("^the user deletes the tournament$")
    public void whenDeleteTournament() {
        deleteTournament(tournamentId);
    }

    @Then("^the tournament exists$")
    public void thenTournamentExists() {
        getTournament(tournamentId).then().statusCode(200);
    }

    @Then("^that bracket is played$")
    public void thenBracketIsPlayed() {
        List<BracketDto> playableBrackets = getPlayableBrackets(tournamentId);

        List<String> ids = playableBrackets.stream().map(b -> b.bracketId).collect(Collectors.toList());
        assertThat(ids).doesNotContain(bracketId);
    }

    @Then("^the tournament does not exist$")
    public void thenTournamentDoesNotExist() {
        getTournament(tournamentId).then().statusCode(404);
    }
}
