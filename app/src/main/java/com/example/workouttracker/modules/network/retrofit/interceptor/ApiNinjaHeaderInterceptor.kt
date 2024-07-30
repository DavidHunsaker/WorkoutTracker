package com.example.workouttracker.modules.network.retrofit.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ApiNinjaHeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("Blah", "Adding headers...hopefully")

        return chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("X-Api-Key", "wiv9ydSUSqf0LeT1QJuJjA==Yr0FGDNfvrchJGep")
                    .build()
            )
        }
    }
}