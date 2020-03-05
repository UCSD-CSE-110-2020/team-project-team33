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
    
    private IAccountInfo user1 = new AccountInfo("test1_first", "test1_last", "test1@gmail.com");
    private IAccountInfo user2 = new AccountInfo("test2_first", "test2_last", "test2@gmail.com");
    private ArrayList<IAccountInfo> testTeam = new ArrayList<>();
    
    
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
        testTeam.add(user1);
        testTeam.add(user2);
        teamSubject.update(testTeam);
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
}
