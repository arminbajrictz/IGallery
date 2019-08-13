package com.example.igallery;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserModel {

    private String email,password;
    private ArrayList<String> something;
    private Boolean isAdmin;
    private Boolean isBlocked;
    private String usersId;

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getSomething() {
        return something;
    }

    public void setSomething(ArrayList<String> something) {
        this.something = something;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }
}
