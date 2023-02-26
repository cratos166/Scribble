package com.nbird.scribble.GUESS_DRAWING.MODEL;

public class ChatModel {
    int key;
    String value;

    public ChatModel() {
    }

    public ChatModel(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
