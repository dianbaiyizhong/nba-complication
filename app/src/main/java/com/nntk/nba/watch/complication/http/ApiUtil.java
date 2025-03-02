package com.nntk.nba.watch.complication.http;

import android.content.ComponentName;
import android.content.Context;

import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.nntk.nba.watch.complication.complication.MainComplicationService;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUtil {


    public static String getResult(Context context) {

        ThreadUtils.getIoPool().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .get()
                        .url("https://18.163.229.96/quote-stock-b-api/trade-tick?token=72af6ca8d7bc67247762ba69a9ce2900-c-app&query=%7B%20%20%22trace%22%3A%20%22pariatur%22,%20%20%22data%22%3A%20%7B%20%20%20%20%22symbol_list%22%3A%20%5B%20%20%20%20%20%20%7B%20%20%20%20%20%20%20%20%22code%22%3A%20%22857.HK%22%20%20%20%20%20%20%7D,%20%20%20%20%20%20%7B%20%20%20%20%20%20%20%20%22code%22%3A%20%22UNH.US%22%20%20%20%20%20%20%7D%20%20%20%20%5D%20%20%7D%7D")
                        .build();
                Call call = client.newCall(request);

                try {
                    Response response = call.execute();
                    System.out.println(response.body().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                    SPStaticUtils.put("myKey", new Random().nextInt() + "");
                    ComplicationDataSourceUpdateRequester request2 = ComplicationDataSourceUpdateRequester.create(
                            context, new ComponentName(
                                    context, // 上下文，通常是应用的上下文
                                    MainComplicationService.class // Complication 数据源服务类
                            )
                    );
                    request2.requestUpdateAll();

                }

            }
        });

        return new Random().doubles().toString();

    }
}
