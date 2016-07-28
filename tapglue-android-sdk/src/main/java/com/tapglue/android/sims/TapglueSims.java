package com.tapglue.android.sims;

import android.content.Context;

import com.tapglue.android.Configuration;
import com.tapglue.android.entities.User;
import com.tapglue.android.internal.NotificationServiceIdStore;
import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.UUIDStore;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Observer;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

public class TapglueSims implements NotificationServiceIdListener {

    private static AtomicBoolean isRegistered = new AtomicBoolean(false);

    NotificationServiceIdStore notificationIdStore;
    SessionStore sessionStore;
    UUIDStore uuidStore;
    final Configuration configuration;
    final Locale locale;

    public TapglueSims(Configuration configuration, Context context) {
        this.configuration = configuration;
        notificationIdStore = new NotificationServiceIdStore(context);
        sessionStore = new SessionStore(context);
        uuidStore = new UUIDStore(context);
        locale = context.getResources().getConfiguration().locale;
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
        if(isRegistered.get()) {
            return;
        }
        Observable.combineLatest(notificationIdStore.get(), sessionStore.get(), uuidStore.get(), new Func3<String, User, String, Void>() {
            @Override
            public Void call(String notificationId, User session, String uuid) {
                SimsServiceFactory serviceFactory = new SimsServiceFactory(configuration);
                serviceFactory.setSessionToken(session.getSessionToken());
                serviceFactory.setUserUUID(uuid);
                SimsService service = serviceFactory.createService();
                DevicePayload payload = new DevicePayload();
                payload.token = notificationId;
                payload.language = locale.toString();
                service.registerDevice(uuid, payload).subscribe();
                return null;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                isRegistered.set(true);
            }
        });
    }
}
