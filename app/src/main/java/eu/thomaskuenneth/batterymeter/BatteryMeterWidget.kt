package eu.thomaskuenneth.batterymeter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.batteryPercent
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.lastUpdatedMillis
import java.text.SimpleDateFormat
import java.util.Date

class BatteryMeterWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val percent = currentState(key = batteryPercent)
        val updated = currentState(key = lastUpdatedMillis)
        val context = LocalContext.current
        Column(
            modifier = GlanceModifier.fillMaxSize().background(color = Color.White),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Box(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                contentAlignment = Alignment.Center
            ) {
                if (percent != null) {
                    Text(
                        text = "${percent.toInt()}%"
                    )
                } else {
                    Text(
                        text = context.getString(R.string.fetching_data)
                    )
                    SideEffect {
                        context.updateBatteryMeterWidgets()
                    }
                }
            }
            if (updated != null) {
                Text(
                    text = SimpleDateFormat.getDateTimeInstance().format(Date(updated))
                )
            }
        }
    }
}

class BatteryMeterWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = BatteryMeterWidget()

    companion object {
        val batteryPercent = floatPreferencesKey("batteryPercent")
        val lastUpdatedMillis = longPreferencesKey("lastUpdatedMillis")
    }
}
