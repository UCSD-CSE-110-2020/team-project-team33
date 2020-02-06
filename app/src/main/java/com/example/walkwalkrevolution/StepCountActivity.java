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
    private FitnessService fitnessService;
    private TextView count;
    private StepCount stepCounter = new StepCount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);

        // Set id of text where step count should go.
        textSteps = findViewById(R.id.textSteps);

        count = findViewById(R.id.counter);

        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);



        Button btnUpdateSteps = findViewById(R.id.buttonUpdateSteps);
        btnUpdateSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepCounter.cancel(true);
            }
        });

        fitnessService.setup();

        stepCounter.execute();

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
        textSteps.setText(String.valueOf(stepCount));
    }

    private class StepCount extends AsyncTask<String, String, String> {
        private String resp = "";
        int i = 0;

        @Override
        protected String doInBackground(String... params) {

            try{
                while(i > -1) {
                    i++;
                    Thread.sleep(1000);
                    publishProgress(String.valueOf(i));
                    if(isCancelled()) break;
                }
            } catch (Exception e) {
                resp = e.getMessage();
            }
            return resp;
        }
        @Override
        protected void onProgressUpdate(String... text) {
            count.setText(text[0]);
        }
    }
}
