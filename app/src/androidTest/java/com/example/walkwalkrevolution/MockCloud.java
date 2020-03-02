package com.example.walkwalkrevolution;

import com.example.walkwalkrevolution.account.IAccountInfo;
import com.example.walkwalkrevolution.cloud.ICloudAdapter;

public class MockCloud implements ICloudAdapter {
    public static IAccountInfo account;

    @Override
    public void addAccount(IAccountInfo account) {
        this.account = account;
    }
}
