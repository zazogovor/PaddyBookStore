package com.example.c12437908.fypv2.BusAPI;


import java.util.List;

public class BusStop {

    private String  stopid;
    private String  displaystopid;
    private String  shortname;
    private String  shortnamelocalized;
    private String  fullname;
    private String  fullnamelocalized;
    private String  latitude;
    private String  longitude;
    private String  lastupdated;
    private List<Operator> operators;

    public BusStop(){

    }

    public BusStop(String stopid, String displaystopid, String shortname, String shortnamelocalized, String fullname, String fullnamelocalized, String latitude, String longitude, String lastupdated, List<Operator> operators){
        this.stopid = stopid;
        this.displaystopid = displaystopid;
        this.shortname = shortname;
        this.shortnamelocalized = shortnamelocalized;
        this.fullname = fullname;
        this.fullnamelocalized = fullnamelocalized;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastupdated = lastupdated;
        this.operators = operators;

    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public String getDisplaystopid() {
        return displaystopid;
    }

    public void setDisplaystopid(String displaystopid) {
        this.displaystopid = displaystopid;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getShortnamelocalized() {
        return shortnamelocalized;
    }

    public void setShortnamelocalized(String shortnamelocalized) {
        this.shortnamelocalized = shortnamelocalized;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullnamelocalized() {
        return fullnamelocalized;
    }

    public void setFullnamelocalized(String fullnamelocalized) {
        this.fullnamelocalized = fullnamelocalized;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

}
