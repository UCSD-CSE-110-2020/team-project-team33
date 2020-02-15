package com.example.walkwalkrevolution.routemanagement.RouteFeatures;
import  com.example.walkwalkrevolution.DataKeys;


public final class RouteFeatures {
    String[] difficultyOptions = {"Select a difficulty...", "Easy", "Moderate", "Hard"};
    String[] roadOptions = {"Select Street/Trail...", "Street", "Trail"};
    String[] routeOptions =  {"Select a Route Type...", "Loop", "Out-And-Back"};
    String[] surfaceOptions = {"Select Even/Uneven...", "Even", "Uneven"};
    String[] terrainOptions = {"Select a Terrain...", "Flat", "Hilly"};

    String[][] options = {difficultyOptions, roadOptions, terrainOptions, surfaceOptions, routeOptions};

    public String[] getFeature(int index) {
        return options[index];
    }
}
