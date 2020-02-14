package com.example.walkwalkrevolution;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Locale;

public class DisplayRouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_route_info);
        setSupportActionBar(findViewById(R.id.walk_title));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //somehow get the route number from routes page
        try {
            Route route = new Route("RouteName", "StartLoc", 100,
                    5.5,  62000);
            String routeName = route.getName();
            String time = StepCountFragment.formatTime(route.getTime());
            long steps = route.getSteps();
            double distance = route.getDistance();

            TextView stepCount = findViewById(R.id.step_count);
            TextView nameText = findViewById(R.id.info_walk_name);
            ImageButton favoriteButton = findViewById(R.id.favorite);
            TextView distanceText = findViewById(R.id.distance_value);
            TextView timeText = findViewById(R.id.time_value);

            initFavoriteButton(favoriteButton, route);

            favoriteButton.setOnClickListener( v ->{
                route.toggleFavorite();
                initFavoriteButton(favoriteButton, route);
            });

            stepCount.setText(Long.toString(steps));
            nameText.setText(routeName);
            distanceText.setText(Double.toString(distance));
            timeText.setText(time);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_saved_activity, menu);
        return true;
    }


    private void initFavoriteButton(ImageButton favoriteButton, Route route){
        if(route.getFavorite()){
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
        }else{
            favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

}
