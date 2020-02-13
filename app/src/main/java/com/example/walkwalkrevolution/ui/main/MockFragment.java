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
import com.example.walkwalkrevolution.walktracker.IDelayedUpdate;
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

    private IDelayedUpdate stepUpdate;
    private IDelayedUpdate walkUpdate;

    public MockFragment(WalkInfo w, IDelayedUpdate step, IDelayedUpdate walk) {
        walkInfo = w;
        stepUpdate = step;
        walkUpdate = walk;
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

        buttonSetStepCount.setEnabled(false);
        buttonSetTime.setEnabled(false);

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
                if(walkInfo.isMocking()) {
                    buttonToggleMock.setText(getString(R.string.toggle_mock_text_off));
                    walkInfo.setMocking(false);
                } else {
                    buttonToggleMock.setText(getString(R.string.toggle_mock_text_on));
                    walkInfo.setMocking(true);
                }
            }
        });

        return view;
    }
}
