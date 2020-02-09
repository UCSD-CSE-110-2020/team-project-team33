package com.example.walkwalkrevolution.routemanagement;

import java.io.Serializable;
import java.util.ArrayList;

public class RoutesData implements Serializable {
    private ArrayList<Route> routes;

    public RoutesData() {
        routes = new ArrayList<Route>();
    }

    public Route getRoute(int index) {
        return routes.get(index);
    }
    public void addRoute(Route route) {
        routes.add(route);
        /*
        String name = route.getName();
        int low = 0;
        int high = routes.size() - 1;
        int mid = 0;
        while(low < high){
            mid = (low + high) / 2;
            String midName = routes.get(mid).getName();
            if (midName.compareToIgnoreCase(name) < 0){
                low = mid + 1;
                continue;
            }
            if(midName.compareToIgnoreCase(name) > 0){
                high = mid - 1;
                continue;
            }
            break;
        }
        routes.add(mid, route);
         */
    }

    public int getNumRoutes() {
        return routes.size();
    }

    public boolean hasRouteName(String name){
        for(Route route: routes){
            if (route.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}
