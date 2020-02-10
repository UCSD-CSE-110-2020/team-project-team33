package com.example.walkwalkrevolution;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

public class SavedWalkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_route);

        Toolbar toolbar = findViewById(R.id.walk_title);
        toolbar.showOverflowMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_saved_activity, menu);
        return true;
    }

    @Override
    public void onBackPressed() { }
}
