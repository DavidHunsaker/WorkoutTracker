package com.example.workouttracker.modules.network.retrofit.interceptor

import android.util.Log
import com.example.workouttracker.modules.network.APIConstants.DEFAULT_RETRY_MAX
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response

        var numberOfAttempts = 0
        var success = false

        val maxRetries = DEFAULT_RETRY_MAX

        while (true) {
            numberOfAttempts++

            response = try {
                chain.proceed(request)
            } catch (e: Throwable) {
                if (numberOfAttempts < maxRetries) {
                    Log.e(null, "Failed to reach server. Attempting retry.")
                    continue
                } else {
                    Log.e(null, "Failed to reach server, and max retries reached. Failing request")
                    throw e
                }
            }

            if (response.isSuccessful) {
                success = true
                break
            } else {
                Log.e(null, "Response was unsuccessful and returned code ${response.code()}")
            }

            if (response.code() in 500 until 600 && numberOfAttempts < maxRetries) {
                Log.i(null, "Retrying failed request. Current attempt count is $numberOfAttempts")
                // If we got a bad response that has a body, make sure to close it to prevent resource leaks
                response.body()?.close()
            } else {
                break
            }
        }

        if (success.not()) {
            Log.e(null, "Request to ${response.request().url()} failed after reaching max number of retries")
        }

        return response
    }
}
