package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;

import java.util.ArrayList;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);
    
    public void getTeam(ITeamSubject teamSubject);
    
    public void getInvites(IInviteSubject inviteSubject);

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);
    
    public void saveRoutes(Iterable<Route> routeManager);

    public interface IInviteSubject {
        public void update(ArrayList<IAccountInfo> invites);
    }

    public interface ITeamSubject {
        public void update(ArrayList<IAccountInfo> teamMembers);
    }
}