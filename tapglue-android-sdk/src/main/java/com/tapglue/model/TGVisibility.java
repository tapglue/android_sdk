package com.tapglue.model;

/**
 * Created by adrian on 11.01.16.
 */
public enum TGVisibility {
    Private(10), Connections(20), Public(30), Global(40);

    private final int mValue;

    public static TGVisibility fromValue(Integer valueToFind) {
        if (valueToFind == null) {
            return Private;
        }
        for (TGVisibility value : values()) {
            if (value.asValue().intValue() == valueToFind.intValue()) {
                return value;
            }
        }
        return Private;
    }

    TGVisibility(int realValue) {
        mValue = realValue;
    }

    public Integer asValue() {
        return mValue;
    }
}
