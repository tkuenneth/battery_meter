package eu.thomaskuenneth.batterymeter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context

class XMLBatteryMeterWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateXMLBatteryMeterWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetIds = appWidgetIds
        )
    }
}

private fun updateXMLBatteryMeterWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetIds: IntArray
) {
}

fun updateBatteryMeterWidget(context: Context) {
    val component = ComponentName(context, BatteryMeterWidgetReceiver::class.java)
    with(AppWidgetManager.getInstance(context)) {
        val appWidgetIds = getAppWidgetIds(component)
        updateXMLBatteryMeterWidget(
            context = context,
            appWidgetManager = this,
            appWidgetIds = appWidgetIds
        )
    }
}
