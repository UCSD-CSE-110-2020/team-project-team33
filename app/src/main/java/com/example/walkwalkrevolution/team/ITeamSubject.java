package com.example.walkwalkrevolution.team;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.google.android.gms.common.data.DataBufferObserver;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public interface ITeamSubject {
    public void registerObserver(Observer o);
    public void deleteObserver(Observer o);
    public ArrayList<IAccountInfo> getTeamAccounts();
    public void notifyObservers(ArrayList<IAccountInfo> teamMembers);
    public void update(ArrayList<IAccountInfo> teamMembers);
}
