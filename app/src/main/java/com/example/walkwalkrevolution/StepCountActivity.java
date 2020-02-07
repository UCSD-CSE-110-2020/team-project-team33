package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;

public class StepCountActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";

    private static final String TAG = "StepCountActivity";

    private TextView textSteps;
    private TextView walkSteps;
    private FitnessService fitnessService;
    private TextView count;
    private long overallSteps;
    private OverallStepCountTask overallStepsTask = new OverallStepCountTask();
    private WalkStepsTask walkStepsTask;
    private int numPresses = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        textSteps = findViewById(R.id.overall_steps);
        walkSteps = findViewById(R.id.walk_steps);

        count = findViewById(R.id.counter);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        fitnessService.setup();

        overallStepsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        final Button btnStartWalk = findViewById(R.id.startWalk_Button);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numPresses == 0) {
                    walkStepsTask = new WalkStepsTask();
                    walkStepsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    btnStartWalk.setText("Stop walk/run");
                    numPresses++;
                }
                else {
                    walkStepsTask.cancel(true);
                    btnStartWalk.setText("Start new walk/run");
                    numPresses--;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            count.setText(String.valueOf(i));
            System.out.println("published progress");
        }
    }
}
