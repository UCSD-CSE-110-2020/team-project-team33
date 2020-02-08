package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launchHeightActivity();
    }

    public void launchHeightActivity() {
        Intent intent = new Intent(this, HeightActivity.class);
        startActivity(intent);
    }
}
