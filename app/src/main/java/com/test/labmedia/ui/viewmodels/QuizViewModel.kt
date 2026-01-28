package com.test.labmedia.ui.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.labmedia.R
import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.domain.use_case.LoadQuestionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.os.SystemClock
import java.time.LocalDateTime
import javax.inject.Inject

class QuizViewModel @Inject constructor(
    private val loadQuestionsUseCase: LoadQuestionsUseCase
) : ViewModel() {
    private val _state = MutableLiveData<QuizUIState>(QuizUIState.Loading)
    val state: LiveData<QuizUIState> = _state

    private val _selection = MutableLiveData<Map<Int, Int>>(emptyMap())
    val selection: LiveData<Map<Int, Int>> = _selection

    private val questions = mutableListOf<QuestionModel>()

    private val _timerText = MutableLiveData<String>()
    val timerText: LiveData<String> = _timerText
    private var timerJob = null as kotlinx.coroutines.Job?
    private var endTimeMs: Long? = null

    init {
        loadData()
        startTimer()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = QuizUIState.Loading
            try {
                val data = withContext(Dispatchers.IO) { loadQuestionsUseCase.execute() }
                questions.addAll(data)
                _state.value = QuizUIState.Content(data)
            } catch (e: Exception) {
                _state.value = QuizUIState.Error(R.string.error_loading)
            }
        }
    }

    fun selectAnswer(questionId: Int, answerIndex: Int) {
        val updated = _selection.value?.toMutableMap() ?: mutableMapOf()
        updated[questionId] = answerIndex
        _selection.value = updated
        if(_selection.value?.keys?.size == questions.size) {
            _state.value = QuizUIState.Solved
        }
    }

    fun getDateTimeEnd(): String {
        return LocalDateTime.now().toString();
    }
    fun generateBundle(old: Bundle): Bundle {
        val max = questions.sumOf { it.score }
        val correctSum = questions.filter {_selection.value[it.id]==it.correct_answer }.sumOf { it.score }
        val min = (max*0.75f).toInt()
        return Bundle().apply {
            putString("dateTimeStart",old.getString("dateTimeStart"))
            putString("dateTimeEnd",getDateTimeEnd())
            putInt("max",max)
            putInt("min",min)
            putInt("sum",correctSum)
        }
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return
        if (endTimeMs == null) {
            endTimeMs = SystemClock.elapsedRealtime() + QUIZ_DURATION_MS
        }
        timerJob = viewModelScope.launch {
            while (isActive) {
                val remainingMs = (endTimeMs ?: 0L) - SystemClock.elapsedRealtime()
                if (remainingMs <= 0L) {
                    _timerText.value = formatTime(0L)
                    break
                }
                _timerText.value = formatTime(remainingMs)
                delay(TIMER_TICK_MS)
            }
        }
    }

    private fun formatTime(remainingMs: Long): String {
        val totalSeconds = remainingMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    private companion object {
        const val QUIZ_DURATION_MS = 60 * 60 * 1000L
        const val TIMER_TICK_MS = 1000L
    }
}

sealed class QuizUIState {
    object Loading : QuizUIState()

    object Solved: QuizUIState()
    class Content(val data: List<QuestionModel>) : QuizUIState()
    data class Error(val messageRes: Int) : QuizUIState()
}