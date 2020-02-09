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
import com.example.walkwalkrevolution.EnterRouteInfoActivity;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.google.gson.Gson;

import java.io.Serializable;

public class StepCountFragment extends Fragment {

    private static final String TAG = "StepCountFragment";

    private TextView textSteps;
    private TextView overallDist;
    private TextView walkSteps;
    private TextView walkDist;
    private TextView timer;

    private FitnessService fitnessService;
    private long overallSteps;
    private OverallStepCountTask overallStepsTask;
    private WalkStepsTask walkStepsTask;
    private int numPresses = 0;
    private Distance dist;

    private long baseSteps = overallSteps;
    
    private long currentWalkSteps;
    private long currentWalkTime;

    private IRouteManagement routesManager;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        setUserHeight(getActivity().getIntent().getIntExtra(DataKeys.USER_HEIGHT_KEY, 0));
        routesManager = (IRouteManagement) getActivity().getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);
        overallStepsTask = new OverallStepCountTask();
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

        overallStepsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//       If authentication was required during google fit setup, this will be called after the user authenticates
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
    
    private class OverallStepCountTask extends AsyncTask<String, String, String> {
        private String resp = "";

        @Override
        protected void onPreExecute() {
            fitnessService.updateStepCount();
            textSteps.setText(String.valueOf(overallSteps));
            overallDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(overallSteps)));
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                while(!isCancelled()) {
                    Thread.sleep(1000);
                    fitnessService.updateStepCount();
                    publishProgress("");
                }
            } catch (Exception e) {
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            textSteps.setText(String.valueOf(overallSteps));
            overallDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(overallSteps)));
        }
    }

    private class WalkStepsTask extends AsyncTask<String, String, String> {
        private String resp = "";
        private long startTime;

        WalkStepsTask(){
            startTime = System.currentTimeMillis();
        }

        String getTimeElapsed(){
            long currentTime = System.currentTimeMillis();
            currentWalkTime = (currentTime - startTime) / 1000;
            return formatTime(currentWalkTime);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                while (!isCancelled()) {
                    currentWalkSteps = overallSteps - baseSteps;
                    publishProgress("");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            walkSteps.setText(String.valueOf(currentWalkSteps));
            walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(currentWalkSteps)));
            timer.setText(getTimeElapsed());
        }

        @Override
        protected void onPreExecute() {
            walkSteps.setText(String.valueOf(currentWalkSteps));
            walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(currentWalkSteps)));
            timer.setText(getTimeElapsed());
        }

        @Override
        protected void onPostExecute(String result) {
            fitnessService.updateStepCount();
            currentWalkSteps = baseSteps - overallSteps;
            walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(currentWalkSteps)));
        }
    }

    public void startWalkTask() {
        walkStepsTask = new WalkStepsTask();
        baseSteps = overallSteps;
        walkStepsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void stopWalkTask() {
        walkStepsTask.cancel(true);
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
