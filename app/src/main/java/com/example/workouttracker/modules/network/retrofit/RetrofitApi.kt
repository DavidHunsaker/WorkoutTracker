package com.example.workouttracker.modules.network.retrofit

import android.util.Log
import com.example.workouttracker.modules.exercises.ExerciseApi
import com.example.workouttracker.modules.network.APIConstants
import com.example.workouttracker.modules.network.retrofit.interceptor.ApiNinjaHeaderInterceptor
import com.example.workouttracker.modules.network.retrofit.interceptor.RetryInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration

object RetrofitApi {

    // Reference to retrofit singleton, only created when needed
    private val retrofit: Retrofit by lazy { createRetrofit() }

    // Place any specific APIs being used here
    val exercises: ExerciseApi by lazy { retrofit.create(ExerciseApi::class.java) }

    private fun createRetrofit(): Retrofit {
        Log.d("RetrofitApi", "Creating retrofit with address ${APIConstants.DEFAULT_SERVER_ADDRESS}")

        return Retrofit.Builder()
            .baseUrl(APIConstants.DEFAULT_SERVER_ADDRESS)
            .client(createOkHttpClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        Log.d("RetrofitApi", "Creating ok http client")

        return OkHttpClient.Builder()
            .connectTimeout(Duration.ofMillis(APIConstants.DEFAULT_TIMEOUT_IN_MILLISECONDS.toLong()))
            .readTimeout(Duration.ofMillis(APIConstants.DEFAULT_TIMEOUT_IN_MILLISECONDS.toLong()))
            .addInterceptor(ApiNinjaHeaderInterceptor())
            .addInterceptor(RetryInterceptor())
            .build()
    }
}