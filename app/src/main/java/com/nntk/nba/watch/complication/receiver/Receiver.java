package com.nntk.nba.watch.complication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nntk.nba.watch.complication.TimerBroadcastHelper;
import com.nntk.nba.watch.complication.http.ApiUtil;
import com.orhanobut.logger.Logger;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i) {
        if (i.getAction().equals(TimerBroadcastHelper.ACTION_GAME)) {
            Logger.i("onReceive time: " + i.getStringExtra("logTime"));
            ApiUtil.getResult(context);
        }
    }
}