package com.example.walkwalkrevolution.invite;

import com.example.walkwalkrevolution.account.IAccountInfo;

import java.util.ArrayList;

public interface IInviteSubject {
    public ArrayList<IAccountInfo> getInvites();
    
    public void update(ArrayList<IAccountInfo> invites);
}
