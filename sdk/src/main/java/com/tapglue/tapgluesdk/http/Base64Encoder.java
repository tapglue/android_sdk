package com.tapglue.tapgluesdk.http;

import android.util.Base64;

import java.io.IOException;

/**
 * Created by John on 4/6/16.
 */
public class Base64Encoder {

    public String encode(String encode) throws IOException{
        return Base64.encodeToString(encode.getBytes("UTF-8"), Base64.NO_WRAP);
    }
}
