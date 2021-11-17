package com.github.glo2003.utournament.application;

import com.github.glo2003.utournament.application.assembler.BracketAssembler;
import com.github.glo2003.utournament.application.assembler.ParticipantAssembler;
import com.github.glo2003.utournament.application.assembler.TournamentAssembler;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.application.exceptions.BracketNotFoundException;
import com.github.glo2003.utournament.application.exceptions.TournamentNotFoundException;
import com.github.glo2003.utournament.entities.*;
import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketId;
import com.github.glo2003.utournament.entities.bracket.visitors.FindPlayableBracketsVisitor;
import com.github.glo2003.utournament.entities.bracket.visitors.WinBracketVisitor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TournamentService {
    private final TournamentFactory tournamentFactory;
    private final TournamentRepository tournamentRepository;
    private final TournamentAssembler tournamentAssembler;
    private final ParticipantAssembler participantAssembler;
    private final BracketAssembler bracketAssembler;
    private final FindPlayableBracketsVisitor findPlayableBracketsVisitor;
    private final WinBracketVisitor winBracketVisitor;

    public TournamentService(TournamentFactory tournamentFactory,
                             TournamentRepository tournamentRepository,
                             FindPlayableBracketsVisitor findPlayableBracketsVisitor,
                             WinBracketVisitor winBracketVisitor) {
        this.tournamentFactory = tournamentFactory;
        this.tournamentRepository = tournamentRepository;
        this.findPlayableBracketsVisitor = findPlayableBracketsVisitor;
        this.winBracketVisitor = winBracketVisitor;
        this.participantAssembler = new ParticipantAssembler();
        this.tournamentAssembler = new TournamentAssembler();
        this.bracketAssembler = new BracketAssembler();
    }

    public TournamentId createTournament(String name, List<ParticipantDto> participantDtos) {
        List<Participant> participants = participantDtos.stream()
                .map(participantAssembler::fromDto)
                .collect(Collectors.toList());
        Tournament tournament = tournamentFactory.createTournament(name, participants);
        tournamentRepository.save(tournament);

        return tournament.getTournamentId();
    }

    public TournamentDto getTournament(String tournamentIdString) {
        TournamentId tournamentId = TournamentId.fromString(tournamentIdString);
        Tournament tournament = getTournament(tournamentId);
        return tournamentAssembler.toDto(tournament);
    }

    public List<BracketDto> getPlayableBrackets(String tournamentIdString) {
        TournamentId tournamentId = TournamentId.fromString(tournamentIdString);
        Tournament tournament = getTournament(tournamentId);
        Bracket bracket = tournament.getBracket();

        findPlayableBracketsVisitor.reset();

        bracket.accept(findPlayableBracketsVisitor);
        List<BracketDto> result = findPlayableBracketsVisitor.getPlayable().stream()
                .map(bracketAssembler::toDto)
                .collect(Collectors.toList());

        findPlayableBracketsVisitor.reset();

        return result;
    }

    public void winBracket(String tournamentIdString, String bracketIdString, ParticipantDto winnerDto) {
        TournamentId tournamentId = TournamentId.fromString(tournamentIdString);
        Tournament tournament = getTournament(tournamentId);
        Bracket bracket = tournament.getBracket();
        BracketId bracketId = BracketId.fromString(bracketIdString);
        Participant winner = participantAssembler.fromDto(winnerDto);

        winBracketVisitor.start(bracketId, winner);
        bracket.accept(winBracketVisitor);
        if (!winBracketVisitor.hasWon()) {
            throw new BracketNotFoundException(bracketId);
        }
        winBracketVisitor.reset();

        tournamentRepository.save(tournament);
    }

    public void deleteTournament(String tournamentIdString) {
        TournamentId tournamentId = TournamentId.fromString(tournamentIdString);
        tournamentRepository.remove(tournamentId);
    }

    private Tournament getTournament(TournamentId tournamentId) {
        Optional<Tournament> optionalTournament = tournamentRepository.get(tournamentId);
        return optionalTournament.orElseThrow(
                () -> new TournamentNotFoundException(tournamentId));
    }
}
