package com.example.walkwalkrevolution.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.Constants;
import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RouteInfoFragment extends Fragment {
    public static final String TAG = "RouteInfoFragment";

    private Route route;
    private boolean personalRoute;
    private TabActivity tabActivity;
    private WalkInfo walkInfo;
    private IRouteManagement routeManagement;
    private ICloudAdapter db;
    private IAccountInfo account;

    private TextView textSteps;
    private TextView textDistance;
    private TextView textTime;
    private TextView textStart;

    private TextView textRouteType;
    private TextView textRouteSurface;
    private TextView textRouteRoad;
    private TextView textRouteDifficulty;
    private TextView textRouteTerrain;

    private TextView textRouteNotes;

    private FloatingActionButton favoriteButton;

    private Button startWalkButton;
    private Button proposedWalkButton;

    public RouteInfoFragment(TabActivity t, Route r, boolean p, WalkInfo w, IRouteManagement rm, ICloudAdapter c, IAccountInfo a) {
        tabActivity = t;
        route = r;
        personalRoute = p;
        walkInfo = w;
        routeManagement = rm;
        db = c;
        account = a;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_info, container, false);

        // Wild bugfix to get the title in the right place
        ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout)).post(new Runnable() {
            @Override
            public void run() {
                ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout)).requestLayout();
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ((CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout)).setTitle(route.getName());

        textSteps = view.findViewById(R.id.route_steps_value);
        textDistance = view.findViewById(R.id.route_dist_value);
        textTime = view.findViewById(R.id.route_time_value);
        textStart = view.findViewById(R.id.route_info_startLoc_value);

        setTextSteps(route.getSteps());
        setTextDistance(route.getDistance());
        setTextTime(route.getTime());
        determineFeatures(view, textStart, route.getStartLoc().equals("") ? null : route.getStartLoc(), R.id.start_row);

        textRouteType = view.findViewById(R.id.route_type_text);
        textRouteSurface = view.findViewById(R.id.route_surface_text);
        textRouteRoad = view.findViewById(R.id.route_road_text);
        textRouteDifficulty = view.findViewById(R.id.route_difficulty_text);
        textRouteTerrain = view.findViewById(R.id.route_terrain_text);

        if(route.getType() == null && route.getSurface() == null && route.getRoad() == null && route.getDifficulty() == null && route.getTerrain() == null) {
            view.findViewById(R.id.route_features_table).setVisibility(View.GONE);
        } else {
            determineFeatures(view, textRouteType, route.getType(), R.id.type_row);
            determineFeatures(view, textRouteSurface, route.getSurface(), R.id.surface_row);
            determineFeatures(view, textRouteRoad, route.getRoad(), R.id.road_row);
            determineFeatures(view, textRouteDifficulty, route.getDifficulty(), R.id.difficulty_row);
            determineFeatures(view, textRouteTerrain, route.getTerrain(), R.id.terrain_row);
        }

        textRouteNotes = view.findViewById(R.id.route_notes_text);
        determineFeatures(view, textRouteNotes, route.getNotes(), R.id.notes_layout);

        favoriteButton = view.findViewById(R.id.fab);
        favoriteButton.setImageDrawable(getContext().getDrawable(route.getFavorite() ? R.drawable.ic_favorite_24px : R.drawable.ic_favorite_border_24px));
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.setFavorite(!route.getFavorite());
                favoriteButton.setImageDrawable(getContext().getDrawable(route.getFavorite() ? R.drawable.ic_favorite_24px : R.drawable.ic_favorite_border_24px));
                routeManagement.saveRoute(getActivity().getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE), route);
                db.saveRoutes((Iterable<Route>) routeManagement);
            }
        });

        startWalkButton = view.findViewById(R.id.buttonStartWalk);
        if(personalRoute) {
            startWalkButton.setEnabled((walkInfo.getCurrentRoute() == null && !tabActivity.tabFragment.isWalkStarted()) || walkInfo.getCurrentRoute() == route);
            startWalkButton.setText(walkInfo.getCurrentRoute() == route ? getString(R.string.stop_string) : getString(R.string.start_string));
            startWalkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (walkInfo.getCurrentRoute() == route) {
                        startWalkButton.setText(getString(R.string.start_string));
                        tabActivity.tabFragment.stopWalk();
                    } else {
                        startWalkButton.setText(getString(R.string.stop_string));
                        walkInfo.setCurrentRoute(route);
                        tabActivity.tabFragment.startWalk();
                        returnFromPage();
                    }
                }
            });
        } else {
            startWalkButton.setVisibility(View.GONE);
        }

        proposedWalkButton = view.findViewById(R.id.buttonProposeWalk);
        db.isWalkProposed(new ICloudAdapter.IBooleanListener() {
            @Override
            public void update(boolean result) {
                proposedWalkButton.setEnabled(!result);
            }
        });
        proposedWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        return view;
    }

    private void determineFeatures(View view, TextView text, String feature, int id) {
        if (feature != null) {
            text.setText(feature);
        } else {
            view.findViewById(id).setVisibility(View.GONE);
        }
    }

    public void setTextSteps(long steps) {
        textSteps.setText(String.valueOf(steps));
    }

    public void setTextDistance(double distance) {
        textDistance.setText(String.format(getString(R.string.dist_format), distance));
    }

    public void setTextTime(long time) {
        textTime.setText(formatTime(time));
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
    
    private void returnFromPage() {
        tabActivity.deleteFragment(this);
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(int hour, int minute) {
        DialogFragment newFragment = new DatePickerFragment(this, hour, minute);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void scheduledTimeReturned(long timeMillis) {
        TeammateRoute teammateRoute = new TeammateRoute(route, account, false, timeMillis);
        Context context = getContext();
        db.proposeWalk(teammateRoute, new ICloudAdapter.IBooleanListener() {
            @Override
            public void update(boolean result) {
                proposedWalkButton.setEnabled(!result);
                Toast.makeText(context, result ? Constants.ROUTE_PROPOSED : Constants.INVALID_TIME, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
