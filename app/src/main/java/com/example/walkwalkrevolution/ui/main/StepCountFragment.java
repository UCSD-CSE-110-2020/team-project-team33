package com.example.walkwalkrevolution.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkwalkrevolution.Constants;
import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
import com.example.walkwalkrevolution.walktracker.StepUpdate;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.example.walkwalkrevolution.walktracker.WalkUpdate;

import java.util.Observable;
import java.util.Observer;

public class StepCountFragment extends Fragment implements Observer {
    public static final String TAG = "StepCountFragment";

    private TextView dailyStepsText;
    private TextView dailyDistanceText;
    private TextView walkStepsText;
    private TextView walkDistanceText;
    private TextView timerText;

    private IRouteManagement routesManager;
    private SharedPreferences sharedPreferences;

    WalkInfo walkInfo;

    IDelayedUpdate stepUpdate;
    IDelayedUpdate walkUpdate;

    public StepCountFragment(WalkInfo w) {
        walkInfo = w;
        stepUpdate = new StepUpdate(this, w, Constants.UPDATE_STEPS_INTERVAL);
        walkUpdate = new WalkUpdate(this, w, Constants.SECOND_MILLIS);
        stepUpdate.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        routesManager = (IRouteManagement) getActivity().getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);
        sharedPreferences = getActivity().getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE);

        dailyStepsText = view.findViewById(R.id.overall_steps);
        dailyDistanceText = view.findViewById(R.id.overall_dist);
        walkStepsText = view.findViewById(R.id.walk_steps);
        walkDistanceText = view.findViewById(R.id.walk_dist);
        timerText = view.findViewById(R.id.walk_time);

        // Get values for most recent walk and set values to display on screen
        setDailyStepsText(walkInfo.getSteps());
        setDailyDistanceText(walkInfo.getDistance());
        setWalkStepsText(routesManager.getRecentSteps(sharedPreferences));
        setWalkDistanceText(routesManager.getRecentDistance(sharedPreferences));
        setTimerText(routesManager.getRecentTime(sharedPreferences));

        return view;
    }

    public void setDailyStepsText(long steps) {
        dailyStepsText.setText(Long.toString(steps));
    }

    public void setDailyDistanceText(double distance) {
        dailyDistanceText.setText(String.format(getString(R.string.dist_format), distance));
    }

    public void setWalkStepsText(long steps) {
        walkStepsText.setText(Long.toString(steps));
    }

    public void setWalkDistanceText(double distance) {
        walkDistanceText.setText(String.format(getString(R.string.dist_format), distance));
    }

    public void setTimerText(long time) {
        timerText.setText(formatTime(time));
    }

    public String formatTime(long duration) {
        int seconds = (int)(duration % 60);
        int minutes = (int)((duration / 60) % 60);
        int hours = (int)(duration / (60 * 60)) % 24;
        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);
    }

    private String formatDigits(int x){
        return x < 10 ? "0" + x : String.valueOf(x);
    }

    public IDelayedUpdate getStepUpdate() {
        return stepUpdate;
    }

    public IDelayedUpdate getWalkUpdate() {
        return walkUpdate;
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.d(TAG, "WalkInfo changed, updating...");
        setDailyStepsText(walkInfo.getSteps());
        setDailyDistanceText(walkInfo.getDistance());
        setWalkStepsText(walkInfo.getWalkSteps());
        setWalkDistanceText(walkInfo.getWalkSteps());
        setTimerText(walkInfo.getWalkTime());
    }
}
