package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class EnterRouteInfo extends AppCompatActivity {

    private Walk walk;
    private String name;
    private String startLoc;
    private String distance;
    private String steps;
    private Route route;
    private RoutesManager routesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_route_info);


        Bundle b = getIntent().getExtras();

        // exclude "miles" in string
        distance = b.getString("distance").substring(0, 3);
        steps = b.getString("steps");


        Button saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("routesManager", "");
                routesManager = gson.fromJson(json, RoutesManager.class);


                EditText nameField = (EditText) findViewById(R.id.routeName);
                EditText startField = (EditText) findViewById(R.id.startLoc);
                name = nameField.getText().toString();
                startLoc = startField.getText().toString();

                Walk savedWalk = new Walk(Long.parseLong(steps), Double.parseDouble(distance.substring(0, 3)), 0);
                Route route = new Route(name, startLoc, savedWalk);

                routesManager.addRoute(route);
                // convert routesManager to json object and store in sharedprefs
                SharedPreferences.Editor editor = sharedPreferences.edit();
                json = gson.toJson(routesManager);
                editor.putString("routesManager", json);
                editor.apply();

                Toast.makeText(EnterRouteInfo.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
