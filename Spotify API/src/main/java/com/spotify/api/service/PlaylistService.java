package com.spotify.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.spotify.api.model.Playlist;


@Service
public class PlaylistService implements GraphQLQueryResolver, GraphQLMutationResolver{
    @Autowired
    private MongoTemplate mongoTemplate;

    private Logger LOGGER = LoggerFactory.getLogger(PlaylistService.class);


    public List<Playlist> lookupOperation(String id){
    LookupOperation lookupfollowers = LookupOperation.newLookup()
            .from("user")
            .localField("followers.follower_id")
            .foreignField("_id")
            .as("followers");
    LookupOperation lookupowner = LookupOperation.newLookup()
            .from("user")
            .localField("owner_id")
            .foreignField("_id")
            .as("owner");
  
    	String q1 ="{$lookup: {from: \"song\",localField: \"songs.song_id\",foreignField: \"_id\",as: \"song\",},}";
    	String q2 ="{$addFields: {song: {$map: {input: \"$song\",as: \"s\",in: {_id: \"$$s._id\",position: {$arrayElemAt: [\"$songs.position\",{$indexOfArray: [\"$songs.song_id\",\"$$s._id\",],},],},artist: \"$$s.artist\",title: \"$$s.title\",},},},},}";
    	String q3 ="{$project: {song: {$sortArray: {input: \"$song\",sortBy: {position: 1},},},owner:1,followers:1,name:1,created_date:1,},}";




        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").is(id)),
            lookupfollowers,
            lookupowner,
                new CustomAggregationOperation(q1),
                new CustomAggregationOperation(q2),
                new CustomAggregationOperation(q3));

        List<Playlist> results = mongoTemplate.aggregate(aggregation, "playlist", Playlist.class).getMappedResults();
        LOGGER.info("Obj Size " + results.size());
		return results;
    }
}
