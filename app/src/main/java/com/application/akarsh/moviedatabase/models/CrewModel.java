package com.application.akarsh.moviedatabase.models;

/**
 * Created by Akarsh on 03-06-2017.
 */

public class CrewModel {

    String job;
    String name;
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
