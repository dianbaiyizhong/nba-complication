/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.nntk.nba.watch.complication.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.blankj.utilcode.util.SPStaticUtils
import com.nntk.nba.watch.complication.R
import com.nntk.nba.watch.complication.Receiver
import com.nntk.nba.watch.complication.TimerBroadcastHelper
import com.nntk.nba.watch.complication.TimerBroadcastHelper.ACTION_GAME
import com.nntk.nba.watch.complication.constant.SettingConst
import com.nntk.nba.watch.complication.presentation.theme.NbacomplicationTheme
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        // 设置logger的tag
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("nba-complication")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        super.onCreate(savedInstanceState)

        SPStaticUtils.put(SettingConst.LOVE_TEAM, "warriors")
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Android")
        }
        val intent = Intent(baseContext, Receiver::class.java)
        intent.setAction(ACTION_GAME)
        sendBroadcast(intent)
        TimerBroadcastHelper.setRepeatingAlarm(baseContext)
    }
}

@Composable
fun WearApp(greetingName: String) {
    NbacomplicationTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}