package com.spotify.api.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spotify.api.model.Playlist;



@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, ObjectId> {

    Page<Playlist> findById(ObjectId id, Pageable paging);
}
