package com.nbird.scribble.GUESS_DRAWING.MODEL;

public class ResultModel {

    String name;
    String imageURL;
    String player2Str,player3Str,player4Str;

    String point2,point3,point4;
    int points;
    int extraPoint;

    public ResultModel() {
    }

    public ResultModel(String name, String imageURL, String player2Str, String player3Str, String player4Str, String point2, String point3, String point4, int points,int extraPoint) {
        this.name = name;
        this.imageURL = imageURL;
        this.player2Str = player2Str;
        this.player3Str = player3Str;
        this.player4Str = player4Str;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
        this.points = points;
        this.extraPoint=extraPoint;
    }

    public int getExtraPoint() {
        return extraPoint;
    }

    public void setExtraPoint(int extraPoint) {
        this.extraPoint = extraPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPlayer2Str() {
        return player2Str;
    }

    public void setPlayer2Str(String player2Str) {
        this.player2Str = player2Str;
    }

    public String getPlayer3Str() {
        return player3Str;
    }

    public void setPlayer3Str(String player3Str) {
        this.player3Str = player3Str;
    }

    public String getPlayer4Str() {
        return player4Str;
    }

    public void setPlayer4Str(String player4Str) {
        this.player4Str = player4Str;
    }

    public String getPoint2() {
        return point2;
    }

    public void setPoint2(String point2) {
        this.point2 = point2;
    }

    public String getPoint3() {
        return point3;
    }

    public void setPoint3(String point3) {
        this.point3 = point3;
    }

    public String getPoint4() {
        return point4;
    }

    public void setPoint4(String point4) {
        this.point4 = point4;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
