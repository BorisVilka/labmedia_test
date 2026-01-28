package com.test.labmedia.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Client {

    companion object {
        private var instance: Retrofit? = null

        fun getInstance(): Retrofit {
            if(instance==null) instance = getRetrofitInstance(getClient())
            return instance!!
        }

        private fun getRetrofitInstance(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://example.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private fun getClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val requestTimeout = 10
            return OkHttpClient.Builder()
                .connectTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()
        }
    }


}