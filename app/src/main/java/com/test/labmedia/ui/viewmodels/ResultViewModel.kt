package com.test.labmedia.ui.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.labmedia.R
import com.test.labmedia.domain.model.TestModel
import com.test.labmedia.domain.use_case.LoadTestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ResultViewModel @Inject constructor(
    private val loadTestUseCase: LoadTestUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ResultUIState>(ResultUIState.Loading)
    val state: LiveData<ResultUIState> = _state

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    private lateinit var bundle: Bundle

    init {
        loadData()
    }

    fun putArgs(bundle: Bundle) {
        this.bundle = bundle
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = ResultUIState.Loading
            try {
                val test = withContext(Dispatchers.IO) { loadTestUseCase.execute() }
                _state.value = ResultUIState.Content(mapToUi(test, bundle))
            } catch (e: Exception) {
                _state.value = ResultUIState.Error(R.string.error_loading)
            }
        }
    }

    private fun mapToUi(test: TestModel, args: Bundle): TestResultUI {
        return TestResultUI(
            title = test.title,
            timeStartText = timeFormat.format(LocalDateTime.parse(args.getString("dateTimeStart"))),
            dateStartText = dateFormat.format(LocalDateTime.parse(args.getString("dateTimeStart"))),
            timeEndText = timeFormat.format(LocalDateTime.parse(args.getString("dateTimeEnd"))),
            dateEndText = dateFormat.format(LocalDateTime.parse(args.getString("dateTimeEnd"))),
            maxScore = bundle.getInt("max"),
            minScore = bundle.getInt("min"),
            score = bundle.getInt("sum"),
            success = bundle.getInt("sum")>=bundle.getInt("min")
        )
    }
}
sealed class ResultUIState {
    object Loading : ResultUIState()
    data class Content(val ui: TestResultUI) : ResultUIState()
    data class Error(val messageRes: Int) : ResultUIState()
}

data class TestResultUI(
    val title: String,
    val timeStartText: String,
    val dateStartText: String,
    val timeEndText: String,
    val dateEndText: String,
    val maxScore: Int,
    val minScore: Int,
    val score: Int,
    val success: Boolean
)