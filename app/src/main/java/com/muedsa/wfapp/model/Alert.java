package com.muedsa.wfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable{

    private String id;
    private String place;
    private String planet;
    private String mission;
    private String faction;
    private String level;
    private String startTime;
    private String endTime;
    private String awards;
    private String archwing;


    public Alert(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getArchwing() {
        return archwing;
    }

    public void setArchwing(String archwing) {
        this.archwing = archwing;
    }


    public Alert(Parcel in) {
        id = in.readString();
        place = in.readString();
        planet= in.readString();
        mission = in.readString();
        faction = in.readString();
        level = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        awards = in.readString();
        archwing = in.readString();
    }

    public static final Creator<Alert> CREATOR = new Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(place);
        dest.writeString(planet);
        dest.writeString(mission);
        dest.writeString(faction);
        dest.writeString(level);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(awards);
        dest.writeString(archwing);
    }
}
