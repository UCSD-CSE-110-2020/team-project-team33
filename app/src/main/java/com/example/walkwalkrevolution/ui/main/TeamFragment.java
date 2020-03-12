package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walkwalkrevolution.PersonalRouteAdapter;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TeammateItemAdapter;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.cloud.Teammate;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TeamFragment extends Fragment implements ICloudAdapter.ITeammateListener, ICloudAdapter.IDatabaseObserver {

    private TabFragment tabFragment;
    private ICloudAdapter db;
    private RecyclerView rvTeammates;
    private RecyclerView rvProposedRoute;
    private TeammateItemAdapter teammateItemAdapter;
    private PersonalRouteAdapter personalRouteAdapter;

    private FloatingActionButton FAB;

    public TeamFragment(TabFragment t, ICloudAdapter c) {
        tabFragment = t;
        db = c;
        this.teammateItemAdapter = new TeammateItemAdapter();
        this.personalRouteAdapter = new PersonalRouteAdapter(tabFragment.tabActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        FAB = view.findViewById(R.id.floatingActionButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabFragment.tabActivity.launchInvite();
            }
        });

        rvTeammates = view.findViewById(R.id.teammates_rv);
        rvTeammates.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvTeammates.addItemDecoration(new DividerItemDecoration(rvTeammates.getContext(), DividerItemDecoration.VERTICAL));
        rvTeammates.setAdapter(teammateItemAdapter);

        rvProposedRoute = view.findViewById(R.id.proposed_walk_rv);
        rvProposedRoute.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvProposedRoute.addItemDecoration(new DividerItemDecoration(rvProposedRoute.getContext(), DividerItemDecoration.VERTICAL));
        rvProposedRoute.setAdapter(personalRouteAdapter);

        db.addObserver(this);
        if(db.userSet()) {
            update();
        }

        return view;
    }


    @Override
    public void update(ArrayList<Teammate> teamMembers) {
        teammateItemAdapter.setTeammates(teamMembers);
        teammateItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void update() {

        db.isWalkProposed(new ICloudAdapter.IBooleanListener() {
            @Override
            public void update(boolean result) {
                if(result) {
                    rvProposedRoute.setVisibility(View.VISIBLE);
                    db.getProposedWalk(new ICloudAdapter.ITeammateRouteListener() {
                        @Override
                        public void update(TeammateRoute teammateRoute) {
                            personalRouteAdapter.setRoute(teammateRoute);
                            personalRouteAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    rvProposedRoute.setVisibility(View.GONE);
                }
            }
        });

        db.getTeam(this);
    }
}
