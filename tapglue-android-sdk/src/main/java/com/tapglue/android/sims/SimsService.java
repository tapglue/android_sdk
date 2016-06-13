package com.tapglue.android.sims;

import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SimsService {
    @PUT("/0.4/me/devices/{deviceId}")
    void registerDevice(@Path("deviceId") String deviceUUID, @Body DevicePayload payload);
}
