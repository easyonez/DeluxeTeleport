package com.pixesoj.deluxeteleport.model.internal;

public class UpdateCheckResult {
    private final String latestVersion;
    private final boolean error;
    private final boolean updateAvailable;
    private final String downloadLink;

    private UpdateCheckResult(String latestVersion, boolean error, boolean updateAvailable, String downloadLink) {
        this.latestVersion = latestVersion;
        this.error = error;
        this.updateAvailable = updateAvailable;
        this.downloadLink = downloadLink;
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }

    public boolean isError() {
        return this.error;
    }

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }

    public String getDownloadLink() {
        return this.downloadLink;
    }

    public static UpdateCheckResult updateAvailable(String latestVersion, String downloadLink) {
        return new UpdateCheckResult(latestVersion, false, true, downloadLink);
    }

    public static UpdateCheckResult noUpdate(String latestVersion) {
        return new UpdateCheckResult(latestVersion, false, false, null);
    }

    public static UpdateCheckResult error() {
        return new UpdateCheckResult(null, true, false, null);
    }
}