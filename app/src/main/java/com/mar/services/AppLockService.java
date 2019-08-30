package com.mar.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mar.R;
import com.mar.appmonitor.AppMonitorEngine;
import com.mar.utils.Preference;

import java.util.HashSet;
import java.util.Set;

public class AppLockService extends Service {
    private static final String TAG = "AppLockService";
    private volatile HandlerThread mBackgroundThread;
    private ServiceHandler mServiceHandler;
    private AppMonitorEngine appMonitorEngine;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params = null;
    private View lock_page_view;
    // TODO: 03-05-2019 work on fetching restricted app named from mainactivity to service class for static list of restricted apps
//    private String restricted_app_name = "com.google.android.gm";
    public Set<String> restricted_apps;
    private boolean isAppLocked;
    private boolean isRestrictedAppFound;
    private Preference pref;


    public AppLockService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isAppLocked = false;
        isRestrictedAppFound = false;
        restricted_apps = new HashSet<>();
        pref = new Preference(AppLockService.this);
        restricted_apps.addAll(pref.getLockedApps());
        if (lock_page_view == null) {
            lock_page_view = LayoutInflater.from(this).inflate(R.layout.lock_view, null);
        }
        if (mBackgroundThread == null)
            mBackgroundThread = new HandlerThread("AppLockService.HandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        mBackgroundThread.start();
        if (mServiceHandler == null)
            mServiceHandler = new ServiceHandler(mBackgroundThread.getLooper());
        if (appMonitorEngine == null)
            appMonitorEngine = new AppMonitorEngine(this, mServiceHandler);
        if (windowManager == null)
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    PixelFormat.TRANSPARENT);
        }

        params.gravity = Gravity.CENTER;
        params.windowAnimations = android.R.style.Animation_Toast;
        lock_page_view.setFocusableInTouchMode(true);
        lock_page_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    lockApp(false);
                    goToHome();
                } else {
                    return false;
                }
                return false;
            }
        });

//        restricted_apps.addAll(RestrictedAppsRepo.getRestrictedAppList());
        /*restricted_apps.add("com.whatsapp");
        restricted_apps.add("com.facebook.katana");
        restricted_apps.add("com.instagram.android");
        restricted_apps.add("com.zhiliaoapp.musically.go");
        restricted_apps.add("com.google.android.gm");
*/
        Log.i(TAG, "onCreate: mBackgroundThread Thread Id : - " + mBackgroundThread.getThreadId());
    }

    private void goToHome() {
        //intent call to launcher home of the system
        Intent homeintent = new Intent(Intent.ACTION_MAIN);
        homeintent.addCategory(Intent.CATEGORY_HOME);
        homeintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeintent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceHandler.sendEmptyMessage(0);
        Log.i(TAG, "onStartCommand: AppLockService Start Command is Triggered");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (appMonitorEngine != null)
            appMonitorEngine.stop();
        if (mBackgroundThread.isAlive())
            mBackgroundThread.quitSafely();
        isAppLocked = false;
        isRestrictedAppFound = false;
        sendBroadcast(new Intent("LockServiceRestart"));
        Log.i(TAG, "onTaskRemoved: Broadcast Sent To LockServiceRestart");
        Log.i(TAG, "onDestroy: AppLock Service Destroyed");
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sendBroadcast(new Intent("LockServiceRestart"));
        Log.i(TAG, "onTaskRemoved: Broadcast Sent To LockServiceRestart");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void lockApp(boolean lockflag) {
        if (lockflag) {
            windowManager.addView(lock_page_view, params);
            isAppLocked = true;
        } else {
            windowManager.removeView(lock_page_view);
            isAppLocked = false;
        }
    }

    private class ServiceHandler extends Handler {

        private ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            //any app
            appMonitorEngine
                    .when(getPackageName(), new AppMonitorEngine.Listener() {
                        @Override
                        public void onForeground(String process) {
                            Log.i(TAG, "onForeground: Our App Monitored");
                        }
                    })
                    .whenOther(new AppMonitorEngine.Listener() {
                        @Override
                        public void onForeground(String process) {
                            isRestrictedAppFound = restricted_apps.contains(process);
                            if (isRestrictedAppFound && !isAppLocked) {
                                lockApp(true);
                            }
                            if (!isRestrictedAppFound && isAppLocked)
                                lockApp(false);
                            Log.i(TAG, "onForeground: On Any App Launches = " + process);
                        }
                    })
                    .timeout(1000)
                    .start(getApplicationContext());
        }
    }
}
