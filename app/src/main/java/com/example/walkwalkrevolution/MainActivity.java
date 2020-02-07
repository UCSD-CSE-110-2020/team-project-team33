package com.example.walkwalkrevolution;

import android.os.Bundle;

import com.example.walkwalkrevolution.ui.main.MainFragment;
import com.example.walkwalkrevolution.ui.main.RoutesFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.walkwalkrevolution.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), getString(R.string.home_tab));
        adapter.addFragment(new RoutesFragment(), getString(R.string.routes_tab));
        viewPager.setAdapter(adapter);
    }
}