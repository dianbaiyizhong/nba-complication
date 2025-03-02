package com.nntk.nba.watch.complication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class TimerBroadcastHelper {

    private static final int ALARM_REQUEST_CODE = 0x1234;

    public static void setRepeatingAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Receiver.class);
        intent.setAction("nntk");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                ALARM_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 设置从现在开始每隔 10 秒触发一次广播。
        long intervalMillis = 10 * 1000; // 10 seconds in milliseconds

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // 使用 setExactAndAllowWhileIdle() 以确保即使在设备处于空闲状态时也能触发。
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalMillis, pendingIntent);
        }

    }
}