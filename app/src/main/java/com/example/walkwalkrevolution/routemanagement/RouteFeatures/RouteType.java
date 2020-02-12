package com.example.walkwalkrevolution.routemanagement.RouteFeatures;

public class RouteType implements Feature {
    String[] options = {"Route Type...", "Loop", "Out-And-Back"};
    public RouteType() {}

    public String[] getOptions() { return options; }
}
