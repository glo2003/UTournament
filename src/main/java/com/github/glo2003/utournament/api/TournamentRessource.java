package com.github.glo2003.utournament.api;

import com.github.glo2003.utournament.api.assemblers.TournamentResponseAssembler;
import com.github.glo2003.utournament.api.requests.TournamentCreation;
import com.github.glo2003.utournament.application.TournamentService;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.application.exceptions.NamesNotUniqueException;
import com.github.glo2003.utournament.application.exceptions.TournamentNotFoundException;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketAlreadyPlayedException;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketCreationException;
import com.github.glo2003.utournament.entities.bracket.exceptions.BracketNotPlayableException;
import com.github.glo2003.utournament.entities.bracket.exceptions.ParticipantNotInBracketException;
import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.*;

public class TournamentRessource {

    private final TournamentService tournamentService;
    private final Gson gson;
    private TournamentResponseAssembler tournamentResponseAssembler;

    public TournamentRessource(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
        this.gson = new Gson();
        this.tournamentResponseAssembler = new TournamentResponseAssembler();
    }

    public void registerRoutes() {
        path("/tournaments", () -> {
            post("/", (req, res) -> {
                TournamentCreation tournamentCreation = gson.fromJson(req.body(), TournamentCreation.class);
                TournamentId id = tournamentService.createTournament(
                        tournamentCreation.name,
                        tournamentCreation.participants
                );
                res.header("Location", id.toString());
                return "";
            }, gson::toJson);

            get("/:id", (req, res) -> {
                String id = req.params(":id");
                TournamentDto tournamentDto = tournamentService.getTournament(id);
                List<BracketDto> playableBrackets = tournamentService.getPlayableBrackets(id);
                return tournamentResponseAssembler.toDto(tournamentDto, playableBrackets);
            }, gson::toJson);

            delete("/:id", (req, res) -> {
                String id = req.params(":id");
                tournamentService.deleteTournament(id);
                return "";
            }, gson::toJson);

            post("/:id/brackets/:bid", (req, res) -> {
                String id = req.params(":id");
                String bid = req.params(":bid");
                ParticipantDto winner = gson.fromJson(req.body(), ParticipantDto.class);
                tournamentService.winBracket(id, bid, winner);
                return "";
            });
        });

        exception(NamesNotUniqueException.class, (exception, request, response) -> {
            response.status(400);
        });
        exception(TournamentNotFoundException.class, (exception, request, response) -> {
            response.status(404);
        });
        exception(BracketAlreadyPlayedException.class, (exception, request, response) -> {
            response.status(400);
        });
        exception(BracketCreationException.class, (exception, request, response) -> {
            response.status(400);
        });
        exception(BracketNotPlayableException.class, (exception, request, response) -> {
            response.status(400);
        });
        exception(ParticipantNotInBracketException.class, (exception, request, response) -> {
            response.status(400);
        });
    }
}
