package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;

import java.io.Serializable;

public class HeightActivity extends AppCompatActivity {

    private String heightFeet;
    private String heightInches;
    private IRouteManagement routesManager;

    int totalHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        // find dropdown menus
        Spinner ftOptions = findViewById(R.id.ftOptions);
        Spinner inOptions = findViewById(R.id.inOptions);

        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        // declare options
        String[] feet = new String[]{"0","1", "2", "3", "4", "5", "6", "7"};
        String[] inches = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};

        ArrayAdapter<String> adaptFt = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, feet);

        ArrayAdapter<String> adaptIn = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, inches);

        ftOptions.setAdapter(adaptFt);
        inOptions.setAdapter(adaptIn);

        ftOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                heightFeet = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });

        inOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id)
            {
                heightInches = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
            }
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
                launchStepCountActivity();
            }
        });


    }

    public void save(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences(DataKeys.USER_NAME_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        totalHeight = (Integer.parseInt(heightFeet) * 12) + Integer.parseInt(heightInches);

        editor.putInt(DataKeys.USER_HEIGHT_KEY, totalHeight);

        editor.apply();
        Toast.makeText(HeightActivity.this, getString(R.string.saved_string), Toast.LENGTH_LONG).show();
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, MainActivity.fitnessServiceKey);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, totalHeight);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, (Serializable) routesManager);
        startActivity(intent);
    }
}
