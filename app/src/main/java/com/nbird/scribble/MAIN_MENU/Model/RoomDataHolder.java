package com.nbird.scribble.MAIN_MENU.Model;

public class RoomDataHolder {

    int numberOfPlayers;
    String roomCode;
    int numberOfRounds;
    int time;
    String privacy;
    String hostName;
    String hostPic;
    Boolean isInGame;
    int hostIsOnline;
    String hostUID;

    public RoomDataHolder() {
    }

    public RoomDataHolder(int numberOfPlayers, String roomCode, int numberOfRounds, int time, String privacy, String hostName, String hostPic, Boolean isInGame, int hostIsOnline, String hostUID) {
        this.numberOfPlayers = numberOfPlayers;
        this.roomCode = roomCode;
        this.numberOfRounds = numberOfRounds;
        this.time = time;
        this.privacy = privacy;
        this.hostName = hostName;
        this.hostPic = hostPic;
        this.isInGame = isInGame;
        this.hostIsOnline = hostIsOnline;
        this.hostUID = hostUID;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPic() {
        return hostPic;
    }

    public void setHostPic(String hostPic) {
        this.hostPic = hostPic;
    }

    public Boolean getInGame() {
        return isInGame;
    }

    public void setInGame(Boolean inGame) {
        isInGame = inGame;
    }

    public int getHostIsOnline() {
        return hostIsOnline;
    }

    public void setHostIsOnline(int hostIsOnline) {
        this.hostIsOnline = hostIsOnline;
    }

    public String getHostUID() {
        return hostUID;
    }

    public void setHostUID(String hostUID) {
        this.hostUID = hostUID;
    }
}
