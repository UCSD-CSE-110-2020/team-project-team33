package com.example.walkwalkrevolution;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.ui.main.StepCountFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartAndStopWalkTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private long nextStepCount = 247;


    @Rule
    public ActivityTestRule<HeightActivity> mActivityTestRule = new ActivityTestRule<HeightActivity>(HeightActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, HeightActivity.class);
            intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
            intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new MockRoutesManager());
            return intent;
        }
    };


    @Test
    public void startAndStopWalkTest() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.saveBtn), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.today_label), withText("Today"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tableLayout),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Today")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.walk_label), withText("Current/last walk"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tableLayout2),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Current/last walk")));

        ViewInteraction button = onView(
                allOf(withId(R.id.buttonStartWalk),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonStartWalk), withText("Start new walk/run"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button2 = onView(
                allOf(withId(R.id.buttonStartWalk), withText("Stop walk/run"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                2),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonStartWalk), withText("Stop walk/run"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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
