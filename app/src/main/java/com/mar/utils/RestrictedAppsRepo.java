package com.mar.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mar.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RestrictedAppsRepo {
    private static final String TAG = "RestrictedAppsRepo";
    private static ArrayList<String> restrictedAppList;
    private static HashMap<String, Integer> colorList;
    private static Context mContext;

    public RestrictedAppsRepo() {
    }

    public static ArrayList<String> getRestrictedAppList() {
        restrictedAppList = new ArrayList<>();
        if (restrictedAppList.isEmpty()) {
            restrictedAppList.add(0, PackageConstants.WHATSAPP);
            restrictedAppList.add(1, PackageConstants.SNAPCHAT);
            restrictedAppList.add(2, PackageConstants.NETFLIX);
            restrictedAppList.add(3, PackageConstants.FACEBOOK);
            restrictedAppList.add(4, PackageConstants.FACEBOOK_LITE);
            restrictedAppList.add(5, PackageConstants.TWITTER);
            restrictedAppList.add(6, PackageConstants.TWITTER_LITE);
            restrictedAppList.add(7, PackageConstants.MUSICALLY);
            restrictedAppList.add(8, PackageConstants.MUSICALLY_LITE);
            restrictedAppList.add(9, PackageConstants.MESSENGER);
            restrictedAppList.add(10, PackageConstants.MESSENGER_LITE);
            restrictedAppList.add(11, PackageConstants.YOUTUBE);
            restrictedAppList.add(12, PackageConstants.GMAIL);
            restrictedAppList.add(13, PackageConstants.INSTAGRAM);
            restrictedAppList.add(14, PackageConstants.HIKE);
            Log.i(TAG, "getRestrictedAppList: Restricted Apps List  Is created and Filled");
            return restrictedAppList;
        } else {
            Log.i(TAG, "getRestrictedAppList: Restricted Apps List  Already Created");
        }
        return restrictedAppList;
    }

    public static HashMap<String, Integer> getAssociatedColorsForRestrictedApps() {
//        TODO ADD COLOR TO EACH PACKAGE FROM CONSTANT CLASS
        colorList = new HashMap<>();
        colorList.put(PackageConstants.WHATSAPP, ContextCompat.getColor(mContext, R.color.whatsapp_color));
        colorList.put(PackageConstants.FACEBOOK, ContextCompat.getColor(mContext, R.color.facebook_color));
        colorList.put(PackageConstants.INSTAGRAM, ContextCompat.getColor(mContext, R.color.instagram_color));
        colorList.put(PackageConstants.TWITTER, ContextCompat.getColor(mContext, R.color.twitter_color));
        colorList.put(PackageConstants.SNAPCHAT, ContextCompat.getColor(mContext, R.color.snapchat_color));
        colorList.put(PackageConstants.NETFLIX, ContextCompat.getColor(mContext, R.color.netflix_color));
        colorList.put(PackageConstants.YOUTUBE, ContextCompat.getColor(mContext, R.color.youtube_color));
        colorList.put(PackageConstants.HIKE, ContextCompat.getColor(mContext, R.color.hike_color));
        return colorList;
    }

    public class PackageConstants {
        public static final String WHATSAPP = "com.whatsapp";
        public static final String HIKE = "com.bsb.hike";
        public static final String SNAPCHAT = "com.snapchat.android";
        public static final String NETFLIX = "com.netflix.mediaclient";
        public static final String YOUTUBE = "com.google.android.youtube";
        public static final String INSTAGRAM = "com.instagram.android";
        public static final String FACEBOOK = "com.facebook.katana";
        public static final String FACEBOOK_LITE = "com.facebook.lite";
        public static final String TWITTER = "com.twitter.android";
        public static final String TWITTER_LITE = "com.twitter.android.lite";
        public static final String MUSICALLY = "com.zhiliaoapp.musically";
        public static final String MUSICALLY_LITE = "com.zhiliaoapp.musically.go";
        public static final String MESSENGER = "com.facebook.orca";
        public static final String MESSENGER_LITE = "com.facebook.mlite";
        public static final String GMAIL = "com.google.android.gm";
    }
}
