package com.tapglue.android.sims;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SimsService {
    @PUT("/0.4/me/devices/{deviceId}")
    Observable<Void> registerDevice(@Path("deviceId") String deviceUUID, @Body DevicePayload payload);

    @DELETE("/0.4/me/devices/{deviceId}")
    Observable<Void> deleteDevice(@Path("deviceId") String deviceUUID);
}
