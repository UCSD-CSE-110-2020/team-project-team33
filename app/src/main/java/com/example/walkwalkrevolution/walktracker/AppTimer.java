package com.example.walkwalkrevolution.walktracker;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class AppTimer extends Observable {
    private static final int SECOND_MILLIS = 1000;

    private Timer t;
    private TimerTask updateTimer;

    public AppTimer() {
        updateTimer = new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers();
            }
        };
        t = new Timer();
        t.schedule(updateTimer, 0, SECOND_MILLIS);
    }

    public boolean cancel() {
        return updateTimer.cancel();
    }
}