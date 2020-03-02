package com.example.walkwalkrevolution.cloud;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CloudAdapterFactory {
    private static final String TAG = "[CloudAdapterFactory]";

    private static Map<String, CloudAdapterFactory.BluePrint> blueprints = new HashMap<>();

    public static void put(String key, CloudAdapterFactory.BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static ICloudAdapter create(String key) {
        Log.i(TAG, String.format("creating AccountInfo with key %s", key));
        return blueprints.get(key).create();
    }

    public interface BluePrint {
        ICloudAdapter create();
    }
}
