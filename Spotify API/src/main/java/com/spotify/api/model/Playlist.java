package com.spotify.api.model;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playlist")
public class Playlist {

	@Id
	private ObjectId _id;
	private String name;
	private String created_date;
	private ObjectId owner_id;
	private List<songs> songs;
	private Set<User> followers;
	private Set<Song> song;
	private Set<User> owner;


	@Override
	public String toString() {
		return "Playlist [_id = " + _id + ", name=" + name + ", created_date=" + created_date + ", owner_id=" + owner_id
				+ ", songs =" + songs + ", followers=" + followers + ", song=" + song + ", owner=" + owner + "]";
	}


	public ObjectId getId() {return _id;}
	public void setId(ObjectId _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public ObjectId getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(ObjectId owner_id) {
		this.owner_id = owner_id;
	}
	public Set<User> getFollowers() {
		return followers;
	}
	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}
	public List<songs> getSongs() {
		return songs;
	}
	public void setSongs(List<songs> songs) {
		this.songs = songs;
	}
	public Set<User> getOwner() {
		return owner;
	}
	public void setOwner(Set<User> owner) {
		this.owner = owner;
	}

	public Playlist() {}
	
	public Playlist(String name, String created_date, ObjectId owner_id, List<songs> songs) {
		this.name = name;
		this.created_date = created_date;
		this.owner_id = owner_id;
		this.songs = songs;
    }

	public Set<Song> getSong() {
		return song;
	}
	public void setSong(Set<Song> song) {
		this.song = song;
	}
}
