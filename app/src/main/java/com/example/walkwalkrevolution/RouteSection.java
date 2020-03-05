package com.example.walkwalkrevolution;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

public class RouteSection extends Section {
   private List<Route> routes;
    private TabActivity tabActivity;

    public RouteSection(TabActivity t) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_route).
                build());
        tabActivity = t;
        routes = new ArrayList<Route>();
   }

   @Override
    public int getContentItemsTotal() { return routes.size(); }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new RouteItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        RouteItemViewHolder viewHolder = (RouteItemViewHolder) holder;
        Route route = routes.get(position);

        viewHolder.routeName.setText(route.getName());
        viewHolder.startLoc.setText(route.getStartLoc());
        viewHolder.dist.setText(String.format("%.2f mi", route.getDistance()));
        viewHolder.steps.setText(route.getSteps() + " steps");
        viewHolder.time.setText(formatTime(route.getTime()));
    }

    public class RouteItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView routeName;
        public TextView startLoc;
        public TextView dist;
        public TextView steps;
        public TextView time;


        public RouteItemViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.itemRouteName);
            startLoc = itemView.findViewById(R.id.itemRouteStart);
            dist = itemView.findViewById(R.id.itemRouteDist);
            steps = itemView.findViewById(R.id.itemRouteSteps);
            time = itemView.findViewById(R.id.itemRouteTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                tabActivity.launchRouteInfo(routes.get(position));
            }
        }
    }

    public void setRoutes(Iterator it) {
        ArrayList<Route> routes = new ArrayList<>();
        while(it.hasNext()) { routes.add((Route)it.next());}
        this.routes = routes;
    }

    public String formatTime(long duration) {
        int seconds = (int)(duration % 60);
        int minutes = (int)((duration / 60) % 60);
        int hours = (int)(duration / (60 * 60)) % 24;
        return formatDigits(hours) + ":" + formatDigits(minutes) + ":" + formatDigits(seconds);
    }

    private String formatDigits(int x){
        return x < 10 ? "0" + x : String.valueOf(x);
    }

}
