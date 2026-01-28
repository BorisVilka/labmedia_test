package com.test.labmedia.data.entity

data class TestDto(
    val id: Int,
    val title: String,
    val status: StatusDto,
    val dateTime: String,
    val description: String,
    val dop_info: String,
    val image_url: String,
    val reward: RewardDto
)