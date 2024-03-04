package com.sarveshdev.techlearn.firebase;

public class AdminModel {
    private String id, password;

    public AdminModel() {
    }

    public AdminModel(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
