package com.rkc.zds.resource.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ProfileData {
	
	@JsonIgnore
    private Integer id;
    private String userName;
    private String bio;
    private String image;
    private boolean following;
    

	public ProfileData(Integer id, String userName, String bio, String image, boolean following) {
		this.id = id;
		this.userName = userName;
		this.bio = bio;
		this.image=image;
		this.following = following;
	}
	
	public ProfileData() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public boolean isFollowing() {
		return following;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}

}
