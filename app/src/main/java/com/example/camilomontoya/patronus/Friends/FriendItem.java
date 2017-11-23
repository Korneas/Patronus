package com.example.camilomontoya.patronus.Friends;

/**
 * Created by Camilo Montoya on 11/23/2017.
 */

public class FriendItem {

    private String profilePicUrl, name, email, uid;

    public FriendItem(String profilePicUrl, String name, String email, String uid) {
        this.profilePicUrl = profilePicUrl;
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
