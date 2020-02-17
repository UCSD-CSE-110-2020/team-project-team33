package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.walkwalkrevolution.fitness.*;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private int userHeight;
    private SharedPreferences sharedPreferences;
    private RoutesManager routesManager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(TabActivity tabActivity) {
                return new GoogleFitAdapter(tabActivity);
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
        Log.i(TAG, "Launching step count activity");
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, userHeight);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        startActivity(intent);
    }

    public void launchHeightActivity() {
        Log.i(TAG, "Launching height activity");
        Intent intent = new Intent(this, HeightActivity.class);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, fitnessServiceKey);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}