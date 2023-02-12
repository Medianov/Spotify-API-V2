package com.spotify.api.repository;



import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.spotify.api.model.Song;

import java.io.ObjectInput;

@Repository
public interface SongRepository extends MongoRepository<Song, ObjectId> {


    Page<Song> findByArtist(String artist, Pageable paging);
    Page<Song> findByArtistAndTitle(String artist, String title, Pageable paging);
    Page<Song> findByTitle(String title, Pageable paging);
}