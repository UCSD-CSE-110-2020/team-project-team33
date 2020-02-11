package com.example.walkwalkrevolution.walktracker;

import android.os.Handler;

import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class WalkUpdate implements IDelayedUpdate {
    StepCountFragment stepCountFragment;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;

    public WalkUpdate(StepCountFragment stepCountFragment, int interval) {
        this.stepCountFragment = stepCountFragment;

        stepUpdateHandler = new Handler();

        stepUpdateTask = new Runnable() {
            @Override
            public void run() {
                stepCountFragment.updateWalk();
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
}