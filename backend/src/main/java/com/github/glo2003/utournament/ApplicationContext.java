package com.github.glo2003.utournament;

import com.github.glo2003.utournament.api.TournamentRessource;
import com.github.glo2003.utournament.application.TournamentService;
import com.github.glo2003.utournament.entities.TournamentFactory;
import com.github.glo2003.utournament.entities.TournamentRepository;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.infrastructure.persistence.InMemoryTournamentRepository;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.MongoTournamentRepository;

import java.util.logging.Logger;

public class ApplicationContext {
    private final static Logger logger = Logger.getLogger(ApplicationContext.class.getName());
    private final int portNumber;
    private final Mode mode;

    public ApplicationContext() {
        portNumber = findPortNumber();
        mode = getApplicationMode();
    }

    public TournamentRessource getTournamentRessource() {
        BracketFactory bracketFactory = new BracketFactory();
        TournamentFactory tournamentFactory = new TournamentFactory(bracketFactory);
        TournamentRepository tournamentRepository = getTournamentRepository();
        TournamentService tournamentService = new TournamentService(tournamentFactory, tournamentRepository);
        return new TournamentRessource(tournamentService);
    }

    public TournamentRepository getTournamentRepository() {
        switch (mode) {
            case Dev:
                return new InMemoryTournamentRepository();
            case Prod:
                String mongoUrl = System.getenv("MONGO_URL");
                if (mongoUrl == null) {
                    throw new BadConfigurationException("No provided Mongo URL");
                }
                return new MongoTournamentRepository(mongoUrl);
            default:
                throw new UnsupportedOperationException("Mode " + mode + " not handled");
        }
    }

    public Mode getApplicationMode() {
        String mode = System.getenv("MODE");
        if (mode == null) {
            logger.warning(
                    "WARNING: The server mode could not be found with 'MODE' env var. Using the default one Dev"
            );
            return Mode.Dev;
        } else if (mode.equals("DEV")) {
            return Mode.Dev;
        } else if (mode.equals("PROD")) {
            return Mode.Prod;
        }
        throw new UnsupportedOperationException("Mode " + mode + " not handled");
    }

    public int getPortNumber() {
        return portNumber;
    }

    private int findPortNumber() {
        try {
            return Integer.parseInt(System.getenv("PORT"));
        } catch (NumberFormatException e) {
            logger.warning(
                    "WARNING: The server port could not be found with 'PORT' env var. Using the default one (9090)"
            );
            return 8080;
        }
    }

    public enum Mode {
        Dev, Prod
    }
}
