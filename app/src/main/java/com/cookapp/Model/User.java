package com.cookapp.Model;

public class User {
    private String Name;
    private String Password;
    private String NrTel;

    public User() {

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNrTel() {
        return NrTel;
    }

    public void setNrTel(String nrTel) {
        NrTel = nrTel;
    }
}