package eu.thomaskuenneth.batterymeter

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest.Companion.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

private const val WORK_NAME = "update-battery-meter-widget"

private val PREFS_NAME = BatteryMeterWorker::class.java.simpleName
private const val LAST_UPDATED = "last_updated"

class BatteryMeterWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putLong(LAST_UPDATED, System.currentTimeMillis()).apply()
        context.updateXMLBatteryMeterWidget()
        return Result.success()
    }
}

fun Context.getLastUpdated() =
    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong(LAST_UPDATED, 0L)

fun enqueueUpdateXMLBatteryMeterWidgetRequest(context: Context) {
    val request = PeriodicWorkRequestBuilder<BatteryMeterWorker>(
        MIN_PERIODIC_INTERVAL_MILLIS,
        TimeUnit.MILLISECONDS
    ).build()
    WorkManager
        .getInstance(context)
        .enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
}

fun cancelUpdateXMLBatteryWidgetRequest(context: Context) {
    WorkManager
        .getInstance(context)
        .cancelUniqueWork(WORK_NAME)
}
