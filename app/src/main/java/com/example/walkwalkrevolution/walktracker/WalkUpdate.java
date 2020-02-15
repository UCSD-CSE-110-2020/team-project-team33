package com.example.walkwalkrevolution.walktracker;

import android.os.Handler;

import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class WalkUpdate implements IDelayedUpdate {
    StepCountFragment stepCountFragment;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;

    boolean running;

    public WalkUpdate(StepCountFragment stepCountFragment, WalkInfo info, int interval) {
        this.stepCountFragment = stepCountFragment;
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
        stepCountFragment.setWalkStepsText(walkInfo.getWalkSteps());
        stepCountFragment.setWalkDistanceText(walkInfo.getWalkDistance());
        if(!walkInfo.isMocking()) {
            walkInfo.incrementWalkTime();
        }
        stepCountFragment.setTimerText(walkInfo.getWalkTime());
    }
}