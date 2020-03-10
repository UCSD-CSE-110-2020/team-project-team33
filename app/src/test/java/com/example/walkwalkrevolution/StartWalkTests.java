package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StartWalkTests {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int VALID_STEP_COUNT = 247;
    private static final int VALID_HEIGHT = 63;
    private static final double EXPECTED_DIST = (((VALID_HEIGHT * 0.413) / 12 ) / 5280) * VALID_STEP_COUNT;
    private static final long VALID_TIME = 5930;
    private static final String VALID_TIME_STR = "01:38:50";


    private Intent intent;

    @Before
    public void setUp() {
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
        MockCloud.resetArrays();

        intent.putExtra(DataKeys.USER_HEIGHT_KEY, VALID_HEIGHT);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new MockRoutesManager());
    }

    @Test
    public void startButtonChangesText() {
        MockFitnessService.nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);
            assertThat(startWalkButton.getText().toString()).isEqualTo(activity.getString(R.string.start_string));
            startWalkButton.performClick();
            assertThat(startWalkButton.getText().toString()).isEqualTo(activity.getString(R.string.stop_string));
            startWalkButton.performClick();
            assertThat(startWalkButton.getText().toString()).isEqualTo(activity.getString(R.string.start_string));
        });
    }

    @Test
    public void testTimerFormat() {
        MockFitnessService.nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            assertThat(activity.tabFragment.stepCountFragment.formatTime(VALID_TIME)).isEqualTo(VALID_TIME_STR);
        });
    }

    @Test
    public void testTimerUI() {
        MockFitnessService.nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);
            startWalkButton.performClick();

            activity.getWalkInfo().setMocking(true);
            activity.getWalkInfo().setWalkTime(VALID_TIME);
            activity.tabFragment.stepCountFragment.getWalkUpdate().update();

            TextView timer = activity.findViewById(R.id.walk_time);
            assertThat(timer.getText().toString()).isEqualTo(VALID_TIME_STR);
        });
    }

    @Test
    public void testStepCountUI() {
        MockFitnessService.nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);

            update(activity.tabFragment.stepCountFragment);

            startWalkButton.performClick();
        });
        MockFitnessService.nextStepCount = VALID_STEP_COUNT;
        scenario.onActivity(activity -> {
            update(activity.tabFragment.stepCountFragment);

            TextView walkSteps = activity.findViewById(R.id.walk_steps);

            assertThat(walkSteps.getText().toString()).isEqualTo(Long.toString(VALID_STEP_COUNT));
        });
    }

    @Test
    public void testStepDistanceUI() {
        MockFitnessService.nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);

            startWalkButton.performClick();
        });

        MockFitnessService. nextStepCount = VALID_STEP_COUNT;

        scenario.onActivity(activity -> {
            TextView walkDist = activity.findViewById(R.id.walk_dist);

            update(activity.tabFragment.stepCountFragment);

            assertThat(walkDist.getText().toString()).isEqualTo(String.format(activity.getString(R.string.dist_format), EXPECTED_DIST));
        });
    }

    private void update(StepCountFragment stepCountFragment) {
        stepCountFragment.getStepUpdate().update();
        stepCountFragment.getWalkUpdate().update();
    }
}
