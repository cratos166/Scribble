package com.nbird.scribble.MAIN_MENU.Model;

public class PlayerDetails {

    String myName;
    String myImage;
    String UID;


    public PlayerDetails() {
    }

    public PlayerDetails(String myName, String myImage,String UID) {
        this.myName = myName;
        this.myImage = myImage;
        this.UID=UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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
