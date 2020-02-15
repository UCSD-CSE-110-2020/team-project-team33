package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.ui.main.MockFragment;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
import com.example.walkwalkrevolution.walktracker.StepUpdate;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.example.walkwalkrevolution.walktracker.WalkUpdate;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.ui.main.SectionsPagerAdapter;

import java.io.Serializable;

public class TabActivity extends AppCompatActivity {
    private static final String TAG = "TabActivity";

    private static final int HOME_TAB_INDEX = 0;
    private static final int ROUTES_TAB_INDEX = 1;
    private static final int MOCK_TAB_INDEX = 2;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    public StepCountFragment stepCountFragment;
    public RoutesFragment routesFragment;
    public MockFragment mockFragment;

    public IRouteManagement routesManager;

    private WalkInfo walkInfo;

    private IDelayedUpdate stepCountUpdate;
    private IDelayedUpdate walkUpdate;

    private FitnessService fitnessService;

    private boolean walkStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        String fitnessServiceKey = getIntent().getStringExtra(DataKeys.FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        walkInfo = new WalkInfo(getIntent().getIntExtra(DataKeys.USER_HEIGHT_KEY, 0), fitnessService);


        // Keep these after all initializations
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        final Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walkStarted) {
                    tabLayout.getTabAt(ROUTES_TAB_INDEX).select();
                    btnStartWalk.setText(getString(R.string.start_string));
                    stepCountFragment.getWalkUpdate().stop();
                    launchEnterRouteInfoActivity();
                } else {
                    btnStartWalk.setText(getString(R.string.stop_string));
                    stepCountFragment.getWalkUpdate().start();
                }
                walkStarted = !walkStarted;
                mockFragment.setButtons();
            }
        });

    }

    public void launchEnterRouteInfoActivity() {
        Intent intent = new Intent (this, EnterRouteInfoActivity.class);
        intent.putExtra(DataKeys.DISTANCE_KEY, stepCountFragment.getWalkInfo().getWalkDistance());
        intent.putExtra(DataKeys.STEPS_KEY, stepCountFragment.getWalkInfo().getWalkSteps());
        intent.putExtra(DataKeys.TIME_KEY, stepCountFragment.getWalkInfo().getWalkTime());
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, (Serializable) routesManager);

        startActivity(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        stepCountFragment = new StepCountFragment(walkInfo);
        adapter.addFragment(stepCountFragment, getString(R.string.home_tab));

        routesFragment = new RoutesFragment(walkInfo);
        adapter.addFragment(routesFragment, getString(R.string.routes_tab));

        mockFragment = new MockFragment(stepCountFragment.getWalkInfo(), this);
        adapter.addFragment(mockFragment, getString(R.string.mock_tab));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    public void setStepCount(long stepCount) {
        walkInfo.setSteps(stepCount);
    }

    public boolean isWalkStarted() {
        return walkStarted;
    }

    @Override
    public void onBackPressed() { }
}