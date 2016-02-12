package com.bouvet.sandvika.myfriends.model;

public class User {

    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String regId;

    public User(String userName, String firstName, String lastName, String regId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regId = regId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegId() {
        return regId;
    }
}
