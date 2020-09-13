package com.propscout.data.models;

import java.io.InputStream;
import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String role;
    private String avatar;
    private transient InputStream fingerprint;
    private String password;
    private int login_count;
    private String create_at;
    private String updated_at;

    public User() {
    }

    public User(int id, String name, String username, String email, String mobile, String role, String avatar, InputStream fingerprint, String password, int login_count, String create_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
        this.avatar = avatar;
        this.fingerprint = fingerprint;
        this.password = password;
        this.login_count = login_count;
        this.create_at = create_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public InputStream getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(InputStream fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLogin_count() {
        return login_count;
    }

    public void setLogin_count(int login_count) {
        this.login_count = login_count;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
