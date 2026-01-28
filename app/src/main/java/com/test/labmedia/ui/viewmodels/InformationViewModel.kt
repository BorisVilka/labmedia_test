package com.test.labmedia.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.labmedia.R
import com.test.labmedia.domain.model.StatusModel
import com.test.labmedia.domain.model.TestModel
import com.test.labmedia.domain.use_case.LoadTestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class InformationViewModel @Inject constructor(
    private val loadTestUseCase: LoadTestUseCase
) : ViewModel() {

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    private val _state = MutableLiveData<InfoUIState>(InfoUIState.Loading)
    val state: LiveData<InfoUIState> = _state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = InfoUIState.Loading
            try {
                val test = withContext(Dispatchers.IO) { loadTestUseCase.execute() }
                _state.value = InfoUIState.Content(mapToUi(test))
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = InfoUIState.Error(R.string.error_loading)
            }
        }
    }

    fun getDateTimeStart(): String {
        return LocalDateTime.now().toString();
    }

    private fun mapToUi(test: TestModel): TestUI {
        val statusTextRes = when (test.status) {
            StatusModel.IN_PROGRESS -> R.string.status_in_progress
            StatusModel.NOT_STARTED -> R.string.status_not_started
            StatusModel.DONE -> R.string.status_done
        }
        val statusIcon = when (test.status) {
            StatusModel.DONE -> R.drawable.outline_check_24
            else -> R.drawable.in_progress
        }
        return TestUI(
            title = test.title,
            description = test.description,
            timeText = timeFormat.format(test.dateTime),
            dateText = dateFormat.format(test.dateTime),
            dopInfo = test.dop_info,
            imageUrl = test.image_url,
            statusTextRes = statusTextRes,
            statusIconRes = statusIcon,
            showButton = test.status != StatusModel.DONE,
            coinsCount = test.reward.coins,
            starsCount = test.reward.stars,
            certificate = test.reward.certificate
        )
    }
}

sealed class InfoUIState {
    object Loading : InfoUIState()
    data class Content(val ui: TestUI) : InfoUIState()
    data class Error(val messageRes: Int) : InfoUIState()
}

data class TestUI(
    val title: String,
    val description: String,
    val timeText: String,
    val dateText: String,
    val dopInfo: String,
    val imageUrl: String,
    val statusTextRes: Int,
    val statusIconRes: Int,
    val showButton: Boolean,
    val coinsCount: Int,
    val starsCount: Int,
    val certificate: Boolean
)