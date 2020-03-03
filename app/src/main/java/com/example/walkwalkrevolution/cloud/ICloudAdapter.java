package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);

    public void saveRoutes(Iterable<Route> routeManager);
}
