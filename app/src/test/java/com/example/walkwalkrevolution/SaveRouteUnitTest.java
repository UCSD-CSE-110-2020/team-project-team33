package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.ui.main.EnterRouteInfoFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class SaveRouteUnitTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private MockRoutesManager routesManager;
    private ActivityScenario<TabActivity> scenario;

    private final long DEFAULT_STEPS = 100;
    private final long DEFAULT_TIME = 30;
    private final double DEFAULT_DISTANCE = 0.05;

    private final int DEFAULT_HEIGHT = 72;
    private final String DEFAULT_NAME = "Route";
    private final String LONG_NAME = "this is over 24 chars i hope";
    private final String DEFAULT_START = "Start";


    @Before
    public void setUp() {
        routesManager = new MockRoutesManager();
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);
        FitnessServiceFactory.put(TEST_SERVICE, MockFitnessService::new);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);

        AccountFactory.put(TEST_SERVICE, new AccountFactory.BluePrint() {
            @Override
            public IAccountInfo create(Context context) {
                return new MockAccountInfo(context);
            }

            @Override
            public IAccountInfo create(String first, String last, String gmail) {
                return new MockAccountInfo(first, last, gmail);
            }
        });        intent.putExtra(DataKeys.ACCOUNT_KEY, TEST_SERVICE);

        CloudAdapterFactory.put(TEST_SERVICE, MockCloud::new);
        intent.putExtra(DataKeys.CLOUD_KEY, TEST_SERVICE);
        MockCloud.reset();

        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, DEFAULT_HEIGHT);

        scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.tabFragment.stepCountFragment.getStepUpdate().stop();
            activity.tabFragment.stepCountFragment.getWalkUpdate().stop();
            activity.getWalkInfo().setMocking(true);
            activity.getWalkInfo().setSteps(DEFAULT_STEPS);
            activity.getWalkInfo().setWalkTime(DEFAULT_TIME);
            activity.launchEnterRouteInfo(true);
        });
    }

    @Test
    public void testSavedRouteWithStartUI() {
        scenario.onActivity(activity -> {
            EnterRouteInfoFragment fragment = (EnterRouteInfoFragment) activity
                    .getSupportFragmentManager()
                    .getFragments()
                    .get(activity.getSupportFragmentManager().getFragments().size() - 1);

            EditText nameField = (EditText) fragment.getView().findViewById(R.id.routeName);
            EditText startField = (EditText) fragment.getView().findViewById(R.id.startLoc);

            nameField.setText(DEFAULT_NAME);
            startField.setText(DEFAULT_START);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.saved_string));
        });
    }

    @Test
    public void testSavedRouteNoNameUI() {
        scenario.onActivity(activity -> {
            EnterRouteInfoFragment fragment = (EnterRouteInfoFragment) activity
                    .getSupportFragmentManager()
                    .getFragments()
                    .get(activity.getSupportFragmentManager().getFragments().size() - 1);

            EditText nameField = (EditText) fragment.getView().findViewById(R.id.routeName);
            EditText startField = (EditText) fragment.getView().findViewById(R.id.startLoc);

            startField.setText(DEFAULT_START);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.empty_name_err_string));
        });
    }

    @Test
    public void testSavedRouteLongNameUI() {
        scenario.onActivity(activity -> {
            EnterRouteInfoFragment fragment = (EnterRouteInfoFragment) activity
                    .getSupportFragmentManager()
                    .getFragments()
                    .get(activity.getSupportFragmentManager().getFragments().size() - 1);

            EditText nameField = (EditText) fragment.getView().findViewById(R.id.routeName);

            nameField.setText(LONG_NAME);

            activity.findViewById(R.id.saveButton).performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.long_name_err_string));
        });
    }

    @Test
    public void testSavedRouteWithStart() {
        scenario.onActivity(activity -> {
            EnterRouteInfoFragment fragment = (EnterRouteInfoFragment) activity
                    .getSupportFragmentManager()
                    .getFragments()
                    .get(activity.getSupportFragmentManager().getFragments().size() - 1);

            EditText nameField = (EditText) fragment.getView().findViewById(R.id.routeName);
            EditText startField = (EditText) fragment.getView().findViewById(R.id.startLoc);

            nameField.setText(DEFAULT_NAME);
            startField.setText(DEFAULT_START);

            fragment.getView().findViewById(R.id.saveButton).performClick();

            assertThat(routesManager.recentSteps).isEqualTo(DEFAULT_STEPS);
            assertThat(routesManager.recentDistance - DEFAULT_DISTANCE < 0.001).isEqualTo(true);
            assertThat(routesManager.recentTime).isEqualTo(DEFAULT_TIME);
            assertThat(routesManager.recentName).isEqualTo(DEFAULT_NAME);
            assertThat(routesManager.recentStart).isEqualTo(DEFAULT_START);
        });
    }
}
