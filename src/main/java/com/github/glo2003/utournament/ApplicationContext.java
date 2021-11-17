package com.github.glo2003.utournament;

import com.github.glo2003.utournament.api.TournamentRessource;
import com.github.glo2003.utournament.application.TournamentService;
import com.github.glo2003.utournament.entities.TournamentFactory;
import com.github.glo2003.utournament.entities.TournamentRepository;
import com.github.glo2003.utournament.entities.bracket.BracketFactory;
import com.github.glo2003.utournament.entities.bracket.visitors.FindPlayableBracketsVisitor;
import com.github.glo2003.utournament.entities.bracket.visitors.WinBracketVisitor;
import com.github.glo2003.utournament.infrastructure.persistence.InMemoryTournamentRepository;

import java.util.logging.Logger;

public class ApplicationContext {

    public enum ApplicationMode {
        Dev, Prod
    }

    private final static Logger logger = Logger.getLogger(ApplicationContext.class.getName());
    private final int portNumber;
    private final ApplicationMode applicationMode;

    public ApplicationContext() {
        portNumber = findPortNumber();
        applicationMode = getApplicationMode();
    }

    public TournamentRessource getTournamentRessource() {
        BracketFactory bracketFactory = new BracketFactory();
        TournamentFactory tournamentFactory = new TournamentFactory(bracketFactory);
        TournamentRepository tournamentRepository = getTournamentRepository();
        FindPlayableBracketsVisitor findPlayableBracketsVisitor = new FindPlayableBracketsVisitor();
        WinBracketVisitor winBracketVisitor = new WinBracketVisitor();
        TournamentService tournamentService = new TournamentService(tournamentFactory,
                tournamentRepository,
                findPlayableBracketsVisitor,
                winBracketVisitor);
        return new TournamentRessource(tournamentService);
    }

    public TournamentRepository getTournamentRepository() {
        switch (applicationMode) {
            case Dev:
                return new InMemoryTournamentRepository();
            default:
                throw new UnsupportedOperationException("Mode " + applicationMode + " not handled");
        }
    }

    public ApplicationMode getApplicationMode() {
        String mode = System.getenv("MODE");
        if (mode == null) {
            logger.warning(
                    "WARNING: The server mode could not be found with 'MODE' env var. Using the default one Dev"
            );
            return ApplicationMode.Dev;
        } else if (mode.equals("DEV")) {
            return ApplicationMode.Dev;
        } else if (mode.equals("PROD")) {
            return ApplicationMode.Prod;
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
}
