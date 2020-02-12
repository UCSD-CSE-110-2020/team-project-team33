package com.example.walkwalkrevolution.routemanagement.RouteFeatures;

import java.util.List;

public class Difficulty implements Feature {
    String[] options = {"Difficulty...", "Easy", "Moderate", "Difficult"};
    public Difficulty() {}

    public String[] getOptions() { return options; }
}
