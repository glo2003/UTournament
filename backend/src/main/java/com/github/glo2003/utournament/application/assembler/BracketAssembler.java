package com.github.glo2003.utournament.application.assembler;

import com.github.glo2003.utournament.application.dtos.BracketDto;
import com.github.glo2003.utournament.application.dtos.ByeBracketDto;
import com.github.glo2003.utournament.application.dtos.IntermediateBracketDto;
import com.github.glo2003.utournament.application.dtos.SingleBracketDto;
import com.github.glo2003.utournament.entities.bracket.*;

public class BracketAssembler {
    private final ParticipantAssembler participantAssembler;

    public BracketAssembler() {
        participantAssembler = new ParticipantAssembler();
    }

    public BracketDto toDto(Bracket bracket) {
        ToDtoVisitor visitor = new ToDtoVisitor();
        bracket.accept(visitor);
        return visitor.getDto();
    }

    private class ToDtoVisitor implements BracketVisitor {

        private BracketDto dto;

        BracketDto getDto() {
            return dto;
        }

        @Override
        public void visit(ByeBracket bracket) {
            ByeBracketDto dto = new ByeBracketDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantAssembler::toDto)
                    .orElse(null);
            dto.participant = participantAssembler.toDto(bracket.getParticipant());

            this.dto = dto;
        }

        @Override
        public void visit(SingleBracket bracket) {
            SingleBracketDto dto = new SingleBracketDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantAssembler::toDto)
                    .orElse(null);
            dto.participantOne = participantAssembler.toDto(bracket.getParticipantOne());
            dto.participantTwo = participantAssembler.toDto(bracket.getParticipantTwo());

            this.dto = dto;
        }

        @Override
        public void visit(IntermediateBracket bracket) {
            IntermediateBracketDto dto = new IntermediateBracketDto();

            dto.bracketId = bracket.getBracketId().toString();
            dto.winner = bracket.getWinner()
                    .map(participantAssembler::toDto)
                    .orElse(null);
            dto.bracketOne = toDto(bracket.getBracketOne());
            dto.bracketTwo = toDto(bracket.getBracketTwo());

            this.dto = dto;
        }
    }
}
