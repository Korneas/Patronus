package com.example.camilomontoya.patronus.Map;

/**
 * Created by Camilo Montoya on 11/23/2017.
 */

public class FriendChooseItem {

    private String profilePicUrl, name, email, uid;
    private boolean choosed;

    public FriendChooseItem(String profilePicUrl, String name, String email, String uid, boolean choosed) {
        this.profilePicUrl = profilePicUrl;
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.choosed = choosed;
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

    public boolean isChoosed() {
        return choosed;
    }

    public void setChoosed(boolean choosed) {
        this.choosed = choosed;
    }
}
