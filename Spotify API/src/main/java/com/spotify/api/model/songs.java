package com.spotify.api.model;

import org.bson.types.ObjectId;

public class songs {
	private ObjectId song_id;
	private Integer position;

	public songs() {}
	public songs(ObjectId song_id, Integer position) {
		this.song_id = song_id;
		this.position = position;
	}

	public ObjectId getId() {
		return song_id;
	}
	public void setId(ObjectId song_id) {
		this.song_id = song_id;
	}
	public Integer getPosition() {return position;}
	public void setPosition(Integer position) {this.position = position;}

	@Override
	public String toString() {
		return "songs [song_id=" + song_id + ", position=" + position + "]";
	}
}
