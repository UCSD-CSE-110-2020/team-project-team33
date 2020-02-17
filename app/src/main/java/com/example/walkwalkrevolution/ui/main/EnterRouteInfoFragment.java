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

import com.example.walkwalkrevolution.Constants;
import com.example.walkwalkrevolution.DataKeys;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RouteFeatures.RouteFeatures;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

public class EnterRouteInfoFragment extends Fragment {
    public static final String TAG = "EnterRouteInfoFragment";

    IRouteManagement routesManager;
    TabActivity tabActivity;
    String[] features = new String[Constants.NUM_FEATURES];
    boolean isFavorited = false;
    WalkInfo walkInfo;
    boolean isSavingWalk = true;
    double distance = 0;
    long steps = 0;
    long time = 0;

    public EnterRouteInfoFragment(TabActivity tabs, IRouteManagement routeMan, WalkInfo walk, Boolean isSavingWalk) {
        tabActivity = tabs;
        routesManager = routeMan;
        walkInfo = walk;
        this.isSavingWalk = isSavingWalk;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_route_info, container, false);

        if (isSavingWalk) {
            distance = walkInfo.getWalkDistance();
            steps = walkInfo.getWalkSteps();
            time = walkInfo.getWalkTime();
        }

        Spinner difficulty = view.findViewById(R.id.difficultyType);
        Spinner road = view.findViewById(R.id.roadType);
        Spinner terrain = view.findViewById(R.id.terrainType);
        Spinner surface = view.findViewById(R.id.surfaceType);
        Spinner routeType = view.findViewById(R.id.routeType);

        Spinner[] spinners = { routeType, surface, road, difficulty, terrain };

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
                    Toast.makeText(view.getContext(), getString(R.string.empty_name_err_string), Toast.LENGTH_SHORT).show();
                    return;
                } else if (name.length() > Constants.MAX_NAME_LENGTH) {
                    Toast.makeText(view.getContext(), getString(R.string.long_name_err_string), Toast.LENGTH_SHORT).show();
                    return;
                }
                String startLoc = startField.getText().toString();

                String type = spinners[Constants.FEATURE_TYPE_INDEX].getSelectedItem().toString().equals(RouteFeatures.TYPE[0]) ?
                        null : spinners[Constants.FEATURE_TYPE_INDEX].getSelectedItem().toString();
                String surface = spinners[Constants.FEATURE_SURFACE_INDEX].getSelectedItem().toString().equals(RouteFeatures.SURFACE[0]) ?
                        null : spinners[Constants.FEATURE_SURFACE_INDEX].getSelectedItem().toString();
                String road = spinners[Constants.FEATURE_ROAD_INDEX].getSelectedItem().toString().equals(RouteFeatures.ROAD[0]) ?
                        null : spinners[Constants.FEATURE_ROAD_INDEX].getSelectedItem().toString();
                String difficulty = spinners[Constants.FEATURE_DIFFICULTY_INDEX].getSelectedItem().toString().equals(RouteFeatures.DIFFICULTY[0]) ?
                        null : spinners[Constants.FEATURE_DIFFICULTY_INDEX].getSelectedItem().toString();
                String terrain = spinners[Constants.FEATURE_TERRAIN_INDEX].getSelectedItem().toString().equals(RouteFeatures.TERRAIN[0]) ?
                        null : spinners[Constants.FEATURE_TERRAIN_INDEX].getSelectedItem().toString();

                String notes = ((EditText) view.findViewById(R.id.editNotes)).getText().toString();
                notes = notes.equals("") ? null : notes;

                Route route = new Route(name, startLoc, steps, distance, time, type, surface, road, difficulty, terrain, notes);
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
                getActivity(), android.R.layout.simple_spinner_dropdown_item, RouteFeatures.OPTIONS[index]){
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
}
