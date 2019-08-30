package com.mar.appmonitor.detector;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mar.appmonitor.AppMonitorUtil;

import java.util.List;

public class LollipopDetector implements Detector {
    private static final String TAG = "LollipopDetector";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public String getForegroundApp(Context context) {
        if (!AppMonitorUtil.hasUsageStatsPermission(context)) {
            return null;
        }
        String foregroundApp = null;

       /* UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Service.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();

        assert mUsageStatsManager != null;
        UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 3600, time);
        UsageEvents.Event event = new UsageEvents.Event();
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                foregroundApp = event.getPackageName();
            }
        }
        return foregroundApp;*/

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Service.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        // get usage stats for the last 10 seconds
        assert mUsageStatsManager != null;
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 3600, currentTime);
        // search for app with most recent last used time
        if (stats != null) {
            long lastUsedAppTime = 0;
            for (UsageStats usageStats : stats) {
                if (usageStats.getLastTimeUsed() > lastUsedAppTime) {
                    foregroundApp = usageStats.getPackageName();
                    lastUsedAppTime = usageStats.getLastTimeUsed();
                }
            }
        } else {
            Log.i(TAG, "getForegroundApp: Stats Query is Null");
        }
        return foregroundApp;

        /*String topPackageName = null;
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats;
        if (mUsageStatsManager != null) {
            stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    topPackageName = Objects.requireNonNull(mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                } else {
                    Log.i(TAG, "getForegroundApp: sortMap is Empty");
                }
            } else {
                Log.i(TAG, "getForegroundApp: stats empty !");
            }
        } else {
            Log.i(TAG, "getForegroundApp: mUsageStatsManager is Null !");
        }
        return topPackageName;*/
    }
}
