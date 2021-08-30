package com.github.glo2003.utournament.application;

import com.github.glo2003.utournament.application.assembler.BracketAssembler;
import com.github.glo2003.utournament.application.assembler.TournamentAssembler;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.TournamentRepository;
import com.github.glo2003.utournament.entities.bracket.BracketId;

import java.util.List;
import java.util.Optional;

// TODO test
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentAssembler tournamentAssembler;
    private final BracketAssembler bracketAssembler;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentAssembler = new TournamentAssembler();
        this.bracketAssembler = new BracketAssembler();
    }

    public TournamentId createTournament(List<Participant> participants) {
        // TODO validate name uniques
        return null;
    }

    public TournamentDto getTournament(TournamentId tournamentId) {
        Optional<Tournament> optionalTournament = tournamentRepository.get(tournamentId);
        Tournament tournament = optionalTournament.orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        return tournamentAssembler.toDto(tournament);
    }

    public List<BracketDto> getPlayableBrackets(TournamentId tournamentId) {
        return null;
    }

    public void winBracket(TournamentId tournamentId, BracketId bracketId, String winner) {}

    public void deleteTournament(TournamentId tournamentId) {

    }
}
