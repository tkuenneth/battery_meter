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

class BatteryMeterWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        context.updateXMLBatteryMeterWidget()
        return Result.success()
    }
}

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
