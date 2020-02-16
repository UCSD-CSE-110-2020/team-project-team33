package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.ui.main.EnterRouteInfoFragment;
import com.example.walkwalkrevolution.ui.main.MockFragment;
import com.example.walkwalkrevolution.ui.main.RouteInfoFragment;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.ui.main.SectionsPagerAdapter;

public class TabActivity extends AppCompatActivity {
    private static final String TAG = "TabActivity";

    private static final int HOME_TAB_INDEX = 0;
    private static final int ROUTES_TAB_INDEX = 1;
    private static final int MOCK_TAB_INDEX = 2;

    private ViewPager viewPager;
    private AppBarLayout appbar;
    private Button btnStartWalk;

    public StepCountFragment stepCountFragment;
    public RoutesFragment routesFragment;
    public MockFragment mockFragment;

    private FrameLayout fragmentContainer;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public IRouteManagement routesManager;

    private WalkInfo walkInfo;

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
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        appbar = findViewById(R.id.appbar);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        fragmentContainer.setVisibility(View.GONE);
        fragmentManager = getSupportFragmentManager();

        btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walkStarted) {
                    tabLayout.getTabAt(ROUTES_TAB_INDEX).select();
                    btnStartWalk.setText(getString(R.string.start_string));
                    stepCountFragment.getWalkUpdate().stop();
                    launchEnterRouteInfo();
                } else {
                    tabLayout.getTabAt(HOME_TAB_INDEX).select();
                    btnStartWalk.setText(getString(R.string.stop_string));
                    stepCountFragment.getWalkUpdate().start();
                }
                walkStarted = !walkStarted;
                mockFragment.setButtons();
            }
        });

    }

    private void toggleViewPagerVisibility() {
        appbar.setVisibility(appbar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        viewPager.setVisibility(viewPager.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        btnStartWalk.setVisibility(btnStartWalk.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        fragmentContainer.setVisibility(fragmentContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public void launchEnterRouteInfo() {
        EnterRouteInfoFragment fragment = new EnterRouteInfoFragment(this, routesManager, walkInfo);
        currentFragment = fragment;
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        toggleViewPagerVisibility();
    }

    public void launchRouteInfo(Route route) {
        RouteInfoFragment fragment = new RouteInfoFragment(this, route);
        currentFragment = fragment;
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        toggleViewPagerVisibility();
    }

    public void deleteFragment(Fragment fragment) {
        fragmentManager.beginTransaction().remove(fragment).commit();
        toggleViewPagerVisibility();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        stepCountFragment = new StepCountFragment(walkInfo);
        adapter.addFragment(stepCountFragment, getString(R.string.home_tab));

        routesFragment = new RoutesFragment(this, routesManager, walkInfo);
        adapter.addFragment(routesFragment, getString(R.string.routes_tab));

        mockFragment = new MockFragment(this, stepCountFragment.getWalkInfo());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            deleteFragment(currentFragment);
            return true;
        }
        return false;
    }
}