package com.example.c12437908.fypv2.BusAPI;

import java.util.List;

/**
 * Created by cinema on 2/20/2018.
 */

public class BusStopRoutes {

    private String errorcode;
    private String errormessage;
    private String numberofresults;
    private String timestamp;
    private List<Route> results;

    public BusStopRoutes(){

    }

    public BusStopRoutes(String errorcode, String errormessage, String numberofresults, String timestamp, List<Route> results){
        this.errorcode = errorcode;
        this.errormessage = errormessage;
        this.numberofresults = numberofresults;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Route> getResults() {
        return results;
    }

    public void setResults(List<Route> results) {
        this.results = results;
    }
}
