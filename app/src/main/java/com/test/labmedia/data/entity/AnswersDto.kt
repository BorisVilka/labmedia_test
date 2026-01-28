package com.test.labmedia.data.entity

import com.google.gson.annotations.SerializedName

data class AnswersDto(
    @SerializedName("1")
    val answer1: String?,
    @SerializedName("2")
    val answer2: String?,
    @SerializedName("3")
    val answer3: String?,
    @SerializedName("4")
    val answer4: String?,
    @SerializedName("5")
    val answer5: String?,
    @SerializedName("6")
    val answer6: String?
)