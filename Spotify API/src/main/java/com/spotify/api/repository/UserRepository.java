package com.spotify.api.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spotify.api.model.User;



@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

	
}
