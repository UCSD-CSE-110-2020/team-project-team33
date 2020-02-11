package com.example.walkwalkrevolution.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.Distance;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
import com.example.walkwalkrevolution.walktracker.StepUpdate;
import com.example.walkwalkrevolution.walktracker.WalkUpdate;

public class StepCountFragment extends Fragment {

    private static final String TAG = "StepCountFragment";

    private static final int UPDATE_STEPS_INTERVAL = 5000;
    private static final int SECOND_MILLIS = 1000;

    private TextView textSteps;
    private TextView overallDist;
    private TextView walkSteps;
    private TextView walkDist;
    private TextView timer;

    private FitnessService fitnessService;

    private long overallSteps;
    private Distance dist;

    private long currentWalkSteps;
    private long currentWalkTime;

    private long baseSteps;
    private long startTime;

    private IRouteManagement routesManager;
    private SharedPreferences sharedPreferences;

    private boolean mocking;

    IDelayedUpdate stepCountUpdate;
    IDelayedUpdate walkUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        setUserHeight(getActivity().getIntent().getIntExtra(DataKeys.USER_HEIGHT_KEY, 0));
        this.routesManager = (IRouteManagement) getActivity().getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);
        this.mocking = getActivity().getIntent().getBooleanExtra(DataKeys.MOCKING_KEY, false);
        this.sharedPreferences = getActivity().getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE);

        textSteps = view.findViewById(R.id.overall_steps);
        overallDist = view.findViewById(R.id.overall_dist);
        walkSteps = view.findViewById(R.id.walk_steps);
        walkDist = view.findViewById(R.id.walk_dist);
        timer = view.findViewById(R.id.walk_time);

        // Get values for most recent walk and set values to display on screen
        walkSteps.setText(Long.toString(routesManager.getRecentSteps(sharedPreferences)));
        walkDist.setText(getString(R.string.dist_format, routesManager.getRecentDistance(sharedPreferences)));
        timer.setText(formatTime(routesManager.getRecentTime(sharedPreferences)));

        String fitnessServiceKey = getActivity().getIntent().getStringExtra(DataKeys.FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        fitnessService.setup();

        stepCountUpdate = new StepUpdate(this, UPDATE_STEPS_INTERVAL);
        stepCountUpdate.start();
        walkUpdate = new WalkUpdate(this, SECOND_MILLIS);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    public void setStepCount(long stepCount) {
        this.overallSteps = stepCount;
    }

    private void setUserHeight(int height) {
        this.dist = new Distance(height);
    }

    private String formatDigits(int x){
        return x < 10 ? "0" + x : String.valueOf(x);
    }
    
    public String formatTime(long duration) {
        int seconds = (int)((duration / SECOND_MILLIS) % 60);
        int minutes = (int)((duration / (SECOND_MILLIS * 60)) % 60);
        int hours = (int)(duration / (SECOND_MILLIS * 60 * 60)) % 24;
        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);
    }

    public void updateSteps() {
        fitnessService.updateStepCount();
        textSteps.setText(String.valueOf(overallSteps));
        overallDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(overallSteps)));
    }

    public void updateWalk() {
        currentWalkSteps = overallSteps - baseSteps;
        walkSteps.setText(String.valueOf(currentWalkSteps));
        walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(currentWalkSteps)));
        timer.setText(getTimeElapsed());
    }

    public void updateWalk(long time) {
        currentWalkSteps = overallSteps - baseSteps;
        walkSteps.setText(String.valueOf(currentWalkSteps));
        walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(currentWalkSteps)));
        timer.setText(formatTime(time));
    }

    private String getTimeElapsed(){
        long currentTime = System.currentTimeMillis();
        currentWalkTime = (currentTime - startTime);
        return formatTime(currentWalkTime);
    }

    public void startWalkTask() {
        baseSteps = overallSteps;
        startTime = System.currentTimeMillis();
        walkUpdate.start();

    }

    public void stopWalkTask() {
        walkUpdate.stop();
    }

    public long getCurrentWalkSteps() {
        return currentWalkSteps;
    }

    public double getCurrentWalkDistance() {
        return dist.calculateDistance(currentWalkSteps);
    }

    public long getCurrentWalkTime() {
        return currentWalkTime;
    }
}
