package com.github.glo2003.utournament;

import com.github.glo2003.utournament.api.TournamentRessource;
import com.github.glo2003.utournament.application.TournamentService;
import com.github.glo2003.utournament.entities.TournamentFactory;
import com.github.glo2003.utournament.entities.TournamentRepository;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.infrastructure.persistence.TournamentRepositoryInMemory;

import static spark.Spark.*;

public class UTournament {

    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static void main(String[] args) {
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        port(8080);
        enableCORS("*", "*", "*");

        TournamentRessource tournamentRessource = getTournamentRessource();
        tournamentRessource.registerRoutes();
    }

    private static TournamentRessource getTournamentRessource() {
        BracketFactory bracketFactory = new BracketFactory();
        TournamentFactory tournamentFactory = new TournamentFactory(bracketFactory);
        TournamentRepository tournamentRepository = new TournamentRepositoryInMemory();
        TournamentService tournamentService = new TournamentService(tournamentFactory, tournamentRepository);
        return new TournamentRessource(tournamentService);
    }

    private static void enableCORS(final String origin, final String methods, final String headers) {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers(ACCESS_CONTROL_REQUEST_HEADERS);
            if (accessControlRequestHeaders != null) {
                response.header(ACCESS_CONTROL_ALLOW_HEADERS, accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers(ACCESS_CONTROL_REQUEST_METHOD);
            if (accessControlRequestMethod != null) {
                response.header(ACCESS_CONTROL_ALLOW_METHODS, accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            response.header(ACCESS_CONTROL_REQUEST_METHOD, methods);
            response.header(ACCESS_CONTROL_ALLOW_HEADERS, headers);
            response.type(APPLICATION_JSON);
        });
    }
}
