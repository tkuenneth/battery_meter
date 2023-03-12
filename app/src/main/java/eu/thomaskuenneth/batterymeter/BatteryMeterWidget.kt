package eu.thomaskuenneth.batterymeter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.batteryPercent

class BatteryMeterWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val percent = currentState(key = batteryPercent)
        val context = LocalContext.current
        Box(
            modifier = GlanceModifier.fillMaxSize()
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (percent != null) {
                Text(
                    text = "${percent.toInt()}%"
                )
            } else {
                Text(text = context.getString(R.string.fetching_data))
                SideEffect {
                    context.updateBatteryMeterWidgets()
                }
            }
        }
    }
}

class BatteryMeterWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = BatteryMeterWidget()

    companion object {
        val batteryPercent = floatPreferencesKey("batteryPercent")
    }
}
