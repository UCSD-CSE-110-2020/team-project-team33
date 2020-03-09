package com.example.walkwalkrevolution.ui.main.routeslist;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.R;

public class RouteItemViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView routeName;
    public TextView startLoc;
    public TextView dist;
    public TextView steps;
    public TextView time;
    public TextView initials;
    public View itemView;

    public RouteItemViewHolder(View itemView) {
        super(itemView);
        routeName = itemView.findViewById(R.id.itemRouteName);
        startLoc = itemView.findViewById(R.id.itemRouteStart);
        dist = itemView.findViewById(R.id.itemRouteDist);
        steps = itemView.findViewById(R.id.itemRouteSteps);
        time = itemView.findViewById(R.id.itemRouteTime);
        initials = itemView.findViewById(R.id.initialsIcon);
        this.itemView = itemView;
    }
}