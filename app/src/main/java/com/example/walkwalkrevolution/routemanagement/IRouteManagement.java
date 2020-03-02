package com.example.walkwalkrevolution.routemanagement;

import android.content.SharedPreferences;

public interface IRouteManagement {
    public long getRecentSteps(SharedPreferences sharedPreferences);

    public double getRecentDistance(SharedPreferences sharedPreferences);

    public long getRecentTime(SharedPreferences sharedPreferences);

    public void saveRoute(SharedPreferences sharedPreferences, Route route);

    public void saveRecentWalk(SharedPreferences sharedPreferences, Route route);
}
