package com.example.walkwalkrevolution.routemanagement.RouteFeatures;

public class Terrain implements Feature {
    String[] options = {"Terrain...", "Flat", "Hilly"};
    public Terrain() {}

    public String[] getOptions() { return options; }
}
