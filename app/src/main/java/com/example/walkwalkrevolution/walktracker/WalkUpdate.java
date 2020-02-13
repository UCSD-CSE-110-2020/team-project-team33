package com.example.walkwalkrevolution.walktracker;

import android.os.Handler;

import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class WalkUpdate implements IDelayedUpdate {
    TabActivity tabActivity;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;

    public WalkUpdate(TabActivity tabActivity, WalkInfo info, int interval) {
        this.tabActivity = tabActivity;
        this.walkInfo = info;

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
        walkInfo.startWalk();
        stepUpdateTask.run();
    }

    @Override
    public void stop() {
        stepUpdateHandler.removeCallbacks(stepUpdateTask);
    }

    @Override
    public void update() {
        tabActivity.stepCountFragment.setWalkStepsText(walkInfo.getWalkSteps());
        tabActivity.stepCountFragment.setWalkDistanceText(walkInfo.getWalkDistance());
        if(!walkInfo.isMocking()) {
            walkInfo.incrementWalkTime();
        }
        tabActivity.stepCountFragment.setTimerText(walkInfo.getWalkTime());
    }
}