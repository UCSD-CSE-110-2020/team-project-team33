package com.example.walkwalkrevolution.ui.main.routeslist;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;
import java.util.Iterator;

public class PersonalRouteSection extends RouteSection {
    private ArrayList<Route> routes;
    private static final String title = "Your Routes";

    public PersonalRouteSection(TabActivity t, ClickListener clickListener) {
        super(t, clickListener, title);
        routes = new ArrayList<>();
    }

    @Override
    public int getContentItemsTotal() {
        return routes.size();
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        RouteItemViewHolder itemViewHolder = (RouteItemViewHolder) holder;
        Route route = routes.get(position);

        itemViewHolder.routeName.setText(route.getName());
        itemViewHolder.startLoc.setText(route.getStartLoc());
        itemViewHolder.dist.setText(String.format("%.2f mi", route.getDistance()));
        itemViewHolder.steps.setText(route.getSteps() + " steps");
        itemViewHolder.time.setText(formatTime(route.getTime()));
        itemViewHolder.initials.setVisibility(View.GONE);

        PersonalRouteSection personalRouteSection = this;
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemRootViewClicked(routes.get(itemViewHolder.getAdapterPosition() - 1),
                        personalRouteSection);
            }
        });
    }

    @Override
    public void setRoutes(Iterator it) {
        routes = new ArrayList<>();
        while(it.hasNext()) {
            routes.add((Route) it.next());
        }
    }
}
