package com.example.walkwalkrevolution.team;

import com.example.walkwalkrevolution.account.AccountInfo;
import com.example.walkwalkrevolution.account.IAccountInfo;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TeamSubject extends Observable implements ITeamSubject {
    ArrayList<IAccountInfo> teamMembers;
    ArrayList<Observer> observers;
    public TeamSubject() {
        teamMembers = new ArrayList<>();
        observers = new ArrayList<>();
    }

    @Override
    public void update(ArrayList<IAccountInfo> newList) {
        teamMembers = newList;
        notifyObservers(newList);
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(ArrayList<IAccountInfo> newList) {
        for(Observer o: observers) {
            o.update(this, newList);
        }
    }

    @Override
    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    public ArrayList<IAccountInfo> getTeamAccounts() {
        return teamMembers;
    }

}
