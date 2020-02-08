package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterRouteInfo extends AppCompatActivity {

    private Walk walk;
    private String name;
    private String startLoc;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_route_info);

        Button saveBtn = findViewById(R.id.saveButton);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = (EditText) findViewById(R.id.routeName);
                EditText startField = (EditText) findViewById(R.id.startLoc);
                name = nameField.getText().toString();
                startLoc = startField.getText().toString();
                //route = new Route(this.name, this.startLoc, this.walk);

                Bundle b = getIntent().getExtras();
                String typesofbook = b.getString("booktype");


            }
        });


    }
}
