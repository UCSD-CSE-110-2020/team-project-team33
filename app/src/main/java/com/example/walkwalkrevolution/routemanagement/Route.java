package com.example.walkwalkrevolution.routemanagement;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable, Comparable<Route> {
    private String name;
    private String startLoc;
    private long steps;
    private double distance;
    private long time;
    private String type;
    private String surface;
    private String road;
    private String difficulty;
    private String terrain;
    private String notes;
    private boolean favorite;

    private boolean walkStarted;

    // Constructor
    public Route(String name, String startLocation, long steps, double distance, long time,
                 String type, String surface, String road, String difficulty, String terrain, String notes) {
        this.name = name;
        this.startLoc = startLocation;
        this.steps = steps;
        this.distance = distance;
        this.time = time;
        this.type = type;
        this.surface = surface;
        this.road = road;
        this.difficulty = difficulty;
        this.terrain = terrain;
        this.notes = notes;
    }

    // Name setter
    public void setName(String name){
        this.name = name;
    }

    // Name getter
    public String getName(){
        return this.name;
    }

    // startLoc setter
    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    // startLoc getter
    public String getStartLoc() {
        return this.startLoc;
    }

    // Steps getter
    public long getSteps(){
        return this.steps;
    }

    // Steps setter
    public void setSteps(long steps){
        this.steps = steps;
    }

    // Distance getter
    public double getDistance(){
        return this.distance;
    }

    // Distance setter
    public void setDistance(double distance){
        this.distance = distance;
    }

    // Time getter
    public long getTime(){
        return this.time;
    }

    // Time setter
    public void setTime(long time){
        this.time = time;
    }

    // favorite setter
    public void setFavorite(boolean fav) {
        this.favorite = fav;
    }

    // favorite getter
    public boolean getFavorite() {
        return this.favorite;
    }

    public String getType() {
        return type;
    }

    public String getSurface() {
        return surface;
    }

    public String getRoad() {
        return road;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTerrain() {
        return terrain;
    }

    public String getNotes() { return notes; }

    public void setWalkStarted(boolean b) {
        walkStarted = b;
    }

    public boolean isWalkStarted() {
        return walkStarted;
    }

    @Override
    public int compareTo(Route route) {
        return this.getName().compareTo(route.getName());
    }
}
