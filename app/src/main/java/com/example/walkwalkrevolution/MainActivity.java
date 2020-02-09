package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.walkwalkrevolution.fitness.*;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class MainActivity extends AppCompatActivity {
    public static String fitnessServiceKey = "GOOGLE_FIT";

    private int userHeight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(StepCountFragment stepCountFragment) {
                return new GoogleFitAdapter(stepCountFragment);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);

        userHeight = sharedPreferences.getInt("height", -1);
        if (userHeight == -1) {
            launchHeightActivity();
        } else {
            launchStepCountActivity();
        }
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(TabActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        intent.putExtra(TabActivity.USER_HEIGHT, userHeight);
        startActivity(intent);
    }

    public void launchHeightActivity() {
        Intent intent = new Intent(this, HeightActivity.class);
        startActivity(intent);
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    public void setUserHeight(int height) {
        this.userHeight = height;
    }
}