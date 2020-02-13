package com.example.walkwalkrevolution.walktracker;

import android.os.Handler;

import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class StepUpdate implements IDelayedUpdate {
    TabActivity tabActivity;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;

    public StepUpdate(TabActivity tabActivity, WalkInfo walk, int interval) {
        this.tabActivity = tabActivity;
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
        tabActivity.stepCountFragment.setDailyStepsText(walkInfo.getSteps());
        tabActivity.stepCountFragment.setDailyDistanceText(walkInfo.getDistance());
    }
}