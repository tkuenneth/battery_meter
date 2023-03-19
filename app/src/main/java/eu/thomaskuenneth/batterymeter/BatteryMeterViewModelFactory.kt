package eu.thomaskuenneth.batterymeter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BatteryMeterViewModelFactory(private val repository: LogRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BatteryMeterViewModel::class.java)) {
            return BatteryMeterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
