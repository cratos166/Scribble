package com.nbird.scribble.MAIN_MENU.Model;

public class PlayerDetails {

    String myName;
    String myImage;


    public PlayerDetails() {
    }

    public PlayerDetails(String myName, String myImage) {
        this.myName = myName;
        this.myImage = myImage;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyImage() {
        return myImage;
    }

    public void setMyImage(String myImage) {
        this.myImage = myImage;
    }
}
