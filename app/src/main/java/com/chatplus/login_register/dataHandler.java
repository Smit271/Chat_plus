package com.chatplus.login_register;

public class dataHandler {
    public String username, name, email, pass, hash_id;
    int index;

    public dataHandler(String username, String name, String email, String pass, String hash_id) {
        this.hash_id = hash_id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public String getHash_id() {
        return hash_id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setHash_id(String hash_id) {
        this.hash_id = hash_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
