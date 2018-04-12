package com.example.c12437908.fypv2.BusAPI;

import java.io.Serializable;

/**
 * Created by cinema on 3/7/2018.
 */

public class TimeTableForADay implements Serializable{
    String route;
    String departure_time;
    String destination;
    String destination_localized;

    public TimeTableForADay(String route, String departure_time, String destination, String destination_localized) {
        this.route = route;
        this.departure_time = departure_time;
        this.destination = destination;
        this.destination_localized = destination_localized;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination_localized() {
        return destination_localized;
    }

    public void setDestination_localized(String destination_localized) {
        this.destination_localized = destination_localized;
    }
}
