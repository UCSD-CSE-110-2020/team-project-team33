package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.RouteItemAdapter;
import com.example.walkwalkrevolution.RouteSection;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class RoutesFragment extends Fragment implements Observer {
    public static final String TAG = "RoutesFragment";

    TabFragment tabFragment;
    IRouteManagement routesManager;
    WalkInfo walkInfo;
    RecyclerView rvRoutes;
    FloatingActionButton fab;
    RouteItemAdapter routeAdapter;
    SectionedRecyclerViewAdapter sectionedAdapter;
    RouteSection personalRoutes;
    View view;

    public RoutesFragment(TabFragment tabFragment, IRouteManagement routesManager, WalkInfo walkInfo) {
        this.tabFragment = tabFragment;
        this.routesManager = routesManager;
        this.walkInfo = walkInfo;
        this.routeAdapter = new RouteItemAdapter(tabFragment.tabActivity);
        routeAdapter.setRoutes(((Iterable<Route>) routesManager).iterator());
        ((Observable) routesManager).addObserver(this);
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_routes, container, false);

        sectionedAdapter = new SectionedRecyclerViewAdapter();
        personalRoutes = new RouteSection(tabFragment.tabActivity);

        sectionedAdapter.addSection(personalRoutes);

        rvRoutes = view.findViewById(R.id.rvRoutes);
        rvRoutes.setHasFixedSize(true);
        rvRoutes.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvRoutes.addItemDecoration(new DividerItemDecoration(rvRoutes.getContext(), DividerItemDecoration.VERTICAL));

        // These are causing problems with Espresso
        rvRoutes.setAdapter(sectionedAdapter);

        fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabFragment.tabActivity.launchEnterRouteInfo(false);
            }
        });


        return view;
    }

    // should be updating when observable calls

    @Override
    public void update(Observable o, Object arg) {
        personalRoutes.setRoutes((Iterator) arg);
        sectionedAdapter.notifyDataSetChanged();
    }
}
