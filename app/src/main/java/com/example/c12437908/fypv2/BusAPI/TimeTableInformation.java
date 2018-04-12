package com.example.c12437908.fypv2.BusAPI;

import java.util.ArrayList;

/**
 * Created by cinema on 3/7/2018.
 */

public class TimeTableInformation {
    String errorcode;
    String errormessage;
    String numberofresults;
    String stopid;
    String route;
    String timestamp;
    ArrayList<TimeTable> results;

    public TimeTableInformation() {
    }

    public TimeTableInformation(String errorcode, String errormessage, String numberofresults, String stopid, String route, String timestamp, ArrayList<TimeTable> results) {
        this.errorcode = errorcode;
        this.errormessage = errormessage;
        this.numberofresults = numberofresults;
        this.stopid = stopid;
        this.route = route;
        this.timestamp = timestamp;
        this.results = results;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getNumberofresults() {
        return numberofresults;
    }

    public void setNumberofresults(String numberofresults) {
        this.numberofresults = numberofresults;
    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<TimeTable> getResults() {
        return results;
    }

    public void setResults(ArrayList<TimeTable> results) {
        this.results = results;
    }
}
