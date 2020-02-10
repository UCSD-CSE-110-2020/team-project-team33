package com.example.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.ui.main.SectionsPagerAdapter;

import java.io.Serializable;

public class TabActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    public StepCountFragment stepCountFragment;
    public RoutesFragment routesFragment;

    public IRouteManagement routesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        final Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            boolean walkStarted = false;

            @Override
            public void onClick(View v) {
                if(walkStarted) {
                    tabLayout.getTabAt(1).select();
                    btnStartWalk.setText(getString(R.string.start_string));
                    stepCountFragment.stopWalkTask();
                    launchEnterRouteInfoActivity();
                } else {
                    btnStartWalk.setText(getString(R.string.stop_string));
                    stepCountFragment.startWalkTask();
                }
                walkStarted = !walkStarted;
            }
        });

    }

    public void launchEnterRouteInfoActivity() {
        Intent intent = new Intent (this, EnterRouteInfoActivity.class);
        intent.putExtra(DataKeys.DISTANCE_KEY, stepCountFragment.getCurrentWalkDistance());
        intent.putExtra(DataKeys.STEPS_KEY, stepCountFragment.getCurrentWalkSteps());
        intent.putExtra(DataKeys.TIME_KEY, stepCountFragment.getCurrentWalkTime());
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, (Serializable) routesManager);

        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        stepCountFragment = new StepCountFragment();
        adapter.addFragment(stepCountFragment, getString(R.string.home_tab));
        routesFragment = new RoutesFragment();
        adapter.addFragment(routesFragment, getString(R.string.routes_tab));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() { }
}