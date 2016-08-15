package com.tapglue.android.sims;

import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface SimsService {
    @PUT("/0.4/me/devices/{deviceId}")
    Observable<Void> registerDevice(@Path("deviceId") String deviceUUID, @Body DevicePayload payload);
}
