package com.example.walkwalkrevolution.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;

public class StepCountFragment extends Fragment {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountFragment";

    private TextView textSteps;
    private TextView walkSteps;
    private FitnessService fitnessService;
    private long overallSteps;
    private OverallStepCountTask overallStepsTask = new OverallStepCountTask();
    private WalkStepsTask walkStepsTask;
    private int numPresses = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_count, container, false);

        textSteps = view.findViewById(R.id.overall_steps);
        walkSteps = view.findViewById(R.id.walk_steps);

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
        }
    }

    private class WalkStepsTask extends AsyncTask<String, String, String> {
        private String resp = "";
        int i = 0;
        long baseSteps = overallSteps;

        @Override
        protected String doInBackground(String... params) {
            try {
                while (!isCancelled()) {
                    Thread.sleep(1000);
                    publishProgress(String.valueOf(overallSteps-baseSteps));
                    i++;
                }
            } catch (Exception e) {
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            walkSteps.setText(text[0]);
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
            return getString(R.string.start_string);
        }
    }
}
