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

import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleEspressoTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<HeightActivity> mActivityTestRule = new ActivityTestRule<HeightActivity>(HeightActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            FitnessServiceFactory.put(TEST_SERVICE, MockFitnessService::new);
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, HeightActivity.class);
            intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
            intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new RoutesManager());
            return intent;
        }
    };

    @Test
    public void exampleEspressoTest() {
        MockFitnessService.nextStepCount = 247;
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        ViewInteraction appCompatButton = onView(
                allOf(isDisplayed(), withId(R.id.saveBtn), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(isDisplayed(), withId(R.id.overall_steps), withText(String.valueOf(MockFitnessService.nextStepCount)),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tableLayout),
                                        1),
                                1),
                        isDisplayed()));
        textView.check(matches(withText(String.valueOf(MockFitnessService.nextStepCount))));

        ViewInteraction textView2 = onView(
                allOf(isDisplayed(), withId(R.id.overall_dist), withText("0.12 mi"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tableLayout),
                                        2),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("0.12 mi")));
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
}
