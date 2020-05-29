package com.rkc.zds.resource.model;

import lombok.Getter;

@Getter
public class UserWithToken {
    private String email;
    private String userName;
    private String bio;
    private String image;
    private String token;
    
	public UserWithToken(UserData userData, String token) {
        this.email = userData.getEmail();
        this.userName = userData.getUserName();
        this.bio = userData.getBio();
        this.image = userData.getImage();
        this.token = token;
    }
	
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
