package com.example.login_register;

public class SingleFriend {

    private String username;
    private int profile_image_id;

    public SingleFriend(String username, int profile_image_id) {
        this.username = username;
        this.profile_image_id = profile_image_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProfile_image_id() {
        return profile_image_id;
    }

    public void setProfile_image_id(int profile_image_id) {
        this.profile_image_id = profile_image_id;
    }
}
