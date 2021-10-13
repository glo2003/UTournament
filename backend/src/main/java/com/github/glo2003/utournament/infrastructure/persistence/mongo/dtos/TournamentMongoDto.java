package com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos;

import org.bson.types.ObjectId;

import java.util.List;

public class TournamentMongoDto {
    public ObjectId id;
    public String tournamentId;
    public String name;
    public List<ParticipantMongoDto> participants;
    public BracketMongoDto bracket;
}
