package com.example.walkwalkrevolution;

//replaced by Save Activity UI Test
/*
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.RoutesManager;
import com.example.walkwalkrevolution.ui.main.EnterRouteInfoFragment;
import com.example.walkwalkrevolution.walktracker.WalkInfo;
import com.google.android.material.tabs.TabLayout;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Iterator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SaveRouteEspressoTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";
    private final long STEPS = 500;
    private final long TIME = 1200;
    private final double DISTANCE = 0.3;
    private final int HEIGHT = 72;
    private SharedPreferences sp;
    private RoutesManager routesManager;

    public SaveRouteEspressoTest(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sp = context.getSharedPreferences(DataKeys.USER_NAME_KEY, Context.MODE_PRIVATE);
        routesManager = new RoutesManager(sp);
    }

    @Rule
    public ActivityTestRule<TabActivity> activity = new ActivityTestRule<TabActivity>(TabActivity.class){
        @Override
        protected Intent getActivityIntent(){
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, TabActivity.class);
            intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
            intent.putExtra(DataKeys.STEPS_KEY, STEPS);
            intent.putExtra(DataKeys.DISTANCE_KEY, DISTANCE);
            intent.putExtra(DataKeys.TIME_KEY, TIME);
            FitnessServiceFactory.put(TEST_SERVICE, MockFitnessService::new);
            intent.putExtra(DataKeys.FITNESS_SERVICE_KEY, TEST_SERVICE);
            intent.putExtra(DataKeys.USER_HEIGHT_KEY, HEIGHT);
            return intent;
        }


    };


    @Before
    public void init(){
        activity.getActivity().launchEnterRouteInfo();

    }

    @Test
    public void testSaveWalk(){



        String routeName = "My Route";
        String startLoc = "home";

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.routeName)).perform(replaceText(routeName));

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.startLoc)).perform(replaceText(startLoc));

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.closeSoftKeyboard();



        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.saveButton)).perform(scrollTo());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.saveButton)).perform(click());

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //routesManager.saveRoute(sp,
        //       new Route(routeName, startLoc, STEPS, DISTANCE, TIME, null));

        assertEquals(routesManager.getRecentSteps(sp), STEPS);
        assertEquals(routesManager.getRecentTime(sp), TIME);
        assertEquals(routesManager.getRecentDistance(sp), DISTANCE);
    }
}
*/