package com.mar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class Preference {
    // String Value Variable for Assigning SharedPreference File Name
    private static final String PreferenceFileName = "MARSharedPreference";
    //Static Key Values For Preferences <Key,Value>
    private String AccessUsagePermissionStatus = "AccessUsagePermissionStatus";
    private String IsOpenedFirstTime = "IsOpenedFirstTime";
    private String IsAppLockPinSet = "IsAppLockPinSet";
    private String AppLockPin = "AppLockPin";
    private String lockedApps = "LockedAppPackages";
    //SharedPreference Api for Persistent data consumption
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public Preference(Context context) {
        mSharedPreferences = context.getSharedPreferences(PreferenceFileName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public int getAppLockPin() {
        return mSharedPreferences.getInt(AppLockPin, 1234);
    }

    public void setAppLockPin(int appLockPin) {
        mEditor.putInt(AppLockPin, appLockPin);
    }

    public boolean getIsAppLockPinSet() {
        return mSharedPreferences.getBoolean(IsAppLockPinSet, false);
    }

    public void setIsAppLockPinSet(boolean isAppLockPinSet) {
        mEditor.putBoolean(IsAppLockPinSet, isAppLockPinSet).commit();
    }

    public boolean getAccessUsagePermissionStatus() {
        return mSharedPreferences.getBoolean(AccessUsagePermissionStatus, false);
    }

    public void setAccessUsagePermissionStatus(Boolean accessUsagePermissionStatus) {
        mEditor.putBoolean(AccessUsagePermissionStatus, accessUsagePermissionStatus).commit();
    }

    public boolean getIsOpenedFirstTime() {
        return mSharedPreferences.getBoolean(IsOpenedFirstTime, false);
    }

    public void setIsOpenedFirstTime(Boolean isOpenedFirstTime) {
        mEditor.putBoolean(IsOpenedFirstTime, isOpenedFirstTime).commit();
    }

    public Set<String> getLockedApps() {
        return mSharedPreferences.getStringSet(lockedApps, null);
    }

    public void setLockedApps(Set<String> apps) {
        mEditor.putStringSet(lockedApps, apps).commit();
    }
}