package com.example.walkwalkrevolution;

import android.content.Context;

import com.example.walkwalkrevolution.account.IAccountInfo;

public class MockAccountInfo implements IAccountInfo {

    public static String firstName;
    public static String lastName;
    public static String gmail;

    public MockAccountInfo(Context context) {
        super();
    }

    public MockAccountInfo(String f, String l, String g) {
        firstName = f;
        lastName = l;
        gmail = g;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getGmail() {
        return gmail;
    }
}
