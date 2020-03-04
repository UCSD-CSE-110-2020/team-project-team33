package com.example.walkwalkrevolution.invite;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.google.android.gms.common.data.DataBufferObserver;

import java.util.ArrayList;
import java.util.Observable;

public interface IInviteSubject {
    public ArrayList<IAccountInfo> getInvites();
    public void update(ArrayList<IAccountInfo> invites);
}
