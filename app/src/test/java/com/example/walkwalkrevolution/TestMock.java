package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class TestMock {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int POSATIVE_STEP_COUNT = 237;
    private static final int NEGATIVE_STEP_COUNT = -5;
    private static final int VALID_HEIGHT = 63;

    private Intent intent;

    @Before
    public void setUp() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);

        FitnessServiceFactory.put(TEST_SERVICE, MockFitnessService::new);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);

        AccountFactory.put(TEST_SERVICE, MockAccountInfo::new);
        intent.putExtra(DataKeys.ACCOUNT_KEY, TEST_SERVICE);

        CloudAdapterFactory.put(TEST_SERVICE, MockCloud::new);
        intent.putExtra(DataKeys.CLOUD_KEY, TEST_SERVICE);

        intent.putExtra(DataKeys.USER_HEIGHT_KEY, VALID_HEIGHT);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new MockRoutesManager());
    }

    @Test
    public void testWalkInfoMocking() {
        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.getWalkInfo().setMocking(true);
            activity.getWalkInfo().setSteps(POSATIVE_STEP_COUNT);
            activity.tabFragment.stepCountFragment.getStepUpdate().update();

            TextView steps = activity.findViewById(R.id.overall_steps);
            assertThat(steps.getText().toString()).isEqualTo(Long.toString(POSATIVE_STEP_COUNT));
        });
    }

    @Test
    public void testWalkInfoWalkMocking() {
        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.getWalkInfo().setMocking(true);
            activity.getWalkInfo().setSteps(POSATIVE_STEP_COUNT);
            activity.tabFragment.stepCountFragment.getStepUpdate().update();
            activity.tabFragment.stepCountFragment.getWalkUpdate().update();
            activity.getWalkInfo().startWalk();
            activity.getWalkInfo().setSteps(POSATIVE_STEP_COUNT + POSATIVE_STEP_COUNT);


            TextView steps = activity.findViewById(R.id.walk_steps);
            assertThat(steps.getText().toString()).isEqualTo(Long.toString(POSATIVE_STEP_COUNT));
        });
    }
}
