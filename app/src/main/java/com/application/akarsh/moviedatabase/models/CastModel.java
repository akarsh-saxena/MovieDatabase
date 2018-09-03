package com.application.akarsh.moviedatabase.models;

/**
 * Created by Akarsh on 01-06-2017.
 */

public class CastModel {

    String chracter;
    String name;
    String profile_path;
    long id;

    public String getChracter() {
        return chracter;
    }

    public void setChracter(String chracter) {
        this.chracter = chracter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
