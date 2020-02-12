package com.example.walkwalkrevolution.routemanagement.RouteFeatures;

public class Road implements Feature {
    String[] options = {"Street/Trail", "Street", "Trail"};
    public Road() {}

    public String[] getOptions() { return options; }
}
