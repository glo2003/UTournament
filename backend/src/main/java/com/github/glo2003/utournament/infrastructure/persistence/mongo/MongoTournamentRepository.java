package com.github.glo2003.utournament.infrastructure.persistence.mongo;

import com.github.glo2003.utournament.entities.Tournament;
import com.github.glo2003.utournament.entities.TournamentId;
import com.github.glo2003.utournament.entities.TournamentRepository;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.assemblers.TournamentMongoAssembler;
import com.github.glo2003.utournament.infrastructure.persistence.mongo.dtos.TournamentMongoDto;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class MongoTournamentRepository implements TournamentRepository {
    private final MongoCollection<TournamentMongoDto> tournaments;
    private final TournamentMongoAssembler tournamentMongoAssembler;

    public MongoTournamentRepository(String connString) {
        // TODO remove
//        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
//        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//                                             pojoCodecRegistry);

        ConnectionString connectionString = new ConnectionString(connString);
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("tournaments");
        tournaments = database.getCollection("tournaments", TournamentMongoDto.class);
        tournamentMongoAssembler = new TournamentMongoAssembler();
    }

    @Override
    public Optional<Tournament> get(TournamentId id) {
        TournamentMongoDto dto = tournaments.find(Filters.eq("tournamentId", id.toString())).first();
        Optional<TournamentMongoDto> d = Optional.ofNullable(dto);
        return d.map(tournamentMongoAssembler::fromDto);
    }

    @Override
    public void save(Tournament tournament) {
        String id = tournament.getTournamentId().toString();
        TournamentMongoDto foundDto = tournaments.find(Filters.eq("tournamentId", id)).first();
        if (foundDto == null) {
            tournaments.insertOne(tournamentMongoAssembler.toDto(tournament));
        } else {
            TournamentMongoDto dto = tournamentMongoAssembler.toDto(tournament);
            tournaments.findOneAndReplace(Filters.eq("tournamentId", id), dto, new FindOneAndReplaceOptions().upsert(true));
        }
    }

    @Override
    public void remove(TournamentId tournamentId) {
        tournaments.deleteOne(Filters.eq("id", tournamentId));
    }
}
