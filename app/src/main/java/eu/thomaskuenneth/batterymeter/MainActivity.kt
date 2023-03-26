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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.thomaskuenneth.batterymeter.ui.theme.BatteryMeterTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: BatteryMeterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = BatteryMeterViewModelFactory(LogRepository(applicationContext))
        setContent {
            BatteryMeterTheme {
                viewModel = viewModel(factory = factory)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                BatteryMeterScreen(
                    lines = uiState.lines
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized)
            viewModel.update()
    }

    override fun onPause() {
        super.onPause()
        updateXMLBatteryMeterWidget()
    }
}

@Composable
fun BatteryMeterScreen(lines: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (lines.isEmpty())
            Text(
                text = stringResource(id = R.string.no_messages),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        else
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(lines) { _, line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
    }
}

fun Context.getBatteryStatusPercent(): Float {
    val batteryStatus =
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    return batteryStatus?.let { intent ->
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    } ?: -1.0F
}
