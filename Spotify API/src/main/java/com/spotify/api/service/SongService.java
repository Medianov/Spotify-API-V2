package com.spotify.api.service;






import com.spotify.api.model.Song;
import com.spotify.api.repository.SongRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import demo.exception.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Repository
public class SongService{
     
    @Autowired
    SongRepository songRepository;
    
     
    public List<Song> getAllSongs(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Song> pagedResult = songRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Song>();
        }
    }

    public List<Song> getAllSongsByArtist(Integer pageNo, Integer pageSize, String sortBy, String artist) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Song> pagedResult = songRepository.findByArtist(artist, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Song>();
        }
    }

    public List<Song> getAllSongsByArtistAndTitle(Integer pageNo, Integer pageSize, String sortBy, String artist, String title) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Song> pagedResult = songRepository.findByArtistAndTitle(artist, title, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Song>();
        }
    }

    public List<Song> getAllSongsByTitle(Integer pageNo, Integer pageSize, String sortBy, String title) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Song> pagedResult = songRepository.findByTitle(title, paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Song>();
        }
    }


    public Song getSongById(ObjectId id) throws RecordNotFoundException
    {
        Optional<Song> song = songRepository.findById(id);
         
        if(song.isPresent()) {
            return song.get();
        } else {
            throw new RecordNotFoundException("No song record exist for given id");
        }
    }
    public void save(Song song) {
    	songRepository.save(song);
	}
	
    public Song updateSong(Song entity, ObjectId id) throws RecordNotFoundException
    {
        Optional<Song> song = songRepository.findById(id);
         
        if(song.isPresent())
        {
            Song newEntity = song.get();
            newEntity.setTitle(entity.getTitle());
            newEntity.setArtist(entity.getArtist());
 
            newEntity = songRepository.save(newEntity);
             
            return newEntity;
        } else {
            entity = songRepository.save(entity);
             
            return entity;
        }
    }
   
     
    public void deleteSongById(ObjectId id) throws RecordNotFoundException
    {
        Optional<Song> song = songRepository.findById(id);
         
        if(song.isPresent())
        {
            songRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No song record exist for given id");
        }
    }
}