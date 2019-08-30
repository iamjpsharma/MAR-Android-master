package com.mar.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppUsageDataUtils {
    private static final String TAG = "AppUsageDataUtils";
    private static List<ApplicationInfo> mApplicationInfo = null;
    private static List<String> mInstalledRestrictedApps = null;
    private static PackageManager packageManager;
    private Context mContext;

    public AppUsageDataUtils(Context context) {
        mApplicationInfo = new ArrayList<>();
        mInstalledRestrictedApps = new ArrayList<>();
        this.mContext = context;
        packageManager = context.getPackageManager();
    }

    public List<ApplicationInfo> getLaunchableInstalledApplications() {
        List<ApplicationInfo> list = packageManager.getInstalledApplications(0);

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (mContext.getPackageManager().getLaunchIntentForPackage(list.get(i).packageName) != null) {
                    //Launchable app filter using ApplicationInfo fetched in list
                    mApplicationInfo.add(list.get(i));
                } else {
                    Log.i(TAG, "Non Launchable App : - " + list.get(i).packageName);
                }
            }
        } else {
            Log.i(TAG, "getLaunchableInstalledApplications: Apps Info Fetching Failed");
        }
        return mApplicationInfo;
    }

    public static List<ApplicationInfo> getRestrictedAppsInfoFromSystem() {
        List<ApplicationInfo> list = packageManager.getInstalledApplications(0);
        ArrayList<String> restrictedApps = RestrictedAppsRepo.getRestrictedAppList();
        if (list != null) {
            for (ApplicationInfo ainfos : list) {
                if (restrictedApps.contains(ainfos.packageName)) {
                    mApplicationInfo.add(ainfos);
                }
            }
        }
        return mApplicationInfo;
    }

    public static List<String> getInstalledRestrictedAppsFromSystem() {
        List<ApplicationInfo> list = packageManager.getInstalledApplications(0);
        ArrayList<String> restrictedApps = RestrictedAppsRepo.getRestrictedAppList();
        if (list != null) {
            for (ApplicationInfo ainfos : list) {
                if (restrictedApps.contains(ainfos.packageName)) {
                    mInstalledRestrictedApps.add(ainfos.packageName);
                }
            }
        }
        return mInstalledRestrictedApps;
    }

    public PackageManager getPackageManager() {
        if (packageManager == null) {
            packageManager = mContext.getPackageManager();
        }
        return packageManager;
    }
}
