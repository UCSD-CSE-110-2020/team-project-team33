package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.RouteItemAdapter;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

import java.util.Iterator;

public class RoutesFragment extends Fragment {

    IRouteManagement routesManager;
    WalkInfo walkInfo;

    public RoutesFragment(WalkInfo walkInfo) {
        this.walkInfo = walkInfo;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        routesManager = (IRouteManagement) getActivity().getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        RecyclerView rvRoutes = (RecyclerView) view.findViewById(R.id.rvRoutes);

        RouteItemAdapter routeAdapter = new RouteItemAdapter(((Iterable<Route>) routesManager).iterator());

        rvRoutes.setAdapter(routeAdapter);

        rvRoutes.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }
}
