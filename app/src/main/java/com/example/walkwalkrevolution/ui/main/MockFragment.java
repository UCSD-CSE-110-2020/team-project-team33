package com.example.walkwalkrevolution.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TabActivity;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

public class MockFragment extends Fragment {
    private EditText editStepCount;
    private Button buttonSetStepCount;
    private NumberPicker pickerHour;
    private NumberPicker pickerMinutes;
    private NumberPicker pickerSeconds;
    private Button buttonSetTime;
    private Button buttonToggleMock;

    private WalkInfo walkInfo;

    private TabActivity tabActivity;

    public MockFragment(WalkInfo w, TabActivity t) {
        walkInfo = w;
        tabActivity = t;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mock, container, false);

        editStepCount = view.findViewById(R.id.editStepCount);
        buttonSetStepCount = view.findViewById(R.id.button_set_step_count);
        pickerHour = view.findViewById(R.id.hour_picker);
        pickerMinutes = view.findViewById(R.id.minute_picker);
        pickerSeconds = view.findViewById(R.id.second_picker);
        buttonSetTime = view.findViewById(R.id.set_time_button);
        buttonToggleMock = view.findViewById(R.id.toggle_mock_button);

        setButtons();

        pickerHour.setMinValue(0);
        pickerHour.setMaxValue(23);
        pickerHour.setWrapSelectorWheel(false);

        pickerMinutes.setMinValue(0);
        pickerMinutes.setMaxValue(59);
        pickerMinutes.setWrapSelectorWheel(false);

        pickerSeconds.setMinValue(0);
        pickerSeconds.setMaxValue(59);
        pickerSeconds.setWrapSelectorWheel(false);

        buttonToggleMock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkInfo.setMocking(!walkInfo.isMocking());
                setButtons();
            }
        });

        buttonSetStepCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkInfo.setSteps(Long.parseLong(editStepCount.getText().toString()));
            }
        });

        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = pickerHour.getValue() * 60 * 60 + pickerMinutes.getValue() * 60 + pickerSeconds.getValue();
                walkInfo.setWalkTime(time);
            }
        });

        return view;
    }

    public void setButtons() {
        if(buttonSetStepCount != null && buttonSetTime != null && buttonToggleMock != null) {
            buttonSetStepCount.setEnabled(walkInfo.isMocking());
            buttonSetTime.setEnabled(walkInfo.isMocking() && tabActivity.isWalkStarted());
            buttonToggleMock.setText(walkInfo.isMocking() ? getString(R.string.toggle_mock_text_on) : getString(R.string.toggle_mock_text_off));
        }
    }
}
