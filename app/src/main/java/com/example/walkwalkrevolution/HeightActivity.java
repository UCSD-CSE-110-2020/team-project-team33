package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HeightActivity extends AppCompatActivity {

    private String heightFeet;
    private String heightInches;
    private boolean getInches;
    private boolean getFeet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        // find dropdown menus
        Spinner ftOptions = findViewById(R.id.ftOptions);
        Spinner inOptions = findViewById(R.id.inOptions);


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


    }

}
