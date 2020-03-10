package com.example.walkwalkrevolution;

import android.content.Context;

import com.example.walkwalkrevolution.account.AccountInfo;
import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.cloud.Teammate;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.util.ArrayList;

public class MockCloud implements ICloudAdapter {
    public static IAccountInfo account;
    private String accountKey;

    public static ArrayList<TeammateRoute> teamRoutes;
    public static ArrayList<Route> route;
    public static ArrayList<Teammate> team;
    public static ArrayList<IAccountInfo> invites;

    public MockCloud(String accountKey) {
        this.accountKey = accountKey;
    }

    public static void resetArrays() {
        teamRoutes = new ArrayList<>();
        route = new ArrayList<>();
        team = new ArrayList<>();
        invites = new ArrayList<>();
    }

    @Override
    public void addAccount(IAccountInfo account) {
        this.account = account;
    }

    @Override
    public void setUser(IAccountInfo account) {
        this.account = account;
    }

    @Override
    public void getTeam(ITeamSubject teamSubject) {
        teamSubject.update(team);
    }

    @Override
    public void getInvites(IInviteSubject inviteSubject) {
        inviteSubject.update(invites);
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
        teammateRoutesSubject.update(teamRoutes);
    }

    @Override
    public void acceptInvite(IAccountInfo account, IAcceptSubject acceptSubject) {

    }

    @Override
    public void declineInvite(IAccountInfo account, IAcceptSubject acceptSubject) {

    }

    @Override
    public void getRoutes(IRouteSubject routeSubject) {

    }

    @Override
    public void getHeight(IHeightSubject heightSubject) {

    }
}
