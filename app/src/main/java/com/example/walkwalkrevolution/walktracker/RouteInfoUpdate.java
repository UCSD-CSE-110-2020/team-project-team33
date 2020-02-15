package com.example.walkwalkrevolution.walktracker;

import android.content.SharedPreferences;
import android.os.Handler;

import com.example.walkwalkrevolution.RouteInfoActivity;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;

public class RouteInfoUpdate implements IDelayedUpdate {


    RouteInfoActivity routeInfoActivity;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;
    private SharedPreferences sharedPreferences;
    public IRouteManagement routesManager;


    public RouteInfoUpdate(RouteInfoActivity routeInfoActivity, WalkInfo walk, int interval) {
        this.routeInfoActivity = routeInfoActivity;
        walkInfo = walk;

        stepUpdateHandler = new Handler();
        stepUpdateTask = new Runnable() {
            @Override
            public void run() {
                update();
                stepUpdateHandler.postDelayed(stepUpdateTask, interval);
            }
        };

    }

    @Override
    public void start() {
        stepUpdateTask.run();
    }

    @Override
    public void stop() {
        stepUpdateHandler.removeCallbacks(stepUpdateTask);
    }

    @Override
    public void update() {
        routeInfoActivity.setWalkStepsText(walkInfo.getSteps());
        routeInfoActivity.setWalkDistanceText(walkInfo.getDistance());
    }
}
