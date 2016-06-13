package com.tapglue.android.sims;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.lang.ref.WeakReference;

public class SimsIdListenerService extends FirebaseInstanceIdService {
    private static NotificationServiceIdListener listener;
    private static String notificationServiceInstanceId;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        notificationServiceInstanceId = FirebaseInstanceId.getInstance().getToken();
        notifyListener();
        Log.d("Token listener: ", notificationServiceInstanceId);
    }

    public static void setListener(NotificationServiceIdListener listener) {
        SimsIdListenerService.listener = listener;
        notifyListener();
    }

    private static void notifyListener() {
        if(listener == null) {
            return;
        }
        if(notificationServiceInstanceId !=  null &&
                !notificationServiceInstanceId.isEmpty()) {
            listener.idChanged(notificationServiceInstanceId);
        }
    }
}
