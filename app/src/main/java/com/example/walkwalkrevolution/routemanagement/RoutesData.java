package com.example.walkwalkrevolution.routemanagement;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class RoutesData implements Serializable, Iterable {
    private TreeSet<Route> routes;

    public RoutesData() {
        routes = new TreeSet<Route>(new compareRoutes());
    }

    public void addRoute(Route route) {
        routes.add(route);
    }

    public int getNumRoutes() {
        return routes.size();
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return routes.iterator();
    }

    private class compareRoutes implements Comparator<Route>, Serializable {
        @Override
        public int compare(Route route, Route t1) {
            return route.getName().compareTo(t1.getName());
        }
    }
}
