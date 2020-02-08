package com.example.walkwalkrevolution;

import java.util.ArrayList;

public class RoutesManager {
    private ArrayList<Route> routes;

    public RoutesManager() {
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
}
