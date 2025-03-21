package com.nntk.nba.watch.complication.http;

import android.content.ComponentName;
import android.content.Context;

import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.nntk.nba.watch.complication.R;
import com.nntk.nba.watch.complication.complication.NbaScoreComplicationService;
import com.nntk.nba.watch.complication.constant.SettingConst;
import com.nntk.nba.watch.complication.entity.GameInfo;
import com.nntk.nba.watch.complication.entity.TeamEntity;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUtil {

    private static List<TeamEntity> teamEntityListCache = new ArrayList<>();

    private static List<TeamEntity> teamEntityList() {

        if (teamEntityListCache.isEmpty()) {
            String json = ResourceUtils.readRaw2String(R.raw.logo);
            JSONArray objects = JSON.parseArray(json);
            for (int i = 0; i < objects.size(); i++) {
                teamEntityListCache.add(TeamEntity.builder()
                        .simpleFrameSize(objects.getJSONObject(i).getInteger("simpleFrameSize"))
                        .teamName(objects.getJSONObject(i).getString("teamName"))
                        .teamNameZh(objects.getJSONObject(i).getString("teamNameZh"))
                        .bgColor(objects.getJSONObject(i).getString("bgColor"))
                        .scoreBoardColor(objects.getJSONObject(i).getString("scoreBoardColor"))
                        .build());
            }
        }
        return teamEntityListCache;

    }

    public static void getResult(Context context) {

        ThreadUtils.getIoPool().execute(new Runnable() {
            @Override
            public void run() {
                // https://nba-prod-us-east-1-mediaops-stats.s3.amazonaws.com/NBA/liveData/scoreboard/todaysScoreboard_00.json
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .get()
                        .url("https://nba.hupu.com/")
                        .build();
                Call call = client.newCall(request);
                try {
                    //同步发送请求
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        String html = response.body().string();

                        Elements cardTeams = Jsoup.parse(html).select("div.nba-match-container   div.match-card-team");
                        List<GameInfo> gameInfoList = new ArrayList<>();
                        for (Element card : cardTeams) {
                            gameInfoList.add(GameInfo.builder()
                                    .guestTeam(card.select("span.team-name").get(0).text())
                                    .homeTeam(card.select("span.team-name").get(1).text())
                                    .guestRate(card.select("span.team-rate").get(0).text())
                                    .homeRate(card.select("span.team-rate").get(1).text())
                                    .build());
                        }
                        String loveTeam = SPStaticUtils.getString(SettingConst.LOVE_TEAM);
                        TeamEntity teamEntity = teamEntityList().stream().filter(new Predicate<TeamEntity>() {
                            @Override
                            public boolean test(TeamEntity teamEntity) {
                                return teamEntity.getTeamName().contains(loveTeam);
                            }
                        }).findFirst().get();


                        GameInfo gameInfo = gameInfoList.stream().filter(new Predicate<GameInfo>() {
                            @Override
                            public boolean test(GameInfo gameInfo) {
                                return gameInfo.getGuestTeam().equals(teamEntity.getTeamNameZh()) || gameInfo.getHomeTeam().equals(teamEntity.getTeamNameZh());
                            }
                        }).findFirst().orElse(null);

                        if (gameInfo == null) {
                            Logger.i("今日球队【%s】没有比赛", loveTeam);
                            return;
                        }

                        gameInfo.setGuestTeamEntity(teamEntityList().stream().filter(new Predicate<TeamEntity>() {
                            @Override
                            public boolean test(TeamEntity teamEntity) {
                                return teamEntity.getTeamNameZh().equals(gameInfo.getGuestTeam());
                            }
                        }).findFirst().get());

                        gameInfo.setHomeTeamEntity(teamEntityList().stream().filter(new Predicate<TeamEntity>() {
                            @Override
                            public boolean test(TeamEntity teamEntity) {
                                return teamEntity.getTeamNameZh().equals(gameInfo.getHomeTeam());
                            }
                        }).findFirst().get());


                        SPStaticUtils.put(SettingConst.LIVE_GAME_INFO, JSON.toJSONString(gameInfo));
                        Logger.i("loveTeam:%s", gameInfo);


                        // 通知更新表盘
                        ComplicationDataSourceUpdateRequester complicationDataSourceUpdateRequester = ComplicationDataSourceUpdateRequester.create(
                                context, new ComponentName(
                                        context, // 上下文，通常是应用的上下文
                                        NbaScoreComplicationService.class // Complication 数据源服务类
                                )
                        );
                        complicationDataSourceUpdateRequester.requestUpdateAll();

                    } else {
                        Logger.w("请求失败非200:%s", response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.w("请求失败", e);

                }
            }
        });


    }
}
