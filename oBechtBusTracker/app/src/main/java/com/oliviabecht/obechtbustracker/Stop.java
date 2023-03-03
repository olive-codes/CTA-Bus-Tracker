package com.oliviabecht.obechtbustracker;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Stop implements Serializable, Comparable<Stop>{

    private final String sID;
    private final String sName;
    private final String sLat;
    private final String sLon;
    private final String direction;
    private final Integer distanceBetweenUser;


    public Stop(String stopID, String stopName, String stopLat, String stopLon, String direction, Integer distanceBetweenUser) {
        this.sID = stopID;
        this.sName = stopName;
        this.sLat = stopLat;
        this.sLon = stopLon;
        this.direction = direction;
        this.distanceBetweenUser = distanceBetweenUser;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistanceBetweenUser() {
        return distanceBetweenUser;
    }

    public String getSID() { return sID; }

    public String getSName() {
        return sName;
    }

    public String getSLat() { return sLat; }

    public String getSLon() {
        return sLon;
    }


    @Override
    public int compareTo(Stop stop) {
        return sID.compareTo(stop.sID);
    }

    @NonNull
    @Override
    public String toString() {
        return "Stop{" +
                "stopID='" + sID + '\'' +
                ", stopName='" + sName + '\'' +
                ", stopLat='" + sLat + '\'' +
                ", stopLon='" + sLon + '\'' +
                '}';
    }
}
