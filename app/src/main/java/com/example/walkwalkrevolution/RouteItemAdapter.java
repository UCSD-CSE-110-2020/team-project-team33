package com.example.walkwalkrevolution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class RouteItemAdapter extends
        RecyclerView.Adapter<RouteItemAdapter.ViewHolder> {

    private List<Route> routes;
    private TabActivity tabActivity;

    public RouteItemAdapter(TabActivity t) {
        tabActivity = t;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView routeName;
        public TextView startLoc;
        public TextView dist;
        public TextView steps;
        public TextView time;
        Context context;
        //public Button favoriteBtn;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            routeName = itemView.findViewById(R.id.itemRouteName);
            startLoc = itemView.findViewById(R.id.itemRouteStart);
            dist = itemView.findViewById(R.id.itemRouteDist);
            steps = itemView.findViewById(R.id.itemRouteSteps);
            time = itemView.findViewById(R.id.itemRouteTime);
            this.context = context;
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

    @Override
    public RouteItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View routeView = inflater.inflate(R.layout.item_route, parent, false);

        ViewHolder viewHolder = new ViewHolder(routeView.getContext(), routeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RouteItemAdapter.ViewHolder viewHolder, int position) {
        Route route = routes.get(position);

        viewHolder.routeName.setText(route.getName());
        viewHolder.startLoc.setText(route.getStartLoc());
        viewHolder.dist.setText(String.format("%.2f mi", route.getDistance()));
        viewHolder.steps.setText(route.getSteps() + " steps");
        viewHolder.time.setText(formatTime(route.getTime()));
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

    @Override
    public int getItemCount() {
        return routes.size();
    }

}
