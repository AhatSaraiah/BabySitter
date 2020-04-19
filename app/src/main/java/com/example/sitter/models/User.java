package com.example.sitter.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class User {

    private String name;
    public String email;
    private long id;
    private boolean gender;
    private String img;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.name = username;
        this.email = email;
    }

    public User(String name, long id, boolean genger) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }


}
