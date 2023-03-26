package eu.thomaskuenneth.batterymeter

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.RowScope
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.batteryPercent
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.lastUpdatedMillis
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class BatteryMeterWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val percent = currentState(key = batteryPercent) ?: 100.0F
        val updated = currentState(key = lastUpdatedMillis) ?: System.currentTimeMillis()
        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(color = Color.Transparent),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Box(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = GlanceModifier.defaultWeight()
                            .fillMaxHeight()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = GlanceModifier.fillMaxSize()
                                .background(color = Color.Gray)
                                .padding(all = 6.dp)
                        ) {
                            with(percent.toInt() + 9) {
                                for (i in 1..100 step 10) {
                                    Segment(i <= percent)
                                }
                            }
                        }
                        Text(
                            text = "${percent.toInt()}%"
                        )
                    }
                    Box(
                        modifier = GlanceModifier.width(8.dp).height(16.dp)
                            .background(color = Color.Gray)
                    ) {}
                }
            }
            Text(
                modifier = GlanceModifier.background(color = Color.White),
                text = SimpleDateFormat.getDateTimeInstance().format(Date(updated))
            )
        }
        // workaround: setting clickable on the Column didn't work
        Box(
            modifier = GlanceModifier.fillMaxSize()
                .clickable(actionStartActivity<MainActivity>())
        ) {
        }
        LocalContext.current.appendTextToFile("Content()")
    }
}

@Composable
private fun RowScope.Segment(isFilled: Boolean) {
    Box(
        modifier = GlanceModifier.defaultWeight()
            .fillMaxHeight()
            .background(color = if (isFilled) Color.Green else Color.Transparent)
    ) {
    }
}

class BatteryMeterWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = BatteryMeterWidget()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val now = System.currentTimeMillis()
        val job = MainScope().launch {
            context.appendTextToFile("onUpdate()", now)
            appWidgetIds.forEach { appWidgetId ->
                GlanceAppWidgetManager(context).getGlanceIdBy(appWidgetId = appWidgetId)
                    .updateAppWidgetState(context, now, context.getBatteryStatusPercent())
            }
        }
        job.invokeOnCompletion {
            super.onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    companion object {
        val batteryPercent = floatPreferencesKey("batteryPercent")
        val lastUpdatedMillis = longPreferencesKey("lastUpdatedMillis")
    }
}

private suspend fun GlanceId.updateAppWidgetState(
    context: Context,
    lastUpdatedMillis: Long,
    batteryPercent: Float
) {
    updateAppWidgetState(
        context = context,
        glanceId = this,
    ) { preferences ->
        preferences[BatteryMeterWidgetReceiver.lastUpdatedMillis] =
            lastUpdatedMillis
        preferences[BatteryMeterWidgetReceiver.batteryPercent] = batteryPercent
        BatteryMeterWidget().update(
            context = context,
            glanceId = this
        )
    }
}
