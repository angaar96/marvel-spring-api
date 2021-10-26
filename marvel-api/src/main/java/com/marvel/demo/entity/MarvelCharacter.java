package com.marvel.demo.entity;

import com.google.gson.JsonObject;

public class MarvelCharacter {
    private int id;
    private String name;
    private String description;
    private JsonObject thumbnail;
    public MarvelCharacter(int id, String description, String name, JsonObject thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
