package com.example.walkwalkrevolution.routemanagement;

import com.example.walkwalkrevolution.account.IAccountInfo;

public class TeammateRoutes {
    private IAccountInfo accountInfo;
    private Route route;

    public TeammateRoutes(Route route, IAccountInfo info){
        this.route = route;
        this.accountInfo = info;
    }

    public Route getRoute(){ return this.route;}
    public IAccountInfo getAccountInfo(){ return this.accountInfo;}


}
