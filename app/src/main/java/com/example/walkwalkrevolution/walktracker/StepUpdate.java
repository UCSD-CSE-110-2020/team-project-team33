package com.example.walkwalkrevolution.walktracker;

import android.os.Handler;

import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class StepUpdate implements IDelayedUpdate {
    StepCountFragment stepCountFragment;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;

    boolean running;

    public StepUpdate(StepCountFragment stepCountFragment, WalkInfo walk, int interval) {
        this.stepCountFragment = stepCountFragment;
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
        if(!running) {
            running = true;
            stepUpdateTask.run();
        }
    }

    @Override
    public void stop() {
        if(running) {
            running = false;
            stepUpdateHandler.removeCallbacks(stepUpdateTask);
        }
    }

    @Override
    public void update() {
        stepCountFragment.setDailyStepsText(walkInfo.getSteps());
        stepCountFragment.setDailyDistanceText(walkInfo.getDistance());
    }
}