package com.example.walkwalkrevolution.cloud;

import com.example.walkwalkrevolution.account.IAccountInfo;

public class Teammate {
    private IAccountInfo account;
    private boolean pending;

    public Teammate(IAccountInfo a, boolean p) {
        account = a;
        pending = p;
    }

    public IAccountInfo getAccount() {
        return account;
    }

    public boolean isPending() {
        return pending;
    }
}
