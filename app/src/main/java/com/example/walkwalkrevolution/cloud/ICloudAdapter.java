package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);
    
    public ArrayList<IAccountInfo> getTeam(IAccountInfo account);

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);
    
    public ArrayList<IAccountInfo> getInvites(IAccountInfo account);

    public void saveRoutes(Iterable<Route> routeManager);
}
