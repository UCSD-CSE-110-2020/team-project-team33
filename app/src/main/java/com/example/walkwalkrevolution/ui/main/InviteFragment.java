package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;


public class InviteFragment extends Fragment {

    private ICloudAdapter db;
    private TabActivity tabActivity;

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editGmail;
    private Button buttonInvite;

    public InviteFragment(ICloudAdapter c, TabActivity t) {
        db = c;
        tabActivity = t;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);

        Toolbar toolbar = view.findViewById(R.id.invite_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.invite_fragment_title);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editFirstName = view.findViewById(R.id.first_name_text);
        editLastName = view.findViewById(R.id.last_name_text);
        editGmail = view.findViewById(R.id.email_text);
        buttonInvite = view.findViewById(R.id.invite_btn);

        buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonInviteBehavior();
            }
        });

        return view;
    }

    private void buttonInviteBehavior() {
        if(checkError(editFirstName, R.string.invite_first_name_error) ||
           checkError(editLastName, R.string.invite_last_name_error) ||
           checkError(editGmail, R.string.invite_email_error)) {
            return;
        }

        IAccountInfo recipient = AccountFactory.create(getActivity().getIntent().getStringExtra(DataKeys.ACCOUNT_KEY),
                editFirstName.getText().toString(),
                editLastName.getText().toString(),
                editGmail.getText().toString());
        db.invite(recipient, getContext());

        tabActivity.deleteFragment(this);
    }

    private boolean checkError(EditText edit, int id) {
        if(edit.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), getString(id), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
