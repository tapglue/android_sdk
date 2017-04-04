package com.tapglue.android.sims;

import android.content.Context;

import com.tapglue.android.Configuration;
import com.tapglue.android.entities.User;
import com.tapglue.android.internal.NotificationServiceIdStore;
import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.UUIDStore;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;


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
        try {
            notificationIdStore.store(id);
            registerDeviceForSims();
        } catch (Exception e) {
            // TODO handle error
        }
    }

    public void sessionTokenChanged() throws Exception{
       registerDeviceForSims();
    }

    public void unregisterDevice() throws Exception {
        if(!isRegistered.get()) {
            return;
        }
        Observable<DeviceRegistrationParams> parameterGathering = Observable.combineLatest(notificationIdStore.get(), sessionStore.get(), uuidStore.get(), new Function3<String, User, String, DeviceRegistrationParams>() {
            @Override
            public DeviceRegistrationParams apply(String notificationId, User session, String uuid) {
                DeviceRegistrationParams params = new DeviceRegistrationParams();
                params.uuid = uuid;
                params.session = session;
                return params;
            }
        });

        parameterGathering.flatMap(new Function<DeviceRegistrationParams, ObservableSource<Void>>() {
            @Override
            public Observable<Void> apply(DeviceRegistrationParams params) {
                SimsServiceFactory serviceFactory = new SimsServiceFactory(configuration);
                serviceFactory.setSessionToken(params.session.getSessionToken());
                serviceFactory.setUserUUID(params.uuid);
                SimsService service = serviceFactory.createService();
                return service.deleteDevice(params.uuid);
            }
        }).subscribeOn(Schedulers.io())
        .subscribe(new Observer<Void>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Void aVoid) {
                isRegistered.set(false);
            }

            @Override
            public void onError(Throwable e) {
                sessionStore.clear();
            }

            @Override
            public void onComplete() {
                sessionStore.clear();
            }
        });
    }

    private void registerDeviceForSims() throws Exception {
        if(isRegistered.get()) {
            return;
        }
        Observable<DeviceRegistrationParams> parameterGathering = Observable.combineLatest(notificationIdStore.get(), sessionStore.get(), uuidStore.get(), new Function3<String, User, String, DeviceRegistrationParams>() {
            @Override
            public DeviceRegistrationParams apply(String notificationId, User session, String uuid) {
                DevicePayload payload = new DevicePayload();
                payload.token = notificationId;
                payload.language = locale.toString();
                DeviceRegistrationParams params = new DeviceRegistrationParams();
                params.payload = payload;
                params.uuid = uuid;
                params.session = session;
                return params;
            }
        });

        parameterGathering.flatMap(new Function<DeviceRegistrationParams, Observable<Void>>() {

            @Override
            public Observable<Void> apply(DeviceRegistrationParams params) {
                SimsServiceFactory serviceFactory = new SimsServiceFactory(configuration);
                serviceFactory.setSessionToken(params.session.getSessionToken());
                serviceFactory.setUserUUID(params.uuid);
                SimsService service = serviceFactory.createService();
                return service.registerDevice(params.uuid, params.payload);
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Observer<Void>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Void aVoid) {
                isRegistered.set(true);
            }
        });
    }

    private static class DeviceRegistrationParams {
        String uuid;
        DevicePayload payload;
        User session;
    }
}
