package com.example.walkwalkrevolution.account;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class AccountFactory {

    private static final String TAG = "[AccountFactory]";

    private static Map<String, AccountFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, AccountFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static IAccountInfo create(String key, Context context) {
        Log.i(TAG, String.format("creating AccountInfo with key %s", key));
        return blueprints.get(key).create(context);
    }

    public interface BluePrint {
        IAccountInfo create(Context context);
    }
}
