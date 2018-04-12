package com.example.c12437908.fypv2.Entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cinema on 12/18/2017.
 */

public class Carpool implements Serializable{
    private int id;
    private String date;
    private String time;
    private int maxPeople;
    private String origination;
    private String destination;
    private String passengerMarkers;
    private int maxPickupDistance;
    private List<User> passengers;
    private User driver;

    public Carpool(){

    }

    public Carpool(String date, String time, User driver, int maxPeople, String origination, String destination, String passengerMarkers, int maxPickupDistance){
        super();
        this.date = date;
        this.time = time;
        this.driver = driver;
        this.maxPeople = maxPeople;
        this.origination = origination;
        this.destination = destination;
        this.passengerMarkers = passengerMarkers;
        this.maxPickupDistance = maxPickupDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getMaxPickupDistance() {
        return maxPickupDistance;
    }

    public void setMaxPickupDistance(int maxPickupDistance) {
        this.maxPickupDistance = maxPickupDistance;
    }

    public String getPassengerMarkers() {
        return passengerMarkers;
    }

    public void setPassengerMarkers(String passengerMarkers) {
        this.passengerMarkers = passengerMarkers;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public List<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<User> passengers) {
        this.passengers = passengers;
    }


}
