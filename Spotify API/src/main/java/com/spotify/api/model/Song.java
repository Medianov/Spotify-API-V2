package com.spotify.api.model;


import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "song")
public class Song {
	public Song() {}

	public Song( String title, String artist) {
		this.title = title;
		this.artist = artist;
	}

	@Id
	private ObjectId id;
	private String title;
	private String artist;
	private Integer position;

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getId() {
		return id.toString();
	}
	public ObjectId getIdObject() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}


	@Override
	public String toString() {
		return "Song [id=" + id.toString() + ",\n" +
				"title=" + title + ",\n" +
				"artist" + artist + "]";
	}
}
