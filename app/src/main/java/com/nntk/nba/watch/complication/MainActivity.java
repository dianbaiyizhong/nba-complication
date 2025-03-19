package com.nntk.nba.watch.complication;

import static com.nntk.nba.watch.complication.TimerBroadcastHelper.ACTION_GAME;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester;
import androidx.wear.widget.WearableRecyclerView;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.nntk.nba.watch.complication.adapter.NbaLogoAdapter;
import com.nntk.nba.watch.complication.animation.CustomAnimation1;
import com.nntk.nba.watch.complication.animation.CustomAnimation2;
import com.nntk.nba.watch.complication.animation.CustomAnimation3;
import com.nntk.nba.watch.complication.complication.NbaScoreComplicationService;
import com.nntk.nba.watch.complication.constant.SettingConst;
import com.nntk.nba.watch.complication.entity.TeamEntity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ComponentActivity {
    private List<TeamEntity> teamEntityList = new ArrayList<>();
    private WearableRecyclerView recyclerView;
    private NbaLogoAdapter nbaLogoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("nba-complication")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        SPStaticUtils.put(SettingConst.LOVE_TEAM, "warriors");

        String json = ResourceUtils.readRaw2String(R.raw.logo);

        JSONArray objects = JSON.parseArray(json);
        for (int i = 0; i < objects.size(); i++) {
            teamEntityList.add(TeamEntity.builder()
                    .simpleFrameSize(objects.getJSONObject(i).getInteger("simpleFrameSize"))
                    .teamName(objects.getJSONObject(i).getString("teamName"))
                    .teamNameZh(objects.getJSONObject(i).getString("teamNameZh"))
                    .bgColor(objects.getJSONObject(i).getString("bgColor"))
                    .city(objects.getJSONObject(i).getString("city"))
                    .build());
        }
        recyclerView = findViewById(R.id.rv);

        initRecyclerView();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        PowerConnectionReceiver mReceiver = new PowerConnectionReceiver();
        registerReceiver(mReceiver, filter);

    }

    private void initRecyclerView() {


        nbaLogoAdapter = new NbaLogoAdapter(teamEntityList);
        nbaLogoAdapter.setAdapterAnimation(new CustomAnimation1());
        nbaLogoAdapter.setAnimationFirstOnly(false);
        recyclerView.setAdapter(nbaLogoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        nbaLogoAdapter.setNewInstance(teamEntityList);

        nbaLogoAdapter.setOnItemClickListener((adapter, view, position) -> {
            TeamEntity teamEntity = (TeamEntity) adapter.getData().get(position);
            Logger.i("star: %s", teamEntity);
            SPStaticUtils.put(SettingConst.LOVE_TEAM, teamEntity.getTeamName());


            ToastUtils.showShort("已选择：" + teamEntity.getTeamNameZh());

            ComplicationDataSourceUpdateRequester request = ComplicationDataSourceUpdateRequester.create(
                    this, new ComponentName(
                            this, // 上下文，通常是应用的上下文
                            NbaScoreComplicationService.class // Complication 数据源服务类
                    )
            );
            request.requestUpdateAll();
        });

    }
}
