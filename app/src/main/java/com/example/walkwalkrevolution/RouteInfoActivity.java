package com.example.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
import com.example.walkwalkrevolution.walktracker.StepUpdate;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.example.walkwalkrevolution.walktracker.WalkUpdate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class RouteInfoActivity extends AppCompatActivity implements IDelayedUpdate {


    private TextView route_info_steps;
    private TextView route_info_time;
    private TextView route_info_dist;


    StepCountFragment stepCountFragment;
    WalkInfo walkInfo;
    Handler stepUpdateHandler;
    Runnable stepUpdateTask;


    private SharedPreferences sharedPreferences;
    public IRouteManagement routesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         route_info_steps = findViewById(R.id.route_steps_value);
         route_info_time = findViewById(R.id.route_time_value);
         route_info_dist = findViewById(R.id.route_dist_value);

        Button startBttn = findViewById(R.id.route_info_bttn);
        startBttn.setOnClickListener(new View.OnClickListener() {
            boolean walkStarted = false;

            @Override
            public void onClick(View view) {
                if(!walkStarted) {
                    //reset everything to 0.
                    setWalkDistanceText(0);
                    setWalkStepsText(0);
                    setTimerText(0);
                    startBttn.setText("Stop walk/run");

                    //start updating



                }else{
                    startBttn.setText("Start new walk/run");

                }

                walkStarted = !walkStarted;

            }
        });
    }



    public void setWalkStepsText(long steps) {
        route_info_steps.setText(Long.toString(steps));
    }

    public void setWalkDistanceText(double distance) {
        route_info_dist.setText(String.format(getString(R.string.dist_format), distance));
    }

    public void setTimerText(long time) {
        route_info_time.setText(formatTime(time));
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


}
