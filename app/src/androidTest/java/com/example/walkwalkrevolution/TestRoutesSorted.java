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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
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
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestRoutesSorted {
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
    public void testRoutesSorted() {
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
        
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.floatingActionButton),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        withParent(withId(R.id.view_pager))),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());
        
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("b"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.saveButton), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                10)));
        appCompatButton2.perform(scrollTo(), click());
        
        ViewInteraction textView = onView(
                allOf(withId(R.id.itemRouteName), withText("b"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvRoutes),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("b")));
        
        ViewInteraction textView2 = onView(
                allOf(withId(R.id.itemRouteSteps), withText("0 steps"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("0 steps")));
        
        ViewInteraction textView3 = onView(
                allOf(withId(R.id.itemRouteDist), withText("0.00 mi"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("0.00 mi")));
        
        ViewInteraction textView4 = onView(
                allOf(withId(R.id.itemRouteTime), withText("00:00:00"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                2),
                        isDisplayed()));
        textView4.check(matches(withText("00:00:00")));
        
        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.floatingActionButton),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        withParent(withId(R.id.view_pager))),
                                1),
                        isDisplayed()));
        floatingActionButton2.perform(click());
        
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.routeName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText2.perform(scrollTo(), replaceText("a"), closeSoftKeyboard());
        
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.saveButton), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                10)));
        appCompatButton3.perform(scrollTo(), click());
        
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        ViewInteraction textView5 = onView(
                allOf(withId(R.id.itemRouteName), withText("a"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvRoutes),
                                        0),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("a")));
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
