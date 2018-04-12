package com.example.c12437908.fypv2.BusAPI;

/**
 * Created by cinema on 2/20/2018.
 */

public class Route {

    private String arrivaldatetime;
    private String destination;
    private String destinationlocalized;
    private String operator;
    private String floorstatus;
    private String route;

    public Route(){

    }

    public Route(String arrivaldatetime, String destination, String destinationlocalized, String operator, String floorstatus, String route) {
        this.arrivaldatetime = arrivaldatetime;
        this.destination = destination;
        this.destinationlocalized = destinationlocalized;
        this.operator = operator;
        this.floorstatus = floorstatus;
        this.route = route;
    }

    public String getArrivaldatetime() {
        return arrivaldatetime;
    }

    public void setArrivaldatetime(String arrivaldatetime) {
        this.arrivaldatetime = arrivaldatetime;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFloorstatus() {
        return floorstatus;
    }

    public void setFloorstatus(String floorstatus) {
        this.floorstatus = floorstatus;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
