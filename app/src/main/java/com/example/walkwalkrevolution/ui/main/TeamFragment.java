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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


public class TeamFragment extends Fragment implements Observer {

    private TabFragment tabFragment;
    RecyclerView rvTeammates;
    TeammateItemAdapter teammateItemAdapter;


    private FloatingActionButton FAB;

    public TeamFragment(TabFragment t) {
        tabFragment = t;
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

        teammateItemAdapter = new TeammateItemAdapter();

        rvTeammates.setAdapter(teammateItemAdapter);

        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        teammateItemAdapter.setTeammates((ArrayList) arg);
        teammateItemAdapter.notifyDataSetChanged();
    }


}
