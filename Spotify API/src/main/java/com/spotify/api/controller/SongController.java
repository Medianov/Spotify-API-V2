package com.spotify.api.controller;


import com.spotify.api.model.Playlist;
import com.spotify.api.model.Song;
import com.spotify.api.model.songs;
import com.spotify.api.repository.PlaylistRepository;
import com.spotify.api.repository.SongRepository;
import com.spotify.api.repository.UserRepository;
import com.spotify.api.service.PlaylistService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spotify.api.service.SongService;

import demo.exception.RecordNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* Zum Testen fuer Postmann
* http://localhost:8080/songs/filter?pageNo=0&pageSize=5&sortBy=artist&artist=Mad Christiane&title=Loud Sales
* http://localhost:8080/songs/filter?artist=Professional Naz
* http://localhost:8080/graphql
*
* */


@RestController
@RequestMapping("/songs")
public class SongController {
    @Autowired
    SongService songService;
    @Autowired
    SongRepository songRepository;
    @Autowired
    PlaylistService playlistService;
    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    UserRepository userRepository;
    Integer SongPositionInPlaylist = 0;


    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy)
    {
        List<Song> list = songService.getAllSongs(pageNo, pageSize, sortBy);
        return new ResponseEntity<List<Song>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    // Get fuer Postmann, nach Artist und Interpret Filterbar
    @GetMapping("/filter")
    public ResponseEntity<List<Song>> getAllSongsByArtist(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "artist") String sortBy,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String title)
    {
        if(artist != null && title == null) {
            List<Song> list = songService.getAllSongsByArtist(pageNo, pageSize, sortBy, artist);
            return new ResponseEntity<List<Song>>(list, new HttpHeaders(), HttpStatus.OK);
        } else if(artist != null && title != null) {
            List<Song> list = songService.getAllSongsByArtistAndTitle(pageNo, pageSize, sortBy, artist, title);
            return new ResponseEntity<List<Song>>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            List<Song> list = songService.getAllSongsByTitle(pageNo, pageSize, sortBy, title);
            return new ResponseEntity<List<Song>>(list, new HttpHeaders(), HttpStatus.OK);
        }
    }

    // Zum probrieren :)
    @GetMapping("/gg")
    public String songAsString(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy)
    {
        String list = songService.getAllSongs(pageNo, pageSize, sortBy).toString();
        return list.toString();
    }

    // Fuer ein einzelnen Song
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") ObjectId id)
                                                    throws RecordNotFoundException {
        Song entity = songService.getSongById(id);
        return new ResponseEntity<Song>(entity, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/playlist/{id}")
    public ResponseEntity<List<Playlist>> getPlaylistByID(@PathVariable("id") String id) throws RecordNotFoundException {
         List<Playlist> playlistList = playlistService.lookupOperation(id);
        return new ResponseEntity<List<Playlist>>(playlistList, new HttpHeaders(), HttpStatus.OK);
    }


    // Song hinzufügen
    @PostMapping
    public ResponseEntity<Song> save(@RequestBody Song song) {
        if (song.getArtist() != null && song.getTitle() != null) {
            songService.save(song);
            return new ResponseEntity<Song>(null, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<Song>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    // songs bearbeiten
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable("id") ObjectId id,
                                           @RequestBody Song song)
                                                    throws RecordNotFoundException {
        if(song.getTitle() != null && song.getArtist() != null) {
            Song updated = songService.updateSong(song, id);
            return new ResponseEntity<Song>(null, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<Song>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }// Hier bitte bearbeiten

    // Song loeschen
    @DeleteMapping("/{id}")
    public HttpStatus deleteSongById(@PathVariable("id") ObjectId id)
            throws RecordNotFoundException {
        songService.deleteSongById(id);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("/playlist_create1")
    public ResponseEntity<Playlist> addPlaylist_over_JSON(@RequestBody Playlist playlist) {
        if(playlist.getOwner_id() != null && userRepository.findById(playlist.getOwner_id()).isPresent()) {
            playlist.setCreated_date(LocalDateTime.now().toString());
            System.out.println(playlist);
            playlistRepository.save(playlist);
            return new ResponseEntity<Playlist>(null, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<Playlist>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping ("/playlist_insert")
    public ResponseEntity<String> addPlaylist(
            @RequestBody Playlist playlist)
            throws RecordNotFoundException {
        List<songs> songs_list = new ArrayList<songs>();
        if(playlist.getOwner_id() != null && userRepository.findById(playlist.getOwner_id()).isPresent()) {
            playlist.getSong().forEach(song -> {
                        songRepository.insert(song);
                        SongPositionInPlaylist++;
                        songs s = new songs(song.getIdObject(), SongPositionInPlaylist);
                        songs_list.add(s);
                    }
            );
            Playlist playlist2 = new Playlist(playlist.getName(),
                    LocalDateTime.now().toString(),
                    playlist.getOwner_id(),
                    songs_list);
            playlistRepository.insert(playlist2);
            return new ResponseEntity<String>(playlist2.getId().toString(), new HttpHeaders(), HttpStatus.OK);
        }
        System.out.println(playlist);
        return new ResponseEntity<String>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
