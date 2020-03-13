package com.example.walkwalkrevolution;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProposedRouteAdapter extends RecyclerView.Adapter<ProposedRouteAdapter.ViewHolder> {
    private TabActivity tabActivity;
    private TeammateRoute route;
    private static final String TAG = "[ProposedRouteAdapter]";

    public ProposedRouteAdapter(TabActivity t) {
        tabActivity = t;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView routeName;
        public TextView startLoc;
        public TextView dist;
        public TextView steps;
        public TextView time;
        public TextView initials;
        public TextView proposedText;
        public TextView proposedTime;
        public View itemView;
        public Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.itemRouteName);
            startLoc = itemView.findViewById(R.id.itemRouteStart);
            dist = itemView.findViewById(R.id.itemRouteDist);
            steps = itemView.findViewById(R.id.itemRouteSteps);
            time = itemView.findViewById(R.id.itemRouteTime);
            initials = itemView.findViewById(R.id.initialsIcon);
            proposedText = itemView.findViewById(R.id.proposed_text);
            proposedTime = itemView.findViewById(R.id.time_text);
            this.itemView = itemView;
            this.context = itemView.getContext();
        }
    }

    @Override
    public ProposedRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View teammateView = inflater.inflate(R.layout.item_proposed_route, parent, false);

        ViewHolder viewHolder = new ViewHolder(teammateView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProposedRouteAdapter.ViewHolder viewHolder, int position) {
        if (route == null) {
            Log.w(TAG, "route is null");
        } else if (route.getAccountInfo() == null) {
            Log.w(TAG, "Route account info is null");
        }
        String firstName = route.getAccountInfo().getFirstName();
        String lastName = route.getAccountInfo().getLastName();

        String firstInitial = firstName.substring(0, 1).toUpperCase();
        String lastInitial = lastName.substring(0,1).toUpperCase();

        Drawable icon = ContextCompat.getDrawable(viewHolder.context, R.drawable.teammate_icon);
        icon.setColorFilter(route.getAccountInfo().getGmail().hashCode(), PorterDuff.Mode.SRC_OVER);

        viewHolder.initials.setBackground(icon);
        viewHolder.initials.setText( firstInitial + lastInitial );

        viewHolder.routeName.setText(route.getRoute().getName());
        viewHolder.startLoc.setText(route.getRoute().getStartLoc());
        viewHolder.dist.setText(String.format("%.2f mi", route.getRoute().getDistance()));
        viewHolder.steps.setText(route.getRoute().getSteps() + " steps");
        viewHolder.time.setText(formatTime(route.getRoute().getTime()));

        viewHolder.proposedText.setText(route.isScheduled() ? R.string.scheduled_time_text : R.string.proposed_time_text);
        viewHolder.proposedTime.setText(new SimpleDateFormat(Constants.DATE_FORMAT_STRING).format(new Date(route.getScheduledTime())));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabActivity.launchProposedRouteFragment(route);
            }
        });
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
        return route == null ? 0 : 1;
    }

    public void setRoute(TeammateRoute route) {
        this.route = route;
    }
}
