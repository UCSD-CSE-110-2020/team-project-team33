package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.walkwalkrevolution.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class TeamFragment extends Fragment {

    private TabFragment tabFragment;

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

        return view;
    }
}
