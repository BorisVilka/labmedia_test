package com.test.labmedia.service

import com.test.labmedia.data.entity.QuestionDto
import com.test.labmedia.data.entity.TestDto
import retrofit2.Call
import retrofit2.http.GET

interface API {

    @GET("/test")
    fun getTest(): Call<TestDto>

    @GET("/questions/get")
    fun getQuestions(): Call<List<QuestionDto>>

}