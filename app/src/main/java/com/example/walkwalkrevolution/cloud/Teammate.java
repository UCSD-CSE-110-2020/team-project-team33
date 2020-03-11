package com.example.walkwalkrevolution.cloud;

import com.example.walkwalkrevolution.Constants;
import com.example.walkwalkrevolution.account.IAccountInfo;

public class Teammate {
    private IAccountInfo account;
    private boolean pending;
    private int status = Constants.UNCOMMITED;

    public Teammate(IAccountInfo a, boolean p, int s) {
        account = a;
        pending = p;
        status = s;
    }

    public IAccountInfo getAccount() {
        return account;
    }

    public boolean isPending() {
        return pending;
    }

    public int getStatus() {
        return status;
    }
}
