package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class SaveRouteUnitTest {

    private Intent intent;
    private MockRoutesManager routesManager;

    private final double DEFAULT_DISTANCE = 20;
    private final long DEFAULT_STEPS = 20;
    private final long DEFAULT_TIME = 20;
    private final String DEFAULT_NAME = "Route";
    private final String DEFAULT_START = "Start";


    @Before
    public void setUp() {
        routesManager = new MockRoutesManager();
        intent = new Intent(ApplicationProvider.getApplicationContext(), EnterRouteInfoActivity.class);
        intent.putExtra(DataKeys.DISTANCE_KEY, DEFAULT_DISTANCE);
        intent.putExtra(DataKeys.STEPS_KEY, DEFAULT_STEPS);
        intent.putExtra(DataKeys.TIME_KEY, DEFAULT_TIME);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
    }

    @Test
    public void testSavedRouteWithStartUI() {
        ActivityScenario<EnterRouteInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            nameField.setText(DEFAULT_NAME);
            startField.setText(DEFAULT_START);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.saved_string));
        });
    }

    @Test
    public void testSavedRouteNoNameUI() {
        ActivityScenario<EnterRouteInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            startField.setText(DEFAULT_START);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.empty_name_err_string));
        });
    }

    @Test
    public void testSavedRouteWithStart() {
        ActivityScenario<EnterRouteInfoActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            nameField.setText(DEFAULT_NAME);
            startField.setText(DEFAULT_START);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(routesManager.recentSteps).isEqualTo(DEFAULT_STEPS);
            assertThat(routesManager.recentDistance).isEqualTo(DEFAULT_DISTANCE);
            assertThat(routesManager.recentTime).isEqualTo(DEFAULT_TIME);
            assertThat(routesManager.recentName).isEqualTo(DEFAULT_NAME);
            assertThat(routesManager.recentStart).isEqualTo(DEFAULT_START);
        });
    }
}
