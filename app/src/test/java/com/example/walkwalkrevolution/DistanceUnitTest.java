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
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class DistanceUnitTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int VALID_HEIGHT = 63;
    private static final int VALID_STEP_COUNT = 247;
    private static final double EXPECTED_DIST = (((VALID_HEIGHT * 0.413) / 12 ) / 5280) * VALID_STEP_COUNT;

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
    public void testNormalDistanceReturned() {
        Distance dist = new Distance(VALID_HEIGHT);
        double actual = dist.calculateDistance(VALID_STEP_COUNT);
        assertTrue(Math.abs(EXPECTED_DIST - actual) < 0.001);
    }

    @Test
    public void testZeroHeightDistanceReturned() {
        Distance dist = new Distance(0);
        assertThat(dist.calculateDistance(VALID_STEP_COUNT)).isEqualTo(0.0);
    }

    @Test
    public void testZeroStepsDistanceReturned() {
        Distance dist = new Distance(VALID_HEIGHT);
        assertThat(dist.calculateDistance(0)).isEqualTo(0.0);
    }

    @Test
    public void testUINormalDistanceShown() {
        MockFitnessService.nextStepCount = VALID_STEP_COUNT;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.tabFragment.stepCountFragment.getStepUpdate().update();
            TextView textDist = activity.findViewById(R.id.overall_dist);
            assertThat(textDist.getText().toString())
                    .isEqualTo(String.format(activity
                            .getString(R.string.dist_format), EXPECTED_DIST));
        });
    }

    @Test
    public void testUIZeroHeightDistanceShown() {
        MockFitnessService.nextStepCount = VALID_STEP_COUNT;
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, 0);

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textDist = activity.findViewById(R.id.overall_dist);
            assertThat(textDist.getText().toString())
                    .isEqualTo(String.format(activity.getString(R.string.dist_format), 0.0));
        });
    }
}
