package com.nntk.nba.watch.complication.complication

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.SmallImage
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.SmallImageType
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.alibaba.fastjson2.JSON
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SPStaticUtils
import com.nntk.nba.watch.complication.R
import com.nntk.nba.watch.complication.constant.SettingConst
import com.nntk.nba.watch.complication.entity.GameInfo
import com.nntk.nba.watch.complication.util.NbaUtils
import com.orhanobut.logger.Logger

/**
 * Skeleton for complication data source that returns short text.
 */
class NbaScoreComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SMALL_IMAGE && type != ComplicationType.LONG_TEXT) {
            return null
        }
        return createComplicationData(
            NbaUtils.getDefault()
        )
    }


    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {


        return createComplicationData(
            NbaUtils.getGameInfo()
        )
    }


    private fun createComplicationData(
        gameInfo: GameInfo,
    ): ComplicationData {


        val homeIcon = ResourceUtils.getMipmapIdByName("hupu_" + gameInfo.homeTeamEntity.teamName)
        val guestIcon = ResourceUtils.getMipmapIdByName("hupu_" + gameInfo.guestTeamEntity.teamName )


        return LongTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                text = gameInfo.guestRate,
            ).build(),
            contentDescription = PlainComplicationText.Builder("contentDescription").build()
        ).setSmallImage(
            SmallImage.Builder(
                image = Icon.createWithResource(this, guestIcon),
                type = SmallImageType.ICON,
            ).build()
        ).setMonochromaticImage(
            MonochromaticImage.Builder(
                image = Icon.createWithResource(this, homeIcon),
            ).build(),
        ).setTitle(
            PlainComplicationText.Builder(
                text = gameInfo.homeRate,
            ).build(),
        ).build()
    }
}