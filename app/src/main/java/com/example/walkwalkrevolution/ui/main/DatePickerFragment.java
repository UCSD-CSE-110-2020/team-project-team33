package com.example.walkwalkrevolution.ui.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private RouteInfoFragment routeInfoFragment;
    private int hour, minute;

    public DatePickerFragment(RouteInfoFragment r, int h, int m) {
        routeInfoFragment = r;
        hour = h;
        minute = m;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        routeInfoFragment.scheduledTimeReturned(c.getTimeInMillis());
    }
}
