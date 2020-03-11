package com.example.walkwalkrevolution;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.walkwalkrevolution.account.AccountFactory;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.CloudAdapterFactory;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.routemanagement.IRouteManagement;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;
import com.example.walkwalkrevolution.ui.main.AcceptInviteFragment;
import com.example.walkwalkrevolution.ui.main.EnterRouteInfoFragment;
import com.example.walkwalkrevolution.ui.main.InviteFragment;
import com.example.walkwalkrevolution.ui.main.RouteInfoFragment;
import com.example.walkwalkrevolution.ui.main.TabFragment;
import com.example.walkwalkrevolution.walktracker.WalkInfo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

public class TabActivity extends AppCompatActivity {
    private static final String TAG = "[TabActivity]";

    public TabFragment tabFragment;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public IRouteManagement routesManager;

    private WalkInfo walkInfo;

    private FitnessService fitnessService;

    private IAccountInfo account;

    private ICloudAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Successfully launched step count activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        routesManager = (IRouteManagement) getIntent().getSerializableExtra(DataKeys.ROUTE_MANAGER_KEY);

        String fitnessServiceKey = getIntent().getStringExtra(DataKeys.FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        String accountKey = getIntent().getStringExtra(DataKeys.ACCOUNT_KEY);
        account = AccountFactory.create(accountKey, this);

        String cloudKey = getIntent().getStringExtra(DataKeys.CLOUD_KEY);
        db = CloudAdapterFactory.create(cloudKey, accountKey);
        db.setUser(account);
        walkInfo = new WalkInfo(getIntent().getIntExtra(DataKeys.USER_HEIGHT_KEY, 0), fitnessService);

        fragmentManager = getSupportFragmentManager();

        tabFragment = new TabFragment(this, walkInfo, routesManager, db);
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, tabFragment).commit();
    }

    public void launchEnterRouteInfo(boolean isSavingWalk) {
        Log.i(TAG, "Launching enter route info fragment");
        EnterRouteInfoFragment fragment = new EnterRouteInfoFragment(this, routesManager, walkInfo, isSavingWalk, db);
        launchFragment(fragment);
    }

    public void launchRouteInfo(Route route, boolean personalRoute) {
        Log.i(TAG, "Launching route info fragment");
        RouteInfoFragment fragment = new RouteInfoFragment(this, route, personalRoute, walkInfo, routesManager, db, account);
        launchFragment(fragment);
    }

    public void launchInvite() {
        Log.i(TAG, "Launching invite fragment");
        InviteFragment inviteFragment = new InviteFragment(db, this);
        launchFragment(inviteFragment);
    }

    public void launchAcceptInvites() {
        Log.i(TAG, "Launching AcceptInvites fragment");
        AcceptInviteFragment acceptInviteFragment = new AcceptInviteFragment(db);
        launchFragment(acceptInviteFragment);
    }

    public void launchProposedRouteFragment(TeammateRoute route) {
        Log.i(TAG, "Launching ProposedRouteFragment");
    }

    public void launchFragment(Fragment fragment) {
        fragmentManager.beginTransaction().hide(tabFragment).commit();
        currentFragment = fragment;
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.fragmentContainer, fragment).commit();
    }

    public void deleteFragment(Fragment fragment) {
        fragmentManager.beginTransaction().show(tabFragment).commit();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .remove(fragment).commit();
        currentFragment = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    public WalkInfo getWalkInfo() {
        return walkInfo;
    }

    public void setStepCount(long stepCount) {
        walkInfo.setSteps(stepCount);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment != null) {
            deleteFragment(currentFragment);
            currentFragment = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            deleteFragment(currentFragment);
            currentFragment = null;
            return true;
        }
        return false;
    }
}