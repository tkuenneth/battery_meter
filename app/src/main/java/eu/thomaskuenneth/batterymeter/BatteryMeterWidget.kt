package eu.thomaskuenneth.batterymeter

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.batteryPercent
import eu.thomaskuenneth.batterymeter.BatteryMeterWidgetReceiver.Companion.lastUpdatedMillis
import java.text.SimpleDateFormat
import java.util.*

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
                    CircularProgressIndicator()
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

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Important: this won't stay here, I just temporarily added it because I want to
        // test if updates initiated by the system are affected by battery optimization
        context.updateBatteryMeterWidgets()

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        val batteryPercent = floatPreferencesKey("batteryPercent")
        val lastUpdatedMillis = longPreferencesKey("lastUpdatedMillis")
    }
}
