package com.example.workouttracker.modules.exercises

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query

/*
    This api uses api-ninjas.com is specifically using https://api-ninjas.com/api/exercises

    These provide a list of exercises matching the query params, and provide a simple set of instructions for performing the exercise
 */

interface ExerciseApi {
    @GET("exercises")
    suspend fun get(
        @Query("muscle") muscle: String? = null,
        @Query("difficulty") difficultyLevel: String? = null,
        @Query("type") type: String? = null,
    ): List<ExerciseResponseDto>
}

@Parcelize
data class ExerciseResponseDto(
    @Json(name = "name")
    val name: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "muscle")
    val muscleGroup: String,

    @Json(name = "equipment")
    val equipmentType: String,

    @Json(name = "difficulty")
    val difficultyLevel: String,

    @Json(name = "instructions")
    val instructions: String,
): Parcelable

enum class ExerciseType(val displayName: String) {
    CARDIO("Cardio"),
    OLYMPIC_WEIGHTLIFTING("Olympic Weightlifting"),
    PLYOMETRICS("Plyometrics"),
    POWERLIFTING("Powerlifting"),
    STRENGTH("Strength"),
    STRETCHING("Stretching"),
    STRONGMAN("Strongman"),
}

enum class MuscleGroup(val displayName: String) {
    ABDOMINALS("Abdominals"),
    ABDUCTORS("Abductors"),
    ADDUCTORS("Adductors"),
    BICEPS("Biceps"),
    CALVES("Calves"),
    CHEST("Chest"),
    FOREARMS("Forearms"),
    GLUTES("Glutes"),
    HAMSTRINGS("Hamstrings"),
    LATS("Lats"),
    LOWER_BACK("Lower Back"),
    MIDDLE_BACK("Middle Back"),
    NECK("Neck"),
    QUADRICEPS("Quadriceps"),
    TRAPS("Traps"),
    TRICEPS("Triceps"),
}

enum class DifficultyLevel(val displayName: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    EXPERT("Expert"),
}