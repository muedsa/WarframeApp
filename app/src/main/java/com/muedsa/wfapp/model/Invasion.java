package com.muedsa.wfapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Invasion implements Parcelable {

    private String id;
    private String place;
    private String planet;
    private String factionA;
    private String awardsA;
    private String factionB;
    private String awardsB;
    private String percentage;
    private String type;

    public Invasion(){
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

    public String getFactionA() {
        return factionA;
    }

    public void setFactionA(String factionA) {
        this.factionA = factionA;
    }

    public String getAwardsA() {
        return awardsA;
    }

    public void setAwardsA(String awardsA) {
        this.awardsA = awardsA;
    }

    public String getFactionB() {
        return factionB;
    }

    public void setFactionB(String factionB) {
        this.factionB = factionB;
    }

    public String getAwardsB() {
        return awardsB;
    }

    public void setAwardsB(String awardsB) {
        this.awardsB = awardsB;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Invasion(Parcel in){
        this.id = in.readString();
        this.place = in.readString();
        this.planet = in.readString();
        this.factionA = in.readString();
        this.awardsA = in.readString();
        this.factionB = in.readString();
        this.awardsB = in.readString();
        this.percentage = in.readString();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(place);
        dest.writeString(planet);
        dest.writeString(factionA);
        dest.writeString(awardsA);
        dest.writeString(factionB);
        dest.writeString(awardsB);
        dest.writeString(percentage);
        dest.writeString(type);
    }

    public static final Creator<Invasion> CREATOR = new Creator<Invasion>() {
        @Override
        public Invasion createFromParcel(Parcel in) {
            return new Invasion(in);
        }

        @Override
        public Invasion[] newArray(int size) {
            return new Invasion[size];
        }
    };
}
