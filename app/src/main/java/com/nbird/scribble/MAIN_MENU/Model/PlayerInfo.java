package com.nbird.scribble.MAIN_MENU.Model;

public class PlayerInfo {

    String myName;
    String myImage;
    String UID;
    Boolean isFirstTime;

    public PlayerInfo() {
    }

    public PlayerInfo(String myName, String myImage, String UID, Boolean isFirstTime) {
        this.myName = myName;
        this.myImage = myImage;
        this.UID = UID;
        this.isFirstTime = isFirstTime;
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

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public Boolean getFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        isFirstTime = firstTime;
    }
}
