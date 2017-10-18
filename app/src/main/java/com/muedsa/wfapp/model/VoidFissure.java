package com.muedsa.wfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VoidFissure implements Parcelable {

    private String id;
    private String place;
    private String planet;
    private String mission;
    private String modifier;
    private String startTime;
    private String endTime;

    public VoidFissure() {
    }

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

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
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

    public VoidFissure(Parcel in) {
        this.id = in.readString();
        this.place = in.readString();
        this.planet = in.readString();
        this.mission = in.readString();
        this.modifier = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
    }
    public static final Creator<VoidFissure> CREATOR = new Creator<VoidFissure>() {
        @Override
        public VoidFissure createFromParcel(Parcel in) {
            return new VoidFissure(in);
        }

        @Override
        public VoidFissure[] newArray(int size) {
            return new VoidFissure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.place);
        dest.writeString(this.planet);
        dest.writeString(this.mission);
        dest.writeString(this.modifier);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }
}
