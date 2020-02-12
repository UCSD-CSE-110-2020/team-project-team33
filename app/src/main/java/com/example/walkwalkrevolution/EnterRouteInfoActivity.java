package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.Difficulty;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.Feature;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.Road;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.RouteType;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.Surface;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.Terrain;
import com.google.gson.Gson;

public class EnterRouteInfoActivity extends AppCompatActivity {
    IRouteManagement routesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_route_info);

        Bundle b = getIntent().getExtras();

        double distance = b.getDouble(DataKeys.DISTANCE_KEY);
        long steps = b.getLong(DataKeys.STEPS_KEY);
        long time = b.getLong(DataKeys.TIME_KEY);
        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);


        Button saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText nameField = (EditText) findViewById(R.id.routeName);
                EditText startField = (EditText) findViewById(R.id.startLoc);
                String name = nameField.getText().toString();
                if(name.trim().length() == 0){
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_name_err_string), Toast.LENGTH_LONG).show();
                    return;
                }
                String startLoc = startField.getText().toString();

                Route route = new Route(name, startLoc, steps, distance, time);
                routesManager.saveRoute(getSharedPreferences(DataKeys.USER_NAME_KEY, MODE_PRIVATE), route);

                Toast.makeText(EnterRouteInfoActivity.this, getString(R.string.saved_string), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        Spinner difficulty = findViewById(R.id.difficultyType);
        Spinner road = findViewById(R.id.roadType);
        Spinner terrain = findViewById(R.id.terrainType);
        Spinner surface = findViewById(R.id.surfaceType);
        Spinner routeType = findViewById(R.id.routeType);

        Feature[] features = { new Difficulty(), new Road(), new Terrain(), new Surface(), new RouteType() };
        Spinner[] spinners = { difficulty, road, terrain, surface, routeType };

        for(int i = 0; i < features.length; i++) {
            setOptions(spinners[i], features[i]);
        }


    }


    private void setOptions(Spinner spinner, Feature feature) {
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, feature.getOptions());
        menu.setAdapter(adapter);*/

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item,feature.getOptions()){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }



    @Override
    public void onBackPressed() { }
}
