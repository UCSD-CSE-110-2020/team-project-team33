package com.example.walkwalkrevolution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.ui.main.SectionsPagerAdapter;

public class TabActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private StepCountFragment stepCountFragment;
    private RoutesFragment routesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final Button btnStartWalk = findViewById(R.id.buttonStartWalk);
        btnStartWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStartWalk.setText(stepCountFragment.startButtonBehavior());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        stepCountFragment = new StepCountFragment();
        adapter.addFragment(stepCountFragment, getString(R.string.home_tab));
        routesFragment = new RoutesFragment();
        adapter.addFragment(routesFragment, getString(R.string.routes_tab));
        viewPager.setAdapter(adapter);
    }
}