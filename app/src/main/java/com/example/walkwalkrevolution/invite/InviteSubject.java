package com.example.walkwalkrevolution.invite;

import com.example.walkwalkrevolution.account.IAccountInfo;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class InviteSubject extends Observable implements IInviteSubject {
    ArrayList<IAccountInfo> invitees;
    ArrayList<Observer> observers;
    public InviteSubject() {
        invitees = new ArrayList<>();
        observers = new ArrayList<>();
    }
    public ArrayList<IAccountInfo> getInvites() {return invitees;}
    
    @Override
    public void update(ArrayList<IAccountInfo> newList) {
        invitees = newList;
        notifyAll();
    }
}
