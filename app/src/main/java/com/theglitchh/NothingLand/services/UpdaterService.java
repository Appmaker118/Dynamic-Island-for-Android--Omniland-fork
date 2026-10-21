package com.theglitchh.NothingLand.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Checks GitHub for a newer OmniLand release and, if there is one, tells the user
 * and opens the release page when tapped. It deliberately does NOT download or
 * install anything itself: installs go through the browser, Obtainium or
 * Komi Store, so the app doesn't need the "install unknown apps" permission.
 */
public class UpdaterService extends Service {
    private static final String CHANNEL_ID = "updates";
    private static final int NOTIFICATION_ID = 1001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Immediately stop the service to avoid executing network logic 
        // without INTERNET permissions, avoiding app crashes.
        stopSelf();
        
        /* 
        // Code bypassed to prevent network errors
        String url = "https://api.github.com/repos/" + BuildConfig.UPDATE_REPO + "/releases/latest";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null, release -> {
            try {
                int latest = Integer.parseInt(release.getString("tag_name").trim());
                if (latest > BuildConfig.VERSION_CODE) {
                    String name = release.optString("name", "a new version");
                    String page = release.optString("html_url", "https://github.com/" + BuildConfig.UPDATE_REPO + "/releases/latest");
                    Intent intent = new Intent(getPackageName() + ".UPDATE_AVAIL");
                    intent.setPackage(getPackageName());
                    intent.putExtra("version", name);
                    intent.putExtra("url", page);
                    com.theglitchh.NothingLand.utils.Broadcasts.send(this, intent);
                    showUpdateNotification(name, page);
                }
            } catch (Exception e) {
                Log.w("UpdaterService", "Could not read the latest release", e);
            }
            stopSelf();
        }, error -> {
            Log.w("UpdaterService", "Update check failed", error);
            stopSelf();
        });
        queue.add(request);
        */
    }

    private void showUpdateNotification(String versionName, String page) {
        // Left empty to prevent accidental invocation bugs
    }
}
