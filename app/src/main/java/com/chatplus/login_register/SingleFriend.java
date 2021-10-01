package com.chatplus.login_register;

import android.graphics.Bitmap;

public class SingleFriend {

    private String username, name_of_user;
    private int profile_image_id;
    private Bitmap profilePictureBitmap;
    private String uri;

    public SingleFriend(String username, int profile_image_id, Bitmap profilePictureBitmap,String uri) {
        this.username = username;
        this.profile_image_id = profile_image_id;
        this.profilePictureBitmap = profilePictureBitmap;
        this.uri = uri;
    }

    public SingleFriend(String username, String name_of_user, int profile_image_id, Bitmap profilePictureBitmap, String uri) {
        this.username = username;
        this.name_of_user = name_of_user;
        this.profile_image_id = profile_image_id;
        this.profilePictureBitmap = profilePictureBitmap;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Bitmap getProfilePictureBitmap() {
        return profilePictureBitmap;
    }

    public void setProfilePictureBitmap(Bitmap profilePictureBitmap) {
        this.profilePictureBitmap = profilePictureBitmap;
    }

    public String getName_of_user() {
        return name_of_user;
    }

    public void setName_of_user(String name_of_user) {
        this.name_of_user = name_of_user;
    }
}
