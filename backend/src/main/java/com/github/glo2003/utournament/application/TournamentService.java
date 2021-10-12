package com.github.glo2003.utournament.application;

import com.github.glo2003.utournament.application.assembler.BracketAssembler;
import com.github.glo2003.utournament.application.assembler.ParticipantAssembler;
import com.github.glo2003.utournament.application.assembler.TournamentAssembler;
import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ParticipantDto;
import com.github.glo2003.utournament.application.dtos.TournamentDto;
import com.github.glo2003.utournament.application.exceptions.NamesNotUniqueException;
import com.github.glo2003.utournament.application.exceptions.TournamentNotFoundException;
import com.github.glo2003.utournament.entities.*;
import com.github.glo2003.utournament.entities.bracket.Bracket;
import com.github.glo2003.utournament.entities.bracket.BracketId;
import com.github.glo2003.utournament.entities.bracket.visitors.FindPlayableBracketsVisitor;
import com.github.glo2003.utournament.entities.bracket.visitors.WinBracketVisitor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TournamentService {
    private final TournamentFactory tournamentFactory;
    private final TournamentRepository tournamentRepository;
    private final TournamentAssembler tournamentAssembler;
    private final ParticipantAssembler participantAssembler;
    private final BracketAssembler bracketAssembler;

    public TournamentService(TournamentFactory tournamentFactory, TournamentRepository tournamentRepository) {
        this.tournamentFactory = tournamentFactory;
        this.tournamentRepository = tournamentRepository;
        this.participantAssembler = new ParticipantAssembler();
        this.tournamentAssembler = new TournamentAssembler();
        this.bracketAssembler = new BracketAssembler();
    }

    public TournamentId createTournament(String name, List<ParticipantDto> participantDtos) {
        Set<String> names = new HashSet<>();
        for (ParticipantDto p : participantDtos) {
            if (names.contains(p.name)) {
                throw new NamesNotUniqueException();
            }
            names.add(p.name);
        }

        List<Participant> participants = participantDtos.stream()
                .map(participantAssembler::fromDto)
                .collect(Collectors.toList());
        Tournament tournament = tournamentFactory.createTournament(name, participants);
        tournamentRepository.save(tournament);

        return tournament.getId();
    }

    public TournamentDto getTournament(String tournamentIdString) {
        TournamentId tournamentId = parseTournamentId(tournamentIdString);
        Tournament tournament = TournamentService.this.getTournament(tournamentId);
        return tournamentAssembler.toDto(tournament);
    }

    public List<BracketDto> getPlayableBrackets(String tournamentIdString) {
        TournamentId tournamentId = parseTournamentId(tournamentIdString);
        Tournament tournament = getTournament(tournamentId);
        Bracket bracket = tournament.getBracket();

        FindPlayableBracketsVisitor visitor = new FindPlayableBracketsVisitor();
        bracket.accept(visitor);

        return visitor.getPlayable().stream()
                .map(bracketAssembler::toDto)
                .collect(Collectors.toList());
    }

    public void winBracket(String tournamentIdString, String bracketIdString, ParticipantDto winnerDto) {
        TournamentId tournamentId = parseTournamentId(tournamentIdString);
        Tournament tournament = getTournament(tournamentId);
        Bracket bracket = tournament.getBracket();
        BracketId bracketId = parseBracketId(bracketIdString);
        Participant winner = participantAssembler.fromDto(winnerDto);

        WinBracketVisitor visitor = new WinBracketVisitor(bracketId, winner);
        bracket.accept(visitor);

        tournamentRepository.save(tournament);
    }

    public void deleteTournament(String tournamentIdString) {
        TournamentId tournamentId = parseTournamentId(tournamentIdString);
        tournamentRepository.remove(tournamentId);
    }

    private TournamentId parseTournamentId(String tournamentIdString) {
        try {
            int i = Integer.parseInt(tournamentIdString);
            return new TournamentId(i);
        } catch (NumberFormatException e) {
            throw new TournamentNotFoundException(tournamentIdString);
        }
    }

    private BracketId parseBracketId(String bracketIdString) {
        try {
            int i = Integer.parseInt(bracketIdString);
            return new BracketId(i);
        } catch (NumberFormatException e) {
            throw new TournamentNotFoundException(bracketIdString);
        }
    }

    private Tournament getTournament(TournamentId tournamentId) {
        Optional<Tournament> optionalTournament = tournamentRepository.get(tournamentId);
        return optionalTournament.orElseThrow(
                () -> new TournamentNotFoundException(tournamentId.toString()));
    }
}
