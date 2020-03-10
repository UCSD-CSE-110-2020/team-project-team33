package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.InviteTeammateItemAdapter;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;

import java.util.ArrayList;

public class AcceptInviteFragment extends Fragment implements ICloudAdapter.IInviteSubject {
    private static final String TAG = "[AcceptInviteFragment]";

    private ICloudAdapter db;
    private RecyclerView rvTeammates;
    private InviteTeammateItemAdapter teammateItemAdapter;

    public AcceptInviteFragment(ICloudAdapter c) {
        db = c;
        this.teammateItemAdapter = new InviteTeammateItemAdapter(db, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_invite, container, false);

        Toolbar toolbar = view.findViewById(R.id.invite_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.invites_string);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rvTeammates = view.findViewById(R.id.rvInvites);
        rvTeammates.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvTeammates.addItemDecoration(new DividerItemDecoration(rvTeammates.getContext(), DividerItemDecoration.VERTICAL));
        rvTeammates.setAdapter(teammateItemAdapter);

        if(db.userSet()) {
            db.getInvites(this);
        }

        return view;
    }

    @Override
    public void update(ArrayList<IAccountInfo> invites) {
        teammateItemAdapter.setTeammates(invites);
        teammateItemAdapter.notifyDataSetChanged();
    }
}
