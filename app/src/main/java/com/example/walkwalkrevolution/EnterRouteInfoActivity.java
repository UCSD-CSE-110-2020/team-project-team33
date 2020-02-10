package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.google.gson.Gson;

public class EnterRouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_route_info);

        Bundle b = getIntent().getExtras();

        double distance = b.getDouble(DataKeys.DISTANCE_KEY);
        long steps = b.getLong(DataKeys.STEPS_KEY);
        long time = b.getLong(DataKeys.TIME_KEY);
        IRouteManagement routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);


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

    }

    @Override
    public void onBackPressed() { }
}
