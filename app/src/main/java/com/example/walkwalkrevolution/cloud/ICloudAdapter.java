package com.example.walkwalkrevolution.cloud;

import com.example.walkwalkrevolution.account.IAccountInfo;

public interface ICloudAdapter {
    public void addAccount(IAccountInfo account);

    public void setUser(IAccountInfo account);

    public boolean userSet();
}
