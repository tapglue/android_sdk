package com.tapglue.sdk.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConnectionList {
    private final List<Connection> incoming;
    private final List<Connection> outgoing;

    public ConnectionList(List<Connection> incoming, List<Connection> outgoing) {
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public List<Connection> getIncomingConnections() {
        return incoming;
    }

    public List<Connection> getOutgoingConnections() {
        return outgoing;
    }
}