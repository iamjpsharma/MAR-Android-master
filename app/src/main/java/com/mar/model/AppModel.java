package com.mar.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mar.MainSwipeableActivity;

import java.io.Serializable;

public class AppModel implements Serializable {
    private static final String TAG = "AppModel";
    // TODO: 27-02-2019 Define App Details Model class and Database Codebase API
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private String AppName;
    private String PackageName;
    private Drawable appIcon;
    private long usageTimeMs;
    private int usagePercentage;

    public AppModel(String appName, Drawable appIcon) {
        this.AppName = appName;
        this.appIcon = appIcon;
    }

    public AppModel(String packageName, long usageTimeMs) {
        this.PackageName = packageName;
        this.usageTimeMs = usageTimeMs;
    }

    public int getUsageInPercent() {
        int percentage = 0;
        int h = (int) ((usageTimeMs / 1000) / 3600);
        int m = (int) (((usageTimeMs / 1000) / 60) % 60);
        int s = (int) ((usageTimeMs / 1000) % 60);
        percentage = (int) ((h / MainSwipeableActivity.totalUsage) * 100);
        Log.d(TAG, "UsageInPercent: " + percentage);
        return percentage;
    }

    public String getUsageLevel() {
        String level = "--";
        int perc = getUsageInPercent();
        if (perc <= 25) {
            return level = "Low";
        } else if (perc >= 25 && perc <= 50) {
            return level = "Medium";
        } else if (perc >= 50 && perc <= 75) {
            return level = "High";
        } else if (perc >= 75) {
            return level = "Extreme";
        }
        return level;
    }

    public String getUsageCount() {
        String count = null;
        int h = (int) ((usageTimeMs / 1000) / 3600);
        int m = (int) (((usageTimeMs / 1000) / 60) % 60);
        int s = (int) ((usageTimeMs / 1000) % 60);
        if (h == 0 && m == 0) {
            return count = m + " min " + s + " sec";
        }
        count = h + " hr " + m + " min";
        return count;
    }

    public String getUsageOfApp() {
        String usage = null;
        int h = (int) ((usageTimeMs / 1000) / 3600);
        int m = (int) (((usageTimeMs / 1000) / 60) % 60);
        int s = (int) ((usageTimeMs / 1000) % 60);
        if (h >= 15) {
            return usage = "Extreme";
        }
        if (h <= 14 && h >= 8) {
            return usage = "High";
        }
        if (h <= 7 && h >= 3) {
            return usage = "Medium";
        }
        if (h <= 3) {
            return usage = "Low";
        }
        if (h == 0) {
            return usage = "Low";
        }
        return usage;
    }

    public int getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(int usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public long getUsageTimeMs() {
        return usageTimeMs;
    }

    public void setUsageTimeMs(long usageTimeMs) {
        this.usageTimeMs = usageTimeMs;
    }
}
