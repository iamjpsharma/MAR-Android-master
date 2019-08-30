package com.mar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mar.services.AppLockService;

public class LockServiceRestarter extends BroadcastReceiver {
    private static final String TAG = "LockServiceRestarter";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context.getApplicationContext(), AppLockService.class));
        Log.i(TAG, "onReceive: AppLockService Restart Service Request Accepted");
    }
}
