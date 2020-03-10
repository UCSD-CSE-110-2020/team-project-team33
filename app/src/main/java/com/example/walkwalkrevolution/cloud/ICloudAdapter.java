package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.util.ArrayList;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);
    
    public void getTeam(ITeamSubject teamSubject);
    
    public void getInvites(IInviteSubject inviteSubject);

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);
    
    public void saveRoutes(Iterable<Route> routeManager);

    public void getTeamRoutes(ITeammateRoutesSubject teammateRoutesSubject);

    public void acceptInvite(IAccountInfo account, IAcceptSubject acceptSubject);

    public void declineInvite(IAccountInfo account, IAcceptSubject acceptSubject);

    public void getRoutes(IRouteSubject routeSubject);

    public void getHeight(IHeightSubject heightSubject);

    public interface IInviteSubject {
        public void update(ArrayList<IAccountInfo> invites);
    }

    public interface ITeamSubject {
        public void update(ArrayList<Teammate> teamMembers);
    }

    public interface ITeammateRoutesSubject {
        public void update(ArrayList<TeammateRoute> teamRoutes);
    }

    public interface IAcceptSubject {
        public void update(String message);
    }

    public interface IRouteSubject {
        public void update(ArrayList<Route> routes);
    }

    public interface IHeightSubject {
        public void update(int height);
    }
}