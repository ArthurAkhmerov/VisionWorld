package com.example.arthurpc.gps_application.infrastructure;

import android.content.Context;

public class Auth {
    private  final Context context;
    private User user;
    private String hash;

    public Auth(Context context) {
        this.context = context;
        this.user = new User();
    }

    public User getUser() {
        return user;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
