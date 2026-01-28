package com.test.labmedia.domain.model

import java.time.LocalDateTime

data class TestModel(
    val id: Int,
    val title: String,
    var status: StatusModel,
    val dateTime: LocalDateTime,
    val description: String,
    val dop_info: String,
    val image_url: String,
    val reward: RewardModel
)