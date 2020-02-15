package com.example.walkwalkrevolution.walktracker;

import com.example.walkwalkrevolution.Distance;
import com.example.walkwalkrevolution.fitness.FitnessService;

import java.io.Serializable;

public class WalkInfo implements Serializable {
    private long steps;
    private long startSteps;
    private long walkTime;

    private Distance distance;

    private FitnessService fitnessService;

    private boolean mocking = false;

    public WalkInfo(int height, FitnessService fit) {
        distance = new Distance(height);
        fitnessService = fit;
    }

    public long getSteps() {
        if(!mocking) {
            fitnessService.updateStepCount();
        }
        return steps;
    }

    public void setSteps(long s) {
        steps = s;
    }

    public double getDistance() {
        return distance.calculateDistance(getSteps());
    }

    public void startWalk() {
        startSteps = steps;
        walkTime = 0;
        walkTime = 0;
    }

    public long getWalkSteps() {
        getSteps();
        return steps - startSteps;
    }

    public double getWalkDistance() {
        return distance.calculateDistance(getWalkSteps());
    }

    public long getWalkTime() {
        return walkTime;
    }

    public void setWalkTime(long t) {
        walkTime = t;
    }

    public void incrementWalkTime() {
        ++walkTime;
    }

    public void setMocking(boolean b) { mocking = b; }

    public boolean isMocking() { return mocking; }
}
