package com.test.labmedia.domain.repository

import com.test.labmedia.domain.model.QuestionModel
import com.test.labmedia.domain.model.TestModel

interface Repository {

    fun loadQuestions(): List<QuestionModel>

    fun loadTest(): TestModel
}