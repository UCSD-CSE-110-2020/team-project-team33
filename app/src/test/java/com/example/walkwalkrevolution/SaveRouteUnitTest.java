package com.example.walkwalkrevolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

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
        intent = new Intent(ApplicationProvider.getApplicationContext(), EnterRouteInfo.class);
        intent.putExtra("DISTANCE", 20);
        intent.putExtra("STEPS", 20);

    }

    @Test
    public void testSavedRouteWithStart() {
        ActivityScenario<EnterRouteInfo> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            nameField.setText("Route");
            startField.setText("Start");

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("Saved");
        });
    }

    @Test
    public void testSavedRouteNoName() {
        ActivityScenario<EnterRouteInfo> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            EditText nameField = (EditText) activity.findViewById(R.id.routeName);
            EditText startField = (EditText) activity.findViewById(R.id.startLoc);

            startField.setText("Start");

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("Name cannot be empty");
        });
    }

}
