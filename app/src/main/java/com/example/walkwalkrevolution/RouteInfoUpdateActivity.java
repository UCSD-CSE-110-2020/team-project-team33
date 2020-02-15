package com.example.walkwalkrevolution;

import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.TextView;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

public class RouteInfoUpdateActivity implements IDelayedUpdate {


    RouteInfo routeInfo;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;
    private SharedPreferences sharedPreferences;
    public IRouteManagement routesManager;


    public RouteInfoUpdateActivity(RouteInfo routeInfo, WalkInfo walk, int interval) {
        this.routeInfo = routeInfo;
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
        routeInfo.setWalkStepsText(walkInfo.getSteps());
        routeInfo.setWalkStepsText(walkInfo.getDistance());
}
