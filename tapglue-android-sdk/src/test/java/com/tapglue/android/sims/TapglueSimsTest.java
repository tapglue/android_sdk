package com.tapglue.android.sims;

import android.content.Context;
import android.graphics.Bitmap;

import com.tapglue.android.Configuration;
import com.tapglue.android.internal.NotificationServiceIdStore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TapglueSimsTest {

    @Mock
    Context context;
    @Mock
    Configuration configuration;
    @Mock
    NotificationServiceIdStore notificationIdStore;
    private static final String ID = "someID";

    @Test
    public void storesNotificationServiceIdOnChanged() {
//        TapglueSims sims = new TapglueSims(configuration, context);
//        sims.notificationIdStore = notificationIdStore;
//        sims.idChanged(ID);
//
//        verify(notificationIdStore).store(ID);
    }
}
