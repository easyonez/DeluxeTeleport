package com.pixesoj.deluxeteleport.utils;

public class ServerInfo {
    private static ServerVersion serverVersion;

    public static void setServerVersion(ServerVersion version) {
        serverVersion = version;
    }

    public static ServerVersion getServerVersion() {
        return serverVersion;
    }
}