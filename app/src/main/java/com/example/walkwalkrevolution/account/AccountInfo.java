package com.example.walkwalkrevolution.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.walkwalkrevolution.DataKeys;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class AccountInfo implements IAccountInfo {
    private static final String TAG = "[AccountInfo]";

    private String firstName;
    private String lastName;
    private String gmail;

    public AccountInfo(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        Log.d(TAG, "Populating with account: " + account);
        firstName = account.getGivenName().toLowerCase();
        lastName = account.getFamilyName().toLowerCase();
        gmail = account.getEmail().toLowerCase();
    }

    public AccountInfo(String f, String l, String g) {
        firstName = f.toLowerCase();
        lastName = l.toLowerCase();
        gmail = g.toLowerCase();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGmail() {
        return gmail;
    }
}
