package com.mar.appmonitor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.mar.appmonitor.detector.Detector;
import com.mar.appmonitor.detector.LollipopDetector;
import com.mar.appmonitor.detector.PreLollipopDetector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppMonitorEngine {
    private static final String TAG = "AppMonitorEngine";
    private static final int DEFAULT_TIMEOUT = 1000;

    private int timeout = DEFAULT_TIMEOUT;
    private ScheduledExecutorService service;
    private Runnable runnable;
    private Listener unregisteredPackageListener;
    private Listener anyPackageListener;
    private Map<String, Listener> listeners;
    private Detector detector;
    private Handler handler;

    public AppMonitorEngine(Context context) {
        listeners = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
        if (AppMonitorUtil.postLollipop())
            detector = new LollipopDetector();
        else
            detector = new PreLollipopDetector();
    }


    public AppMonitorEngine(Context context, Handler customHandler) {
        listeners = new HashMap<>();
        this.handler = customHandler;
        if (AppMonitorUtil.postLollipop())
            detector = new LollipopDetector();
        else
            detector = new PreLollipopDetector();
    }

    public AppMonitorEngine timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public AppMonitorEngine when(String packageName, Listener listener) {
        listeners.put(packageName, listener);
        return this;
    }

    @Deprecated
    public AppMonitorEngine other(Listener listener) {
        return whenOther(listener);
    }

    public AppMonitorEngine whenOther(Listener listener) {
        unregisteredPackageListener = listener;
        return this;
    }

    public AppMonitorEngine whenAny(Listener listener) {
        anyPackageListener = listener;
        return this;
    }

    public void start(Context context) {
        runnable = createRunnable(context.getApplicationContext());
        service = new ScheduledThreadPoolExecutor(1);
        service.schedule(runnable, timeout, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (service != null) {
            service.shutdownNow();
            service = null;
        }
        runnable = null;
    }

    private Runnable createRunnable(final Context context) {
        return () -> {
            getForegroundAppAndNotify(context);
            service.schedule(createRunnable(context), timeout, TimeUnit.MILLISECONDS);
        };
    }

    private void getForegroundAppAndNotify(Context context) {
        final String foregroundApp = getForegroundApp(context);
        boolean foundRegisteredPackageListener = false;
        if (foregroundApp != null) {
            for (String packageName : listeners.keySet()) {
                if (packageName.equalsIgnoreCase(foregroundApp)) {
                    foundRegisteredPackageListener = true;
                    callListener(listeners.get(foregroundApp), foregroundApp);
                }
            }

            if (!foundRegisteredPackageListener && unregisteredPackageListener != null) {
                callListener(unregisteredPackageListener, foregroundApp);
            }
        }
        if (anyPackageListener != null) {
            callListener(anyPackageListener, foregroundApp);
        }
    }

    private void callListener(final Listener listener, final String packageName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onForeground(packageName);
            }
        });
    }

    private String getForegroundApp(Context context) {
        return detector.getForegroundApp(context);
    }

    public interface Listener {
        void onForeground(String process);
    }
}