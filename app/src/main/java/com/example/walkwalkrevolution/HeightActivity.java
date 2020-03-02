package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;

import java.io.Serializable;

public class HeightActivity extends AppCompatActivity {

    private IRouteManagement routesManager;

    int totalHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        // find dropdown menus
        NumberPicker ftOptions = findViewById(R.id.foot_picker);
        NumberPicker inOptions = findViewById(R.id.inch_picker);

        ftOptions.setMinValue(0);
        ftOptions.setMaxValue(9);
        ftOptions.setValue(6);
        ftOptions.setWrapSelectorWheel(false);
        inOptions.setMaxValue(0);
        inOptions.setMaxValue(11);
        inOptions.setWrapSelectorWheel(false);


        Button saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view, ftOptions.getValue(), inOptions.getValue());
                launchStepCountActivity();
            }
        });
    }

    public void save(View view, int feet, int inches) {

        SharedPreferences sharedPreferences = getSharedPreferences(DataKeys.USER_NAME_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        totalHeight = (feet * 12) + inches;

        editor.putInt(DataKeys.USER_HEIGHT_KEY, totalHeight);

        editor.apply();
        Toast.makeText(HeightActivity.this, getString(R.string.saved_string), Toast.LENGTH_LONG).show();
    }

    public void launchStepCountActivity() {
        Intent intent = new Intent(this, TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, getIntent().getStringExtra(DataKeys.FITNESS_SERVICE_KEY));
        intent.putExtra(DataKeys.ACCOUNT_KEY, getIntent().getStringExtra(DataKeys.ACCOUNT_KEY));
        intent.putExtra(DataKeys.CLOUD_KEY, getIntent().getStringExtra(DataKeys.CLOUD_KEY));
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, totalHeight);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, (Serializable) routesManager);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { }
}
