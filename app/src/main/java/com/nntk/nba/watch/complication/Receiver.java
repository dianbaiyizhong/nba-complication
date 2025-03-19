package com.nntk.nba.watch.complication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester;

import com.blankj.utilcode.util.SPStaticUtils;
import com.nntk.nba.watch.complication.complication.NbaScoreComplicationService;
import com.nntk.nba.watch.complication.http.ApiUtil;

import java.util.Random;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i) {
        if (i.getAction().equals(TimerBroadcastHelper.ACTION_GAME)) {
            ApiUtil.getResult(context);
            ComplicationDataSourceUpdateRequester request = ComplicationDataSourceUpdateRequester.create(
                    context, new ComponentName(
                            context, // 上下文，通常是应用的上下文
                            NbaScoreComplicationService.class // Complication 数据源服务类
                    )
            );
            request.requestUpdateAll();

        }
    }
}