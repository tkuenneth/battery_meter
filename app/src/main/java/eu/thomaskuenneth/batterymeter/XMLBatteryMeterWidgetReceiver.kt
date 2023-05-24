package eu.thomaskuenneth.batterymeter

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import java.lang.Float.max
import java.text.DateFormat
import java.util.Date

class XMLBatteryMeterWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateXMLBatteryMeterWidget(
            context = context, appWidgetManager = appWidgetManager, appWidgetIds = appWidgetIds
        )
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        enqueueUpdateXMLBatteryMeterWidgetRequest(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelUpdateXMLBatteryWidgetRequest(context)
    }
}

private fun updateXMLBatteryMeterWidget(
    context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
) {
    appWidgetIds.forEach { appWidgetId ->
        val views = RemoteViews(
            context.packageName, R.layout.widget_batterymeter
        ).apply {
            val percent = max(context.getBatteryStatusPercent(), 0.0F)
            val resIds = listOf(
                R.id.percent10,
                R.id.percent20,
                R.id.percent30,
                R.id.percent40,
                R.id.percent50,
                R.id.percent60,
                R.id.percent70,
                R.id.percent80,
                R.id.percent90,
                R.id.percent100
            )
            for (i in 0..9) {
                setViewVisibility(
                    resIds[i], if (percent >= 10 + (i * 10)) View.VISIBLE else View.INVISIBLE
                )
            }
            setTextViewText(R.id.percent, "${percent.toInt()} %")
            with(context.getLastUpdated()) {
                setTextViewText(
                    R.id.last_updated, if (this != 0L)
                        DateFormat.getInstance().format(Date(this))
                    else
                        context.getString(R.string.not_run)
                )
                context.appendTextToFile(
                    prefix = "updateXMLBatteryMeterWidget",
                    now = this
                )
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            setOnClickPendingIntent(R.id.root, pendingIntent)
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

fun Context.updateXMLBatteryMeterWidget() {
    val component = ComponentName(this, XMLBatteryMeterWidgetReceiver::class.java)
    with(AppWidgetManager.getInstance(this)) {
        val appWidgetIds = getAppWidgetIds(component)
        updateXMLBatteryMeterWidget(
            context = this@updateXMLBatteryMeterWidget,
            appWidgetManager = this,
            appWidgetIds = appWidgetIds
        )
    }
}
