package com.example.workouttracker.ui.screen.listScreen

import com.example.workouttracker.modules.exercises.DifficultyLevel
import com.example.workouttracker.modules.exercises.ExerciseType
import com.example.workouttracker.modules.exercises.MuscleGroup

data class ListScreenUiState(
    val listCategoryTitle: String? = null,
    val listCategory: ListCategory = ListCategory.NONE,
    val isSearching: Boolean = false,
    val selectedMuscleGroup: MuscleGroup? = null,
    val selectedDifficultyLevel: DifficultyLevel? = null,
    val selectedExerciseType: ExerciseType? = null,
    val showingDetail: Boolean = false,
)
