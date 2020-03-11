package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.google.android.material.tabs.TabLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class InviteUnitTests {

    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private MockRoutesManager routesManager;
    private ActivityScenario<TabActivity> scenario;

    private final int DEFAULT_HEIGHT = 72;

    private final String DEFAULT_FIRST_NAME = "FIRST";
    private final String DEFAULT_LAST_NAME = "LAST";
    private final String DEFAULT_EMAIL = "GMAIL@GMAIL.COM";

    @Before
    public void setUp() {
        routesManager = new MockRoutesManager();
        intent = new Intent(ApplicationProvider.getApplicationContext(), TabActivity.class);
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
        });        intent.putExtra(DataKeys.ACCOUNT_KEY, TEST_SERVICE);

        CloudAdapterFactory.put(TEST_SERVICE, MockCloud::new);
        intent.putExtra(DataKeys.CLOUD_KEY, TEST_SERVICE);
        MockCloud.reset();

        intent.putExtra(DataKeys.ROUTE_MANAGER_KEY, routesManager);
        intent.putExtra(DataKeys.USER_HEIGHT_KEY, DEFAULT_HEIGHT);

        scenario = ActivityScenario.launch(intent);
        scenario.onActivity(activity -> {
            ((TabLayout) activity.tabFragment.getView().findViewById(R.id.tabs)).getTabAt(3).select();
            activity.tabFragment
                    .teamFragment
                    .getView()
                    .findViewById(R.id.floatingActionButton)
                    .performClick();
        });
    }

    @Test
    public void testInvitingUser() {
        scenario.onActivity(activity -> {
            View view = activity.getSupportFragmentManager().getFragments().get(activity.getSupportFragmentManager().getFragments().size() - 1).getView();
            EditText editFirst = view.findViewById(R.id.first_name_text),
                     editLast = view.findViewById(R.id.last_name_text),
                     editEmail = view.findViewById(R.id.email_text);
            Button buttonInvite = view.findViewById(R.id.invite_btn);

            editFirst.setText(DEFAULT_FIRST_NAME);
            editLast.setText(DEFAULT_LAST_NAME);
            editEmail.setText(DEFAULT_EMAIL);
            buttonInvite.performClick();

            assertThat(MockAccountInfo.firstName).isEqualTo(DEFAULT_FIRST_NAME);
            assertThat(MockAccountInfo.lastName).isEqualTo(DEFAULT_LAST_NAME);
            assertThat(MockAccountInfo.gmail).isEqualTo(DEFAULT_EMAIL);
        });
    }

    @Test
    public void testEmptyFirstName() {
        scenario.onActivity(activity -> {
            View view = activity.getSupportFragmentManager().getFragments().get(activity.getSupportFragmentManager().getFragments().size() - 1).getView();
            EditText editFirst = view.findViewById(R.id.first_name_text),
                    editLast = view.findViewById(R.id.last_name_text),
                    editEmail = view.findViewById(R.id.email_text);
            Button buttonInvite = view.findViewById(R.id.invite_btn);

            editLast.setText(DEFAULT_LAST_NAME);
            editEmail.setText(DEFAULT_EMAIL);
            buttonInvite.performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.invite_first_name_error));
        });
    }

    @Test
    public void testEmptyLastName() {
        scenario.onActivity(activity -> {
            View view = activity.getSupportFragmentManager().getFragments().get(activity.getSupportFragmentManager().getFragments().size() - 1).getView();
            EditText editFirst = view.findViewById(R.id.first_name_text),
                    editLast = view.findViewById(R.id.last_name_text),
                    editEmail = view.findViewById(R.id.email_text);
            Button buttonInvite = view.findViewById(R.id.invite_btn);

            editFirst.setText(DEFAULT_FIRST_NAME);
            editEmail.setText(DEFAULT_EMAIL);
            buttonInvite.performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.invite_last_name_error));
        });
    }

    @Test
    public void testEmptyGmail() {
        scenario.onActivity(activity -> {
            View view = activity.getSupportFragmentManager().getFragments().get(activity.getSupportFragmentManager().getFragments().size() - 1).getView();
            EditText editFirst = view.findViewById(R.id.first_name_text),
                    editLast = view.findViewById(R.id.last_name_text),
                    editEmail = view.findViewById(R.id.email_text);
            Button buttonInvite = view.findViewById(R.id.invite_btn);

            editFirst.setText(DEFAULT_FIRST_NAME);
            editLast.setText(DEFAULT_LAST_NAME);
            buttonInvite.performClick();

            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(activity.getString(R.string.invite_email_error));
        });
    }
}
