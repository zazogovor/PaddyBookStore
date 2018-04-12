package com.example.c12437908.fypv2.BusAPI;

import java.util.List;

public class Operator {

    private String name;
    private List<String> routes;

    public Operator() {
    }

    public Operator(String name, List<String> routes) {
        this.name = name;
        this.routes = routes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }

}
