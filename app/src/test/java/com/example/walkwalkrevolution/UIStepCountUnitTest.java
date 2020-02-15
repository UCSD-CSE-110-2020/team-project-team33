package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.TextView;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class UIStepCountUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int POSATIVE_STEP_COUNT = 237;
    private static final int NEGATIVE_STEP_COUNT = -5;
    private static final int VALID_HEIGHT = 63;

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, VALID_HEIGHT);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new MockRoutesManager());
    }

    @Test
    public void testPosativeSteps() {
        nextStepCount = POSATIVE_STEP_COUNT;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.stepCountFragment.getStepUpdate().update();
            TextView textSteps = activity.findViewById(R.id.overall_steps);
            assertThat(textSteps.getText().toString()).isEqualTo(Long.toString(nextStepCount));
        });
    }

    @Test
    public void testNegativeSteps() {
        nextStepCount = NEGATIVE_STEP_COUNT;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.stepCountFragment.getStepUpdate().update();
            TextView textSteps = activity.findViewById(R.id.overall_steps);
            assertThat(textSteps.getText().toString()).isEqualTo(Long.toString(nextStepCount));
        });
    }

    @Test
    public void testChangedSteps() {
        nextStepCount = POSATIVE_STEP_COUNT;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            activity.stepCountFragment.getStepUpdate().update();
        });
        nextStepCount += nextStepCount;
        scenario.onActivity(activity -> {
            activity.stepCountFragment.getStepUpdate().update();
            TextView textSteps = activity.findViewById(R.id.overall_steps);
            assertThat(textSteps.getText().toString()).isEqualTo(Long.toString(nextStepCount));
        });
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private TabActivity tabActivity;

        public TestFitnessService(TabActivity tabActivity) {
            this.tabActivity = tabActivity;
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
            tabActivity.setStepCount(nextStepCount);
        }
    }
}
