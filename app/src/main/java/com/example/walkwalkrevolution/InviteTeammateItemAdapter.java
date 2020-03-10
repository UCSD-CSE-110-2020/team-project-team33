package com.example.walkwalkrevolution;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.ui.main.AcceptInviteFragment;

import java.util.ArrayList;
import java.util.List;

public class InviteTeammateItemAdapter extends
        RecyclerView.Adapter<InviteTeammateItemAdapter.ViewHolder> {

    private List<IAccountInfo> teammates;
    private ICloudAdapter db;
    private AcceptInviteFragment acceptInviteFragment;

    public InviteTeammateItemAdapter(ICloudAdapter c, AcceptInviteFragment a) {
        this.teammates = new ArrayList<IAccountInfo>();
        db = c;
        acceptInviteFragment = a;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements ICloudAdapter.IAcceptSubject {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView name;
        public TextView initials;
        public Button acceptButton;
        public Button declineButton;
        public IAccountInfo account;
        Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            name = itemView.findViewById(R.id.name);
            initials = itemView.findViewById(R.id.initials);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
            this.context = context;

            ICloudAdapter.IAcceptSubject dbItem = this;
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.acceptInvite(account, dbItem);
                }
            });
        }

        @Override
        public void update(String message) {
            if(db.userSet()) {
                db.getInvites(acceptInviteFragment);
            }
            Toast.makeText(acceptInviteFragment.getContext(), message, Toast.LENGTH_SHORT).show();
        }

        public void setAccount(IAccountInfo account) {
            this.account = account;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View teammateView = inflater.inflate(R.layout.item_invite, parent, false);

        ViewHolder viewHolder = new ViewHolder(teammateView.getContext(), teammateView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        IAccountInfo teammate = teammates.get(position);
        viewHolder.setAccount(teammate);
        String firstName = teammate.getFirstName();
        String lastName = teammate.getLastName();
        String firstInitial = firstName.substring(0, 1).toUpperCase();
        String lastInitial = lastName.substring(0,1).toUpperCase();
        Drawable icon = ContextCompat.getDrawable(viewHolder.context, R.drawable.teammate_icon);
        icon.setColorFilter(teammate.getGmail().hashCode(), PorterDuff.Mode.SRC_OVER);
        viewHolder.name.setText(firstInitial + firstName.substring(1) + " " + lastInitial + lastName.substring(1));
        viewHolder.initials.setBackground(icon);
        viewHolder.initials.setText( firstInitial + lastInitial );
    }

    @Override
    public int getItemCount() {
        return teammates.size();
    }

    public void setTeammates(List<IAccountInfo> teammates) {
        this.teammates = teammates;
    }

}
