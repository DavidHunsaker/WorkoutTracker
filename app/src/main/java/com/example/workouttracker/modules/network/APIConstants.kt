package com.example.workouttracker.modules.network

import kotlin.time.Duration.Companion.seconds

object APIConstants {

    const val DEFAULT_TIMEOUT_IN_MILLISECONDS = 60000
    const val DEFAULT_RETRY_COUNT = 2
    const val MAX_BODY_SIZE = 500
    const val DEFAULT_SERVER_ADDRESS = "https://api.api-ninjas.com/v1/"
}