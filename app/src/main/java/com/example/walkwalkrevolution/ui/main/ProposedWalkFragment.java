package com.example.walkwalkrevolution.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.walkwalkrevolution.Constants;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.zip.CheckedOutputStream;

public class ProposedWalkFragment extends Fragment {
    public static final String TAG = "ProposedWalkFragment";

    private TeammateRoute teamRoute;
    private TabActivity tabActivity;
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

    private Button withdrawlButton;
    private Button scheduleButton;

    private Button acceptButton;
    private Button declineButton;

    public ProposedWalkFragment(TabActivity t, TeammateRoute r, ICloudAdapter c, IAccountInfo a) {
        tabActivity = t;
        teamRoute = r;
        db = c;
        account = a;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proposed_walk, container, false);

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

        Route route = teamRoute.getRoute();

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

        view.findViewById(R.id.routeButtons).setVisibility(View.GONE);
        if(teamRoute.getAccountInfo().equals(account)) {
            view.findViewById(R.id.ownerActions).setVisibility(View.VISIBLE);

            withdrawlButton = view.findViewById(R.id.withdrawButton);
            scheduleButton = view.findViewById(R.id.scheduleButton);

            db.isWalkScheduled(new ICloudAdapter.IBooleanListener() {
                @Override
                public void update(boolean result) {
                    scheduleButton.setVisibility(result ? View.GONE : View.VISIBLE);
                    scheduleButton.setEnabled(!result);
                }
            });

            scheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.scheduleWalk();
                    scheduleButton.setVisibility(View.GONE);
                }
            });

            withdrawlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.cancelWalk();
                    returnFromPage();
                }
            });
        } else {
            view.findViewById(R.id.routeButtons).setVisibility(View.VISIBLE);

            acceptButton = view.findViewById(R.id.acceptButton);
            declineButton = view.findViewById(R.id.declineButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.acceptInvite(account, new ICloudAdapter.IStringListener() {
                        @Override
                        public void update(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                        }
                    });
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.declineInvite(account, new ICloudAdapter.IStringListener() {
                        @Override
                        public void update(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                        }
                    });
                }
            });
        }

        textStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGoogleMaps(view);
            }
        });


        return view;
    }

    public void launchGoogleMaps(View view) {
        TextView startLoc = view.findViewById(R.id.route_info_startLoc_value);
        String address = startLoc.getText().toString();
        String map = Constants.GOOGLE_MAP_URL + address;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
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
}
