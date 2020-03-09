package com.example.walkwalkrevolution;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestDisplayRouteFeatures {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<TabActivity>(TabActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, TabActivity.class);

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
            });
            intent.putExtra(DataKeys.ACCOUNT_KEY, TEST_SERVICE);

            CloudAdapterFactory.put(TEST_SERVICE, MockCloud::new);
            intent.putExtra(DataKeys.CLOUD_KEY, TEST_SERVICE);

            intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, new RoutesManager());
            return intent;
        }
    };

    @Test
    public void testDisplayRouteFeatures() {
        MockFitnessService.nextStepCount = 0;

        ViewInteraction tabView = onView(
                allOf(withContentDescription("Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tabs),
                                        0),
                                1),
                        isDisplayed()));
        tabView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        withParent(withId(R.id.view_pager))),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("MyRoute"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.routeName), withText("MyRoute"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.routeType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.surfaceType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatSpinner2.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.roadType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatSpinner3.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatSpinner4 = onView(
                allOf(withId(R.id.difficultyType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        appCompatSpinner4.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatSpinner5 = onView(
                allOf(withId(R.id.terrainType),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatSpinner5.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.saveButton), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                10)));
        appCompatButton2.perform(scrollTo(), click());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.rvRoutes),
                                childAtPosition(
                                        withId(R.id.constraintLayout),
                                        0)),
                        1),
                        isDisplayed()));
        linearLayout.perform(click());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withText("Route Features"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.route_features_table),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Route Features")));

        ViewInteraction textView2 = onView(
                allOf(withText("Type:"),
                        childAtPosition(
                                allOf(withId(R.id.type_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                1)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Type:")));

        ViewInteraction textView3 = onView(
                allOf(withText("Surface:"),
                        childAtPosition(
                                allOf(withId(R.id.surface_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                2)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Surface:")));

        ViewInteraction textView4 = onView(
                allOf(withText("Road:"),
                        childAtPosition(
                                allOf(withId(R.id.road_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                3)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Road:")));

        ViewInteraction textView5 = onView(
                allOf(withText("Difficulty:"),
                        childAtPosition(
                                allOf(withId(R.id.difficulty_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                4)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Difficulty:")));

        ViewInteraction textView6 = onView(
                allOf(withText("Terrain:"),
                        childAtPosition(
                                allOf(withId(R.id.terrain_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                5)),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Terrain:")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.route_type_text), withText("Loop"),
                        childAtPosition(
                                allOf(withId(R.id.type_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                1)),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("Loop")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.route_surface_text), withText("Even"),
                        childAtPosition(
                                allOf(withId(R.id.surface_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                2)),
                                1),
                        isDisplayed()));
        textView8.check(matches(withText("Even")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.route_road_text), withText("Street"),
                        childAtPosition(
                                allOf(withId(R.id.road_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                3)),
                                1),
                        isDisplayed()));
        textView9.check(matches(withText("Street")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.route_difficulty_text), withText("Easy"),
                        childAtPosition(
                                allOf(withId(R.id.difficulty_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                4)),
                                1),
                        isDisplayed()));
        textView10.check(matches(withText("Easy")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.route_terrain_text), withText("Flat"),
                        childAtPosition(
                                allOf(withId(R.id.terrain_row),
                                        childAtPosition(
                                                withId(R.id.route_features_table),
                                                5)),
                                1),
                        isDisplayed()));
        textView11.check(matches(withText("Flat")));
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
