package com.example.walkwalkrevolution;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.routemanagement.Route;

public class MockCloud implements ICloudAdapter {
    public static IAccountInfo account;
    private String accountKey;

    public MockCloud(String accountKey) {
        this.accountKey = accountKey;
    }

    @Override
    public void addAccount(IAccountInfo account) {
        this.account = account;
    }

    @Override
    public void setUser(IAccountInfo account) {

    }

    @Override
    public void getTeam(ITeamSubject teamSubject) {

    }

    @Override
    public void getInvites(IInviteSubject inviteSubject) {

    }

    @Override
    public boolean userSet() {
        return false;
    }

    @Override
    public void invite(IAccountInfo recipient, Context context) {

    }

    @Override
    public void saveRoutes(Iterable<Route> routeManager) {

    }

    @Override
    public void acceptInvite(IAccountInfo account, IAcceptSubject acceptSubject) {

    }
}
