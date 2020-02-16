package com.example.walkwalkrevolution;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Spliterator;

public class MockRoutesManager extends Observable implements IRouteManagement, Serializable, Iterable {
    public long recentSteps;
    public double recentDistance;
    public long recentTime;
    public String recentName;
    public String recentStart;
    ArrayList<Route> routes = new ArrayList<>();

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
        routes.add(route);
        setChanged();
        notifyObservers(iterator());
    }

    @NonNull
    @Override
    public Iterator<Route> iterator() {
        return iterator();
    }

    @Override
    public Spliterator<Route> spliterator() {
        return routes.spliterator();
    }
}
