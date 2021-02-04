package com.intellex.hometek.controller;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.linphone.LinphoneContext;
import org.linphone.LinphoneManager;
import org.linphone.core.Core;
import org.linphone.settings.LinphonePreferences;
import org.linphone.utils.LinphoneUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFBMsgSrv";

    private Runnable mPushReceivedRunnable =
            new Runnable() {
                @Override
                public void run() {
                    android.util.Log.d("FirebaseMessaging", "Runnable");
                    if (!LinphoneContext.isReady()) {
                        android.util.Log.d(
                                "FirebaseMessaging", "[Push Notification] Starting context");
                        new LinphoneContext(getApplicationContext());
                        LinphoneContext.instance().start(true);
                    } else {
                        android.util.Log.d(
                                "FirebaseMessaging", "[Push Notification] Notifying Core");
                        if (LinphoneManager.getInstance() != null) {
                            Core core = LinphoneManager.getCore();
                            if (core != null) {
                                core.ensureRegistered();
                            }
                        }
                    }
                }
            };

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data: " + remoteMessage.getData());
        Log.d(TAG, "ID: " + remoteMessage.getMessageId());
        Log.d(TAG, "toString: " + remoteMessage.toString());
        Log.d(
                TAG,
                "the value of key(notification_id): "
                        + remoteMessage.getData().get("notification_id"));
        Log.d(TAG, "the value of key(message): " + remoteMessage.getData().get("message"));

        if (remoteMessage.getData() != null
                && remoteMessage.getData().containsKey("notification_id")
                && remoteMessage.getData().containsKey("message")) {
        } else {
            Log.e(TAG, "推播資料格式不對, 應該是sip");

            LinphoneUtils.dispatchOnUIThread(mPushReceivedRunnable);
        }
    }

    @Override
    public void onNewToken(final String token) {
        super.onNewToken(token);

        Log.d(TAG, "onNewToken: " + token);
        LinphoneUtils.dispatchOnUIThread(
                new Runnable() {
                    @Override
                    public void run() {
                        LinphonePreferences.instance().setPushNotificationRegistrationID(token);
                    }
                });
    }
}
