package com.example.walkwalkrevolution;

import android.content.Context;
import android.util.Log;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;
import com.example.walkwalkrevolution.cloud.Teammate;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.util.ArrayList;

public class MockCloud implements ICloudAdapter {
    private static final String TAG = "[MockCloud]";
    public static IAccountInfo account;
    private String accountKey;
    private ArrayList<ICloudAdapter.IDatabaseObserver> observers;

    public static ArrayList<TeammateRoute> teamRoutes;
    public static ArrayList<Route> routes;
    public static ArrayList<Teammate> team;
    public static ArrayList<IAccountInfo> invites;
    public static boolean walkProposed;
    public static boolean walkScheduled;
    public static TeammateRoute proposedWalk;
    public static IAccountInfo proposedAccount;
    public static long scheduledTime;

    public MockCloud(String accountKey) {
        this.accountKey = accountKey;
        observers = new ArrayList<>();
    }

    public static void reset() {
        teamRoutes = new ArrayList<>();
        routes = new ArrayList<>();
        team = new ArrayList<>();
        invites = new ArrayList<>();
        walkProposed = false;
        walkScheduled = false;
        proposedWalk = null;
        proposedAccount = null;
        scheduledTime = 0;
    }
    
    public static void setUserAccount(IAccountInfo newAccount) {
        account = newAccount;
    }

    @Override
    public void addAccount(IAccountInfo account, IBooleanListener booleanListener) {
        this.account = account;
        booleanListener.update(true);
    }

    @Override
    public void setUser(IAccountInfo account) {
        this.account = account;
    }

    @Override
    public void getTeam(ITeammateListener teamSubject) {
        teamSubject.update(team);
    }

    @Override
    public void getInvites(IAccountInfoListener inviteSubject) {
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
        routes = new ArrayList<>();
        for (Route route : routeManager) {
            routes.add(route);
        }
    }

    @Override
    public void getTeamRoutes(ITeammateRoutesListener teammateRoutesSubject) {
        teammateRoutesSubject.update(teamRoutes);
    }

    @Override
    public void acceptInvite(IAccountInfo account, IStringListener acceptSubject) {
        invites.remove(account);
        acceptSubject.update("Invite accepted");
        notifyObservers();
    }

    @Override
    public void declineInvite(IAccountInfo account, IStringListener acceptSubject) {
        invites.remove(account);
        acceptSubject.update("Invite declined");
        notifyObservers();
    }

    @Override
    public void getRoutes(IRouteListener routeSubject) {
        routeSubject.update(routes);
    }

    @Override
    public void getHeight(IIntListener heightSubject) {

    }

    @Override
    public void isWalkProposed(IBooleanListener walkProposedSubject) {
        walkProposedSubject.update(walkProposed);
    }

    @Override
    public void isWalkScheduled(IBooleanListener proposedWalkSubject) {
        proposedWalkSubject.update(walkScheduled);
    }

    @Override
    public void scheduleWalk() {
        walkScheduled = true;
        notifyObservers();
    }

    @Override
    public void cancelWalk() {
        walkProposed = false;
        notifyObservers();
    }

    @Override
    public void proposeWalk(TeammateRoute route, IBooleanListener accept) {
        Log.i(TAG, "Route is proposed by: " + route.getAccountInfo().getFirstName());
        proposedWalk = route;
        walkProposed = true;
        notifyObservers();
    }

    @Override
    public void getProposedWalk(ITeammateRouteListener teammateRouteListener) {
        teammateRouteListener.update(new TeammateRoute(proposedWalk.getRoute(), proposedAccount, walkScheduled, scheduledTime));
    }

    @Override
    public void addObserver(IDatabaseObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for(IDatabaseObserver observer : observers) {
            observer.update();
        }
    }

    @Override
    public void acceptWalkInvite() {

    }

    @Override
    public void declineWalkInvite() {

    }
}
