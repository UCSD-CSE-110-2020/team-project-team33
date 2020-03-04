package com.example.walkwalkrevolution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.account.AccountInfo;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.List;

public class TeammateItemAdapter extends
        RecyclerView.Adapter<TeammateItemAdapter.ViewHolder>  {

    private List<IAccountInfo> teammates;

    public TeammateItemAdapter() {}

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView name;
        Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            name = itemView.findViewById(R.id.name);
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

        // need to set text based on position

        viewHolder.name.setText("idk");
    }

    @Override
    public int getItemCount() {
        return teammates.size();
    }

    public void setTeammates(List<IAccountInfo> teammates) {
        this.teammates = teammates;
    }

}
