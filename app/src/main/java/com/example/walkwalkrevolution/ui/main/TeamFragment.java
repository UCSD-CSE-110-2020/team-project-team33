package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TeammateItemAdapter;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;




public class TeamFragment extends Fragment implements ICloudAdapter.ITeamSubject {

    private TabFragment tabFragment;
    private ICloudAdapter db;
    private RecyclerView rvTeammates;
    private TeammateItemAdapter teammateItemAdapter;

    private FloatingActionButton FAB;

    public TeamFragment(TabFragment t, ICloudAdapter c) {
        tabFragment = t;
        db = c;
        this.teammateItemAdapter = new TeammateItemAdapter();
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

        if(db.userSet()) {
            db.getTeam(this);
        }

        return view;
    }


    @Override
    public void update(ArrayList<IAccountInfo> teamMembers) {
        teammateItemAdapter.setTeammates(teamMembers);
        teammateItemAdapter.notifyDataSetChanged();
    }
}
