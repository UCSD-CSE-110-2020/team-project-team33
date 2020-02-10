package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.Button;
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
import org.robolectric.annotation.LooperMode;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StartWalkTests {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private static final int POSATIVE_STEP_COUNT = 237;
    private static final int NEGATIVE_STEP_COUNT = -5;

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);
        intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, 60);
        intent.putExtra(DataKeys.MOCKING_KEY, true);
        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new MockRoutesManager());
    }

    @Test
    public void startButtonChangesText() {
        nextStepCount = 0;

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
        nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            assertThat(activity.stepCountFragment.formatTime(5590000)).isEqualTo("01:33:10");
        });
    }

    @Test
    public void testTimerUI() {
        nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);
            startWalkButton.performClick();
            activity.stepCountFragment.updateWalk(5590000);
            TextView timer = activity.findViewById(R.id.walk_time);
            assertThat(timer.getText().toString()).isEqualTo("01:33:10");
        });
    }

    @Test
    public void testStepCountUI() {
        nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);
            activity.stepCountFragment.updateSteps();
            activity.stepCountFragment.updateWalk();
            startWalkButton.performClick();
        });
        nextStepCount = POSATIVE_STEP_COUNT;
        scenario.onActivity(activity -> {
            activity.stepCountFragment.updateSteps();
            activity.stepCountFragment.updateWalk();
            TextView walkSteps = activity.findViewById(R.id.walk_steps);
            assertThat(walkSteps.getText().toString()).isEqualTo(Long.toString(POSATIVE_STEP_COUNT));
        });
    }

    @Test
    public void testStepDistanceUI() {
        nextStepCount = 0;

        ActivityScenario<TabActivity> scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            Button startWalkButton = activity.findViewById(R.id.buttonStartWalk);
            activity.stepCountFragment.updateSteps();
            activity.stepCountFragment.updateWalk();
            startWalkButton.performClick();
        });
        nextStepCount = POSATIVE_STEP_COUNT;
        scenario.onActivity(activity -> {
            activity.stepCountFragment.updateSteps();
            activity.stepCountFragment.updateWalk();
            TextView walkDist = activity.findViewById(R.id.walk_dist);
            assertThat(walkDist.getText().toString()).isEqualTo("0.1 mi");
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
