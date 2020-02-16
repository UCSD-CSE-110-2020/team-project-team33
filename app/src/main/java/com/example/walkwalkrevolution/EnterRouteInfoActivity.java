package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.RouteFeatures;

import java.util.ArrayList;
import java.util.List;

public class EnterRouteInfoActivity extends AppCompatActivity {
    IRouteManagement routesManager;
    RouteFeatures routeFeatures = new RouteFeatures();
    String[] features = new String[Constants.NUM_FEATURES];
    boolean isFavorited = false;

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

                Route route = new Route(name, startLoc, steps, distance, time, getFeatures());
                route.setFavorite(isFavorited);
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

        Spinner[] spinners = { difficulty, road, terrain, surface, routeType };

        for(int i = 0; i < spinners.length; i++) {
            setSpinnerOptions(spinners[i], i);
            setSpinnerSelect(spinners[i], i);
        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.favoriteBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavorited = true;
                } else {
                    isFavorited = false;
                }
            }
        });
    }

    private void setSpinnerSelect(Spinner spinner, int index) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0) {
                    setSpinnerHint(selectedItemView);
                    features[index] = null;
                } else {
                    features[index] = selectedItemView.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // default stub
            }

        });
    }


    private void setSpinnerOptions(Spinner spinner, int index) {

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item, routeFeatures.getFeature(index)){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(position == 0){
                    // Set the hint text color gray italic
                    setSpinnerHint(view);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void setSpinnerHint(View view) {
        // set gray italics for hints
        TextView tv = (TextView) view;
        tv.setTextColor(Color.GRAY);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
    }

    private List<String> getFeatures() {
        List<String> tags = new ArrayList<String>();
        for(String s : features) {
            if(s != null) { tags.add(s); }
        }
        return tags;

    }



    @Override
    public void onBackPressed() { }

}
