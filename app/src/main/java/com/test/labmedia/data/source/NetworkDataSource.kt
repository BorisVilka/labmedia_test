package com.test.labmedia.data.source

import com.test.labmedia.data.entity.QuestionDto
import com.test.labmedia.data.entity.TestDto
import com.test.labmedia.service.API
import java.io.IOException
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val api: API
) {
    fun loadTest(): TestDto {
        val response = api.getTest().execute()
        if (!response.isSuccessful || response.body() == null) {
            throw IOException("Network error: ${response.code()}")
        }
        return response.body()!!
    }

    fun loadQuestions(): List<QuestionDto> {
        val response = api.getQuestions().execute()
        if (!response.isSuccessful || response.body() == null) {
            throw IOException("Network error: ${response.code()}")
        }
        return response.body()!!
    }
}