package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.io.Serializable;

public class MockRoutesManager implements IRouteManagement, Serializable {
    public long recentSteps;
    public double recentDistance;
    public long recentTime;
    public String recentName;
    public String recentStart;

    @Override
    public long getRecentSteps(SharedPreferences sharedPreferences) {
        return recentSteps;
    }

    @Override
    public double getRecentDistance(SharedPreferences sharedPreferences) {
        return recentDistance;
    }

    @Override
    public long getRecentTime(SharedPreferences sharedPreferences) {
        return recentTime;
    }

    @Override
    public void saveRoute(SharedPreferences sharedPreferences, Route route) {
        recentSteps = route.getSteps();
        recentDistance = route.getDistance();
        recentTime = route.getTime();
        recentName = route.getName();
        recentStart = route.getStartLoc();
    }
}
