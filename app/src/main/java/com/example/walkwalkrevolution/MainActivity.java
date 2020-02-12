package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.walkwalkrevolution.fitness.*;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private int userHeight;
    private SharedPreferences sharedPreferences;
    private RoutesManager routesManager;

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

        sharedPreferences = getSharedPreferences(DataKeys.USER_NAME_KEY, MODE_PRIVATE);
        routesManager = new RoutesManager(sharedPreferences);

        userHeight = sharedPreferences.getInt(DataKeys.USER_HEIGHT_KEY, -1);
        if (userHeight == -1) {
            launchHeightActivity();
        } else {
            launchStepCountActivity();
        }
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, userHeight);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        startActivity(intent);
    }

    public void launchHeightActivity() {
        Intent intent = new Intent(this, HeightActivity.class);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}