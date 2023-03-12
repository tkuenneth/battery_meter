package eu.thomaskuenneth.batterymeter

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import eu.thomaskuenneth.batterymeter.ui.theme.BatteryMeterTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatteryMeterTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

fun Context.updateBatteryMeterWidgets() {
    val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val batteryPercent = batteryStatus?.let { intent ->
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    }
    batteryPercent?.let {
        MainScope().launch {
            GlanceAppWidgetManager(this@updateBatteryMeterWidgets).getGlanceIds(BatteryMeterWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(
                        context = this@updateBatteryMeterWidgets,
                        glanceId = glanceId,
                    ) { preferences ->
                        preferences[BatteryMeterWidgetReceiver.batteryPercent] = batteryPercent
                    }
                }
            BatteryMeterWidget().updateAll(this@updateBatteryMeterWidgets)
        }
    }
}
