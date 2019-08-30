package com.mar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mar.services.AppLockService;

import java.util.Objects;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, AppLockService.class));
            Log.i(TAG, "onReceive: Boot Completed Broadcast Received after  Boot Completed");
        } else if (Objects.equals(intent.getAction(), Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            context.startService(new Intent(context, AppLockService.class));
            Log.i(TAG, "onReceive: Boot Completed Broadcast Received after Phone Unlocked");
        }
    }
}
