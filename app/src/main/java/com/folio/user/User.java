package com.folio.user;

public class User {

    public static User user = null;
    public static boolean LOGGED_IN = false;

    private String username;

    public User(String username){
        this.username = username;
    }
}
