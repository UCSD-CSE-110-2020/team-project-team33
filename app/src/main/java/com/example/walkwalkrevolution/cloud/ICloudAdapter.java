package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.invite.IInviteSubject;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.team.ITeamSubject;

import java.util.ArrayList;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);
    
    public void getTeam(ITeamSubject teamSubject);
    
    public void getInvites(IInviteSubject inviteSubject);

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);
    
    public void saveRoutes(Iterable<Route> routeManager);
}
