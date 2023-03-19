package eu.thomaskuenneth.batterymeter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BatteryMeterUiState(
    val lines: List<String> = emptyList()
)

class BatteryMeterViewModel(
    private val logRepository: LogRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BatteryMeterUiState())
    val uiState: StateFlow<BatteryMeterUiState> = _uiState.asStateFlow()

    init {
        update()
    }

    fun update() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    lines = try {
                        logRepository.readFile()
                    } catch (ex: java.lang.Exception) {
                        emptyList()
                    }
                )
            }
        }
    }
}
