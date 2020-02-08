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

import com.example.walkwalkrevolution.Distance;
import com.example.walkwalkrevolution.EnterRouteInfo;
import com.example.walkwalkrevolution.HeightActivity;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;

public class StepCountFragment extends Fragment {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountFragment";

    private TextView textSteps;
    private TextView overallDist;
    private TextView walkSteps;
    private TextView walkDist;

    private FitnessService fitnessService;
    private long overallSteps;
    private OverallStepCountTask overallStepsTask = new OverallStepCountTask();
    private WalkStepsTask walkStepsTask;
    private int numPresses = 0;
    private static int userHeight;  // have to set this later
    private Distance dist = new Distance(userHeight);

    private long baseSteps = overallSteps;
    private long mySteps = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user_name", Context.MODE_PRIVATE);
        userHeight = sharedPreferences.getInt("height", -1);

        textSteps = view.findViewById(R.id.overall_steps);
        overallDist = view.findViewById(R.id.overall_dist);
        walkSteps = view.findViewById(R.id.walk_steps);
        walkDist = view.findViewById(R.id.walk_dist);

        String fitnessServiceKey = getActivity().getIntent().getStringExtra(FITNESS_SERVICE_KEY);
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

    private class OverallStepCountTask extends AsyncTask<String, String, String> {
        private String resp = "";

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


        @Override
        protected String doInBackground(String... params) {
            try {
                while (!isCancelled()) {
                    mySteps = overallSteps - baseSteps;
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
            walkSteps.setText(String.valueOf(mySteps));
            walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(mySteps)));
        }
    }

    public String startButtonBehavior() {
        if (numPresses == 0) {
            walkStepsTask = new WalkStepsTask();
            walkStepsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            numPresses++;
            return getString(R.string.stop_string);
        }
        else {
            walkStepsTask.cancel(true);
            numPresses--;
            fitnessService.updateStepCount();
            mySteps = baseSteps - overallSteps;
            walkDist.setText(String.format(getString(R.string.dist_format), dist.calculateDistance(mySteps)));
            launchEnterRouteInfoActivity();
            return getString(R.string.start_string);
        }
    }

    public void launchEnterRouteInfoActivity() {

        Intent intent = new Intent (this.getActivity(), EnterRouteInfo.class);
        String distance = walkDist.getText().toString();
        String steps = walkSteps.getText().toString();

        intent.putExtra("distance", distance);
        intent.putExtra("steps", steps);


        startActivity(intent);
    }

}
