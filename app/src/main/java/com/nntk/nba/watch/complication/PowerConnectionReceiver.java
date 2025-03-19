package com.nntk.nba.watch.complication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Log.i("亮屏:", "Connected");
            TimerBroadcastHelper.setRepeatingAlarm(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.i("熄屏:", "Disconnected");
            // 熄屏，取消定时器
            TimerBroadcastHelper.cancelAlarm(context);
        }
    }
}