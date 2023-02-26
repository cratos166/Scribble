package com.nbird.scribble.WHITE_BOARD.Model;

public class ObjectModel {

    int id;
    String name;

    public ObjectModel() {
    }

    public ObjectModel(int id, String name) {
        this.id = id;
        this.name = name;
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
}
