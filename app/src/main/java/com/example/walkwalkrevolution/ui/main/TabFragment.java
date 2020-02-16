package com.example.walkwalkrevolution.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class TabFragment extends Fragment {
    public static final String TAG = "TabFragment";

    private static final int HOME_TAB_INDEX = 0;
    private static final int ROUTES_TAB_INDEX = 1;
    private static final int MOCK_TAB_INDEX = 2;

    private ViewPager viewPager;
    private AppBarLayout appbar;
    private Button btnStartWalk;
    private TabLayout tabLayout;

    public StepCountFragment stepCountFragment;
    public RoutesFragment routesFragment;
    public MockFragment mockFragment;

    public TabActivity tabActivity;
    private WalkInfo walkInfo;
    private IRouteManagement routesManager;

    private boolean walkStarted;

    public TabFragment(TabActivity t, WalkInfo w, IRouteManagement r) {
        tabActivity = t;
        walkInfo = w;
        routesManager = r;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        appbar = view.findViewById(R.id.appbar);

        // Keep these after all initializations
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btnStartWalk = view.findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walkStarted) {
                    stopWalk();
                } else {
                    startWalk();
                }
                mockFragment.setButtons();
            }
        });


        return view;
    }

    public void startWalk() {
        walkStarted = !walkStarted;
        tabLayout.getTabAt(HOME_TAB_INDEX).select();
        btnStartWalk.setText(getString(R.string.stop_string));
        stepCountFragment.getWalkUpdate().start();
    }

    public void stopWalk() {
        walkStarted = !walkStarted;
        tabLayout.getTabAt(ROUTES_TAB_INDEX).select();
        btnStartWalk.setText(getContext().getString(R.string.start_string));
        stepCountFragment.getWalkUpdate().stop();
        if (walkInfo.getCurrentRoute() == null) {
            tabActivity.launchEnterRouteInfo(true);
        } else {
            walkInfo.getCurrentRoute().setSteps(walkInfo.getWalkSteps());
            walkInfo.getCurrentRoute().setDistance(walkInfo.getWalkDistance());
            walkInfo.getCurrentRoute().setTime(walkInfo.getWalkTime());
            routesManager.saveRoute(getActivity().getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE), walkInfo.getCurrentRoute());
            walkInfo.setCurrentRoute(null);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());

        stepCountFragment = new StepCountFragment(walkInfo);
        adapter.addFragment(stepCountFragment, getString(R.string.home_tab));

        routesFragment = new RoutesFragment(this, routesManager, walkInfo);
        adapter.addFragment(routesFragment, getString(R.string.routes_tab));

        mockFragment = new MockFragment(this, walkInfo);
        adapter.addFragment(mockFragment, getString(R.string.mock_tab));

        viewPager.setAdapter(adapter);
    }

    public boolean isWalkStarted() {
        return walkStarted;
    }
}
