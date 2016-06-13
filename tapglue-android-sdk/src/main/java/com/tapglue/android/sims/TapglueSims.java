package com.tapglue.android.sims;

import android.content.Context;

import com.tapglue.android.Configuration;
import com.tapglue.android.entities.User;
import com.tapglue.android.internal.NotificationServiceIdStore;
import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.UUIDStore;

import rx.Observable;
import rx.functions.Func3;

public class TapglueSims implements NotificationServiceIdListener {

    NotificationServiceIdStore notificationIdStore;
    SessionStore sessionStore;
    UUIDStore uuidStore;
    final Configuration configuration;

    public TapglueSims(Configuration configuration, Context context) {
        this.configuration = configuration;
        notificationIdStore = new NotificationServiceIdStore(context);
        sessionStore = new SessionStore(context);
        uuidStore = new UUIDStore(context);
        SimsIdListenerService.setListener(this);
    }

    @Override
    public void idChanged(String id) {
        notificationIdStore.store(id);
        registerDeviceForSims();
    }

    public void sessionTokenChanged() {
       registerDeviceForSims();
    }

    private void registerDeviceForSims() {
        Observable.combineLatest(notificationIdStore.get(), sessionStore.get(), uuidStore.get(), new Func3<String, User, String, Void>() {
            @Override
            public Void call(String notificationId, User session, String uuid) {
                SimsServiceFactory serviceFactory = new SimsServiceFactory(configuration);
                serviceFactory.setSessionToken(session.getSessionToken());
                serviceFactory.setUserUUID(uuid);
                SimsService service = serviceFactory.createService();
                DevicePayload payload = new DevicePayload();
                payload.token = notificationId;
                payload.language = "en-US";
                service.registerDevice(uuid, payload);
                return null;
            }

        }).subscribe();
    }
}
