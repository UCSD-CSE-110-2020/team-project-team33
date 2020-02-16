package com.example.walkwalkrevolution.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.RouteFeatures;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

import java.util.ArrayList;
import java.util.List;

public class EnterRouteInfoFragment extends Fragment {
    IRouteManagement routesManager;
    TabActivity tabActivity;
    RouteFeatures routeFeatures = new RouteFeatures();
    String[] features = new String[DataKeys.NUM_FEAUTRES];
    boolean isFavorited = false;
    WalkInfo walkInfo;

    public EnterRouteInfoFragment(TabActivity tabs, IRouteManagement routeMan, WalkInfo walk) {
        tabActivity = tabs;
        routesManager = routeMan;
        walkInfo = walk;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_route_info, container, false);

        double distance = walkInfo.getWalkDistance();
        long steps = walkInfo.getWalkSteps();
        long time = walkInfo.getWalkTime();

        Spinner difficulty = view.findViewById(R.id.difficultyType);
        Spinner road = view.findViewById(R.id.roadType);
        Spinner terrain = view.findViewById(R.id.terrainType);
        Spinner surface = view.findViewById(R.id.surfaceType);
        Spinner routeType = view.findViewById(R.id.routeType);

        Spinner[] spinners = { difficulty, road, terrain, surface, routeType };

        for(int i = 0; i < spinners.length; i++) {
            setSpinnerOptions(spinners[i], i);
            setSpinnerSelect(spinners[i], i);
        }

        ToggleButton toggle = (ToggleButton) view.findViewById(R.id.favoriteBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavorited = true;
                } else {
                    isFavorited = false;
                }
            }
        });

        Button saveBtn = view.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameField = (EditText) view.findViewById(R.id.routeName);
                EditText startField = (EditText) view.findViewById(R.id.startLoc);
                String name = nameField.getText().toString();
                if(name.trim().length() == 0){
                    Toast.makeText(view.getContext(), getString(R.string.empty_name_err_string), Toast.LENGTH_LONG).show();
                    return;
                }
                String startLoc = startField.getText().toString();

                Route route = new Route(name, startLoc, steps, distance, time, getFeatures());
                route.setFavorite(isFavorited);
                routesManager.saveRoute(getActivity().getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE), route);

                Toast.makeText(view.getContext(), getString(R.string.saved_string), Toast.LENGTH_SHORT).show();

                returnFromPage();
            }
        });

        return view;
    }

    private void returnFromPage() {
        tabActivity.deleteFragment(this);
    }

    private void setSpinnerSelect(Spinner spinner, int index) {
        if(spinner == null) {
            return;
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 0) {
                    setSpinnerHint(selectedItemView);
                    features[index] = null;
                } else {
                    features[index] = selectedItemView.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // default stub
            }

        });
    }


    private void setSpinnerOptions(Spinner spinner, int index) {
        if(spinner == null) {
            return;
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, routeFeatures.getFeature(index)){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if(position == 0){
                    // Set the hint text color gray italic
                    setSpinnerHint(view);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void setSpinnerHint(View view) {
        // set gray italics for hints
        TextView tv = (TextView) view;
        tv.setTextColor(Color.GRAY);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
    }

    private List<String> getFeatures() {
        List<String> tags = new ArrayList<String>();
        for(String s : features) {
            if(s != null) { tags.add(s); }
        }
        return tags;

    }
}
