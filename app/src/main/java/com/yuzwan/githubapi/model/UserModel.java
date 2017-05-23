package com.yuzwan.githubapi.model;

/**
 * Created by user on 23/05/2017.
 */

public class UserModel {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String username, image;
}
