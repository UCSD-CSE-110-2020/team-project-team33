package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@LooperMode(PAUSED)
public class DistanceUnitTest {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int VALID_HEIGHT = 63;
    private static final int VALID_STEP_COUNT = 247;

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, DistanceUnitTest.TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, VALID_HEIGHT);
    }

    @Test
    public void testNormalDistanceReturned() {
        Distance dist = new Distance(VALID_HEIGHT);
        double expect = (((VALID_HEIGHT * 0.413) / 12 ) / 5280) * VALID_STEP_COUNT;
        double actual = dist.calculateDistance(VALID_STEP_COUNT);
        assertTrue(Math.abs(expect - actual) < 0.001);
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
        nextStepCount = VALID_STEP_COUNT;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textDist = activity.findViewById(R.id.overall_dist);
            assertThat(textDist.getText().toString()).isEqualTo("0.1 mi");
        });
    }

    @Test
    public void testUIZeroHeightDistanceShown() {
        nextStepCount = VALID_STEP_COUNT;
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, 0);

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            TextView textDist = activity.findViewById(R.id.overall_dist);
            assertThat(textDist.getText().toString()).isEqualTo("0.0 mi");
        });
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private StepCountFragment stepCountFragment;

        public TestFitnessService(StepCountFragment stepCountFragment) {
            this.stepCountFragment = stepCountFragment;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            stepCountFragment.setStepCount(nextStepCount);
        }
    }
}
