package com.example.camilomontoya.patronus.Utils;

/**
 * Created by Camilo Montoya on 11/23/2017.
 */

public class ProfilePic {

    private String pic_path, user_id;

    public ProfilePic(String pic_path, String user_id) {
        this.pic_path = pic_path;
        this.user_id = user_id;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
