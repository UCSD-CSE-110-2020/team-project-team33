package com.example.walkwalkrevolution.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.walkwalkrevolution.walktracker.AppTimer;

import java.util.Observable;
import java.util.Observer;

public class StepCountFragment extends Fragment implements Observer {

    private static final String TAG = "StepCountFragment";

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
    private AppTimer stepUpdateTimer;
    private AppTimer currentWalkTimer;

    private boolean mocking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        setUserHeight(getActivity().getIntent().getIntExtra(DataKeys.USER_HEIGHT_KEY, 0));
        routesManager = (IRouteManagement) getActivity().getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);
        mocking = getActivity().getIntent().getBooleanExtra(DataKeys.MOCKING_KEY, false);
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

        if(!mocking) {
            stepUpdateTimer = new AppTimer();
            stepUpdateTimer.addObserver(this);
        }

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
    
    private String formatTime(long duration) {
        int hours = (int)(duration / 3600);
        int minutes = (int)((duration % 3600) / 60);
        int seconds = (int)(duration % 60);
        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable == stepUpdateTimer) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateSteps();
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateWalk();
                }
            });
        }
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

    private String getTimeElapsed(){
        long currentTime = System.currentTimeMillis();
        currentWalkTime = (currentTime - startTime) / 1000;
        return formatTime(currentWalkTime);
    }

    public void startWalkTask() {
        if(!mocking) {
            currentWalkTimer = new AppTimer();
            baseSteps = overallSteps;
            startTime = System.currentTimeMillis();
            currentWalkTimer.addObserver(this);
        }
    }

    public void stopWalkTask() {
        if(!mocking) {
            currentWalkTimer.deleteObservers();
            currentWalkTimer.cancel();
            currentWalkTimer = null;
        }
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
