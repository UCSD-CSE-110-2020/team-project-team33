package com.example.walkwalkrevolution;

import com.example.walkwalkrevolution.fitness.FitnessService;

public class MockFitnessService implements FitnessService {
    public static long nextStepCount;

    private static final String TAG = "[MockFitnessService]: ";
    private TabActivity tabActivity;

    public MockFitnessService(TabActivity tabActivity) {
        this.tabActivity = tabActivity;
    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    public void setup() {
        System.out.println(TAG + "setup");
    }

    @Override
    public void updateStepCount() {
        System.out.println(TAG + "updateStepCount");
        tabActivity.setStepCount(nextStepCount);
    }
}
