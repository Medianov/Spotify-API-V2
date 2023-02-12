package com.spotify.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
public class User {

	@Id
	private ObjectId _id;
	private String name;
	private String gender;


	@Override
	public String toString() {
		return "User [_id=" + _id + ", name=" + name + ", gender=" + gender + "]";
	}
	public ObjectId getId() {
		return _id;
	}
	public void setId(ObjectId _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
