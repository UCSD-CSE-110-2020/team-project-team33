package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class EnterRouteInfo extends AppCompatActivity {

    private Walk walk;
    private String name;
    private String startLoc;
    private double distance;
    private long steps;
    private Route route;
    private RoutesManager routesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_route_info);


        Bundle b = getIntent().getExtras();

        distance = b.getDouble("DISTANCE");
        steps = b.getLong("STEPS");


        Button saveBtn = findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("routesManager", "");
                routesManager = gson.fromJson(json, RoutesManager.class);
                 */


                EditText nameField = (EditText) findViewById(R.id.routeName);
                EditText startField = (EditText) findViewById(R.id.startLoc);
                name = nameField.getText().toString();
                if(name.trim().length()== 0){
                    Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                startLoc = startField.getText().toString();

                Walk savedWalk = new Walk(steps, distance, 0);
                Route route = new Route(name, startLoc, savedWalk);

                /*
                routesManager.addRoute(route);
                // convert routesManager to json object and store in sharedprefs
                SharedPreferences.Editor editor = sharedPreferences.edit();
                json = gson.toJson(routesManager);
                editor.putString("routesManager", json);
                editor.apply();
                 */

                Toast.makeText(EnterRouteInfo.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
