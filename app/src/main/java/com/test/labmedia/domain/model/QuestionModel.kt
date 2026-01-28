package com.test.labmedia.domain.model


data class QuestionModel(
    val question: String,
    val topic: String,
    val comment: String?,
    val correct_answer: Int,
    val id: Int,
    val score: Int,
    val answers: AnswersModel
)