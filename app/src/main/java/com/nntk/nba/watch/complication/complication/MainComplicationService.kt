package com.nntk.nba.watch.complication.complication

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.blankj.utilcode.util.SPStaticUtils
import com.blankj.utilcode.util.ThreadUtils
import com.nntk.nba.watch.complication.http.ApiUtil

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {

        if (type != ComplicationType.LONG_TEXT) {
            return null
        }
        return createComplicationData("Mon", "Monday")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {

        return createComplicationData(SPStaticUtils.getString("myKey", "hello"), "Saturday")
    }

    private fun createComplicationData(text: String, contentDescription: String) =

        LongTextComplicationData.Builder(
            text = PlainComplicationText.Builder(
                text = text,
            ).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()
}