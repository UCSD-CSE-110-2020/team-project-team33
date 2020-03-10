package com.example.walkwalkrevolution;

import android.content.Context;

import com.example.walkwalkrevolution.account.AccountInfo;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;

public class MockCloud implements ICloudAdapter {
    public static IAccountInfo account;
    private String accountKey;
    private ArrayList<IAccountInfo> testTeam;


    public MockCloud(String accountKey) {
        testTeam = new ArrayList<>();
        testTeam.add(new AccountInfo("test1_first", "test1_last", "test1@gmail.com"));
        testTeam.add(new AccountInfo("test2_first", "test2_last", "test2@gmail.com"));
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
        teamSubject.update(testTeam);
    }

    @Override
    public void getInvites(IInviteSubject inviteSubject) {

    }

    @Override
    public boolean userSet() {
        return true;
    }

    @Override
    public void invite(IAccountInfo recipient, Context context) {

    }

    @Override
    public void saveRoutes(Iterable<Route> routeManager) {

    }

    @Override
    public void getTeamRoutes(ITeammateRoutesSubject teammateRoutesSubject) {

    }

    @Override
    public void acceptInvite(IAccountInfo account, IAcceptSubject acceptSubject) {

    }
}
