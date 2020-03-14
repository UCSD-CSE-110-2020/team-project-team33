package com.example.walkwalkrevolution;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {
    private String channelName = "Invite";
    private String channelDescription = "for channel invites";
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(Constants.CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }

    }
}
