package com.example.walkwalkrevolution.routemanagement;

import com.example.walkwalkrevolution.account.IAccountInfo;

public class TeammateRoute {
    private IAccountInfo accountInfo;
    private Route route;
    private boolean scheduled;
    private long scheduledTime;

    public TeammateRoute(Route route, IAccountInfo info){
        this.route = route;
        this.accountInfo = info;
    }

    public TeammateRoute(Route route, IAccountInfo info, boolean scheduled, long scheduledTime) {
        this(route, info);
        this.scheduled = scheduled;
        this.scheduledTime = scheduledTime;
    }

    public Route getRoute(){ return this.route;}
    public IAccountInfo getAccountInfo(){ return this.accountInfo;}

    public boolean isScheduled() {
        return scheduled;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }
}
