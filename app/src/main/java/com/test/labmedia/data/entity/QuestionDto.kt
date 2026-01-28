package com.test.labmedia.data.entity

data class QuestionDto(
    val question: String,
    val topic: String,
    val comment: String?,
    val correct_answer: Int,
    val id: Int,
    val score: Int,
    val answers: AnswersDto
)