package com.example.walkwalkrevolution;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.account.IAccountInfo;

import java.util.ArrayList;
import java.util.List;

public class TeammateItemAdapter extends
        RecyclerView.Adapter<TeammateItemAdapter.ViewHolder>  {

    private List<IAccountInfo> teammates;

    public TeammateItemAdapter() {
        this.teammates = new ArrayList<IAccountInfo>();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView name;
        public TextView initials;
        Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            initials = itemView.findViewById(R.id.initials);
            this.context = context;
        }
    }

    @Override
    public TeammateItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View teammateView = inflater.inflate(R.layout.item_teammate, parent, false);

        TeammateItemAdapter.ViewHolder viewHolder = new TeammateItemAdapter.ViewHolder(teammateView.getContext(), teammateView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TeammateItemAdapter.ViewHolder viewHolder, int position) {
        System.out.println(getItemCount());
        IAccountInfo teammate = teammates.get(position);
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
