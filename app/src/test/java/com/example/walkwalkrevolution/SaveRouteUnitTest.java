package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.routemanagement.RoutesManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class SaveRouteUnitTest {

    private Intent intent;
    private RoutesManager routesManager;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), EnterRouteInfoActivity.class);
        intent.putExtra(DataKeys.DISTANCE_KEY, 20);
        intent.putExtra(DataKeys.STEPS_KEY, 20);
        intent.putExtra(DataKeys.TIME_KEY, 20);
    }

    @Test
    public void testSavedRouteWithStart() {
        ActivityScenario<EnterRouteInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            nameField.setText("Route");
            startField.setText("Start");

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.saved_string));
        });
    }

    @Test
    public void testSavedRouteNoName() {
        ActivityScenario<EnterRouteInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            startField.setText("Start");

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.empty_name_err_string));
        });
    }

}
