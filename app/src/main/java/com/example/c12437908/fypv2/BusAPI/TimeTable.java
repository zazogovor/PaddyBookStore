package com.example.c12437908.fypv2.BusAPI;

import java.util.ArrayList;

/**
 * Created by cinema on 3/7/2018.
 */

public class TimeTable {

    String startdayofweek;
    String enddayofweek;
    String destination;
    String destinationlocalized;
    String lastupdated;

    //Refers to the departing times for the bus.
    ArrayList<String> departures;

    public TimeTable() {
    }

    public TimeTable(String startdayofweek, String enddayofweek, String destination, String destinationlocalized, String lastupdated, ArrayList<String> departures) {
        this.startdayofweek = startdayofweek;
        this.enddayofweek = enddayofweek;
        this.destination = destination;
        this.destinationlocalized = destinationlocalized;
        this.lastupdated = lastupdated;
        this.departures = departures;
    }

    public String getStartdayofweek() {
        return startdayofweek;
    }

    public void setStartdayofweek(String startdayofweek) {
        this.startdayofweek = startdayofweek;
    }

    public String getEnddayofweek() {
        return enddayofweek;
    }

    public void setEnddayofweek(String enddayofweek) {
        this.enddayofweek = enddayofweek;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationlocalized() {
        return destinationlocalized;
    }

    public void setDestinationlocalized(String destinationlocalized) {
        this.destinationlocalized = destinationlocalized;
    }

    public String getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(String lastupdated) {
        this.lastupdated = lastupdated;
    }

    public ArrayList<String> getDepartures() {
        return departures;
    }

    public void setDepartures(ArrayList<String> departures) {
        this.departures = departures;
    }
}
