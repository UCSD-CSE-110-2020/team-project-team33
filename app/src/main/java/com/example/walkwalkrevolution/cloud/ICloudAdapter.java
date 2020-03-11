package com.example.walkwalkrevolution.cloud;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.routemanagement.Route;
import com.example.walkwalkrevolution.routemanagement.TeammateRoute;

import java.util.ArrayList;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account, IBooleanListener booleanListener);

    public void setUser(IAccountInfo account);
    
    public void getTeam(ITeammateListener teamSubject);
    
    public void getInvites(IAccountInfoListener inviteSubject);

    public void setUserListener();

    public void setTeamListener();

    public boolean userSet();

    public void invite(IAccountInfo recipient, Context context);
    
    public void saveRoutes(Iterable<Route> routeManager);

    public void getTeamRoutes(ITeammateRoutesListener teammateRoutesSubject);

    public void acceptInvite(IAccountInfo account, IStringListener acceptSubject);

    public void declineInvite(IAccountInfo account, IStringListener acceptSubject);

    public void getRoutes(IRouteListener routeSubject);

    public void getHeight(IIntListener heightSubject);

    public void isWalkProposed(IBooleanListener walkProposedSubject);

    public void isWalkScheduled(IBooleanListener proposedWalkSubject);

    public void scheduleWalk();

    public void cancelWalk();

    public void proposeWalk(TeammateRoute route, IBooleanListener accept);

    public void getProposedWalk(ITeammateRouteListener teammateRouteListener);

    public interface IAccountInfoListener {
        public void update(ArrayList<IAccountInfo> invites);
    }

    public interface ITeammateListener {
        public void update(ArrayList<Teammate> teamMembers);
    }

    public interface ITeammateRoutesListener {
        public void update(ArrayList<TeammateRoute> teamRoutes);
    }

    public interface ITeammateRouteListener {
        public void update(TeammateRoute teammateRoute);
    }

    public interface IStringListener {
        public void update(String message);
    }

    public interface IRouteListener {
        public void update(ArrayList<Route> routes);
    }

    public interface IIntListener {
        public void update(int height);
    }

    public interface IBooleanListener {
        public void update(boolean result);
    }
}