package com.example.camilomontoya.patronus.Utils;

import java.util.ArrayList;

/**
 * Created by Camilo Montoya on 11/14/2017.
 */

public class User {

    private String name, email, residence, profile_pic;
    private long distance;
    private boolean suburb, comercial, street, people, car;
    private ArrayList<User> friends;

    public User(){}

    public User(String name, String email, String residence, long distance, boolean suburb, boolean comercial, boolean street, boolean people, boolean car, ArrayList<User> friends, String profilePic) {
        this.name = name;
        this.email = email;
        this.residence = residence;
        this.distance = distance;
        this.suburb = suburb;
        this.comercial = comercial;
        this.street = street;
        this.people = people;
        this.car = car;
        this.friends = friends;
        this.profile_pic = profilePic;
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

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isSuburb() {
        return suburb;
    }

    public void setSuburb(boolean suburb) {
        this.suburb = suburb;
    }

    public boolean isComercial() {
        return comercial;
    }

    public void setComercial(boolean comercial) {
        this.comercial = comercial;
    }

    public boolean isStreet() {
        return street;
    }

    public void setStreet(boolean street) {
        this.street = street;
    }

    public boolean isPeople() {
        return people;
    }

    public void setPeople(boolean people) {
        this.people = people;
    }

    public boolean isCar() {
        return car;
    }

    public void setCar(boolean car) {
        this.car = car;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public String getProfilePic() {
        return profile_pic;
    }

    public void setProfilePic(String profilePic) {
        this.profile_pic = profilePic;
    }
}
