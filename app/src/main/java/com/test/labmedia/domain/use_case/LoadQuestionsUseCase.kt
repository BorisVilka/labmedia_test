package com.test.labmedia.domain.use_case

import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.domain.repository.Repository

class LoadQuestionsUseCase(
    private val repository: Repository
) {

    suspend fun execute(): List<QuestionModel> {
        return repository.loadQuestions()
    }
}