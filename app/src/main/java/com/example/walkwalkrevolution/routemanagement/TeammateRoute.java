package com.example.walkwalkrevolution.routemanagement;

import com.example.walkwalkrevolution.account.IAccountInfo;

public class TeammateRoute {
    private IAccountInfo accountInfo;
    private Route route;

    public TeammateRoute(Route route, IAccountInfo info){
        this.route = route;
        this.accountInfo = info;
    }

    public Route getRoute(){ return this.route;}
    public IAccountInfo getAccountInfo(){ return this.accountInfo;}


}
