package com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos;

import org.bson.types.ObjectId;

public class BracketMongoDto {
    public ObjectId id;
    public String bracketId;
    public ParticipantMongoDto winner;
    public ParticipantMongoDto participant;
    public ParticipantMongoDto participantOne;
    public ParticipantMongoDto participantTwo;
    public BracketMongoDto bracketOne;
    public BracketMongoDto bracketTwo;
}
