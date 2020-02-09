package com.example.walkwalkrevolution;

public class Walk {
    private long steps;
    private double distance;
    private String time;

    // Constructor
    public Walk (long steps, double distance, String time){
        this.steps = steps;
        this.distance = distance;
        this.time = time;
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
    public String getTime(){
        return this.time;
    }

    // Time setter
    public void setTime(String time){
        this.time = time;
    }

}