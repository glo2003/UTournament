package com.github.glo2003.utournament;

import com.github.glo2003.utournament.api.TournamentRessource;

import static spark.Spark.*;

public class UTournament {
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static void main(String[] args) {
        ApplicationContext appContext = new ApplicationContext();

        port(appContext.getPortNumber());
        enableCORS("*", "*", "*");
        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(exception.getMessage());
        });

        TournamentRessource tournamentRessource = appContext.getTournamentRessource();
        tournamentRessource.registerRoutes();
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
