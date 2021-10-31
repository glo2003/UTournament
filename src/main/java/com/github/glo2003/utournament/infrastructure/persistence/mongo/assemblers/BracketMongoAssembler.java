package com.github.glo2003.utournament.infrastructure.persistence.mongo.assemblers;

import com.github.glo2003.utournament.entities.Participant;
import com.github.glo2003.utournament.entities.bracket.*;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos.BracketMongoDto;

public class BracketMongoAssembler {
    private final ParticipantMongoAssembler participantMongoAssembler;

    public BracketMongoAssembler() {
        participantMongoAssembler = new ParticipantMongoAssembler();
    }

    public BracketMongoDto toDto(Bracket bracket) {
        ToMongoDtoVisitor visitor = new ToMongoDtoVisitor();
        bracket.accept(visitor);
        return visitor.getDto();
    }

    public Bracket fromDto(BracketMongoDto dto) {
        BracketId id = BracketId.fromString(dto.bracketId);
        if (dto.participant != null) {
            Participant participant = participantMongoAssembler.fromDto(dto.participant);

            return new ByeBracket(id, participant);
        } else if (dto.participantOne != null && dto.participantTwo != null) {
            Participant participantOne = participantMongoAssembler.fromDto(dto.participantOne);
            Participant participantTwo = participantMongoAssembler.fromDto(dto.participantTwo);
            Participant winner = null;
            if (dto.winner != null) {
                winner = participantMongoAssembler.fromDto(dto.winner);
            }

            return new SingleBracket(id, participantOne, participantTwo, winner);
        } else if (dto.bracketOne != null && dto.bracketTwo != null) {
            Bracket bracketOne = fromDto(dto.bracketOne);
            Bracket bracketTwo = fromDto(dto.bracketTwo);
            Participant winner = null;
            if (dto.winner != null) {
                winner = participantMongoAssembler.fromDto(dto.winner);
            }

            return new IntermediateBracket(id, bracketOne, bracketTwo, winner);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private class ToMongoDtoVisitor implements BracketVisitor {
        private BracketMongoDto dto;

        BracketMongoDto getDto() {
            return dto;
        }

        @Override
        public void visit(ByeBracket bracket) {
            BracketMongoDto dto = new BracketMongoDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantMongoAssembler::toDto)
                    .orElse(null);
            dto.participant = participantMongoAssembler.toDto(bracket.getParticipant());

            this.dto = dto;
        }

        @Override
        public void visit(SingleBracket bracket) {
            BracketMongoDto dto = new BracketMongoDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantMongoAssembler::toDto)
                    .orElse(null);
            dto.participantOne = participantMongoAssembler.toDto(bracket.getParticipantOne());
            dto.participantTwo = participantMongoAssembler.toDto(bracket.getParticipantTwo());

            this.dto = dto;
        }

        @Override
        public void visit(IntermediateBracket bracket) {
            BracketMongoDto dto = new BracketMongoDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantMongoAssembler::toDto)
                    .orElse(null);
            dto.bracketOne = toDto(bracket.getBracketOne());
            dto.bracketTwo = toDto(bracket.getBracketTwo());

            this.dto = dto;
        }
    }
}
