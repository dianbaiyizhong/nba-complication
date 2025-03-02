package com.nntk.nba.watch.complication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester;

import com.blankj.utilcode.util.SPStaticUtils;
import com.nntk.nba.watch.complication.complication.MainComplicationService;
import com.nntk.nba.watch.complication.http.ApiUtil;

import java.util.Random;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i) {
        if (i.getAction().equals("nntk")) {

            ApiUtil.getResult(context);



        }
    }
}