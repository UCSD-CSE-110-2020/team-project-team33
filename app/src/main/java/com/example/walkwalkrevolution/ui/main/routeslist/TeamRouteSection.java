package com.example.walkwalkrevolution.ui.main.routeslist;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.util.ArrayList;
import java.util.Iterator;

public class TeamRouteSection extends RouteSection {
    private ArrayList<TeammateRoute> routes;
    private static final String title = "Team Routes";

    public TeamRouteSection(TabActivity t, ClickListener clickListener) {
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
        TeammateRoute route = routes.get(position);

        itemViewHolder.routeName.setText(route.getRoute().getName());
        itemViewHolder.startLoc.setText(route.getRoute().getStartLoc());
        itemViewHolder.dist.setText(String.format("%.2f mi", route.getRoute().getDistance()));
        itemViewHolder.steps.setText(route.getRoute().getSteps() + " steps");
        itemViewHolder.time.setText(formatTime(route.getRoute().getTime()));

        String firstInitial = route.getAccountInfo().getFirstName().substring(0, 1).toUpperCase();
        String lastInitial = route.getAccountInfo().getLastName().substring(0,1).toUpperCase();

        Drawable icon = ContextCompat.getDrawable(itemViewHolder.itemView.getContext(), R.drawable.teammate_icon);
        icon.setColorFilter(route.getAccountInfo().getGmail().hashCode(), PorterDuff.Mode.SRC_OVER);

        itemViewHolder.initials.setBackground(icon);
        itemViewHolder.initials.setText( firstInitial + lastInitial );

        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemRootViewClicked(routes.get(itemViewHolder.getAdapterPosition()).getRoute());
            }
        });
    }

    @Override
    public void setRoutes(Iterator it) {
        routes = new ArrayList<>();
        while(it.hasNext()) {
            routes.add((TeammateRoute) it.next());
        }
    }
}
