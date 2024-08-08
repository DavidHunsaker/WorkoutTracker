package com.example.workouttracker.ui.screen.listScreen

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workouttracker.modules.exercises.DifficultyLevel
import com.example.workouttracker.modules.exercises.ExerciseResponseDto
import com.example.workouttracker.modules.exercises.ExerciseType
import com.example.workouttracker.modules.exercises.MuscleGroup
import com.example.workouttracker.modules.network.retrofit.RetrofitApi.exercises
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ListScreenUiState())
    val uiState: StateFlow<ListScreenUiState> = _uiState.asStateFlow()

    private val _exerciseItems: MutableLiveData<List<ExerciseResponseDto>> = MutableLiveData<List<ExerciseResponseDto>>(emptyList())
    val exerciseItems: LiveData<List<ExerciseResponseDto>>
        get() = _exerciseItems

    // We use this job to keep track of any requests being made. This way we can cancel any work that's being done
    // if the user navigates away. We don't want to update the ui unless the job is still valid and running
    @VisibleForTesting
    var listScreenJob: Job? = null

    fun updateCategory(category: ListCategory = ListCategory.NONE) {
        _uiState.value = uiState.value.copy(listCategory = category, listCategoryTitle = category.displayName)
    }

    // Clear out all UI data and cancel job
    fun reset() {
        _uiState.value = ListScreenUiState()
        _exerciseItems.value = emptyList()
        resetListScreenJob()
    }

    // Next up is being able to search by exercise name
    fun updateSearchState(searching: Boolean = false) {
        _uiState.value = _uiState.value.copy(isSearching = searching)
    }

    fun updateSelection(selectedMuscleGroup: MuscleGroup? = null, selectedDifficultyLevel: DifficultyLevel? = null, selectedExerciseType: ExerciseType? = null) {
        _uiState.value = _uiState.value.copy(
            selectedExerciseType = selectedExerciseType,
            selectedDifficultyLevel = selectedDifficultyLevel,
            selectedMuscleGroup = selectedMuscleGroup
        )
    }

    fun clearItems() {
        _exerciseItems.value = emptyList()
    }

    fun updateShowingDetail(showingDetail: Boolean = false) {
        _uiState.value = _uiState.value.copy(showingDetail = showingDetail)
    }

    private fun resetListScreenJob() {
        listScreenJob?.cancel()
        listScreenJob = null
    }

    fun startListScreenJob() {
        listScreenJob?.cancel()
        listScreenJob = viewModelScope.launch(Dispatchers.IO) {
            getListScreenData()
        }
    }

    /**
     * This will request exercises by the current type selected and then post results out
     */
    @VisibleForTesting
    suspend fun getListScreenData() = withContext(Dispatchers.IO) {
        // Validate user when auth is added
        val results = when (uiState.value.listCategory) {
            ListCategory.MUSCLE -> exercises.get(muscle = uiState.value.selectedMuscleGroup?.name?.lowercase())
            ListCategory.TYPE -> exercises.get(type = uiState.value.selectedExerciseType?.name?.lowercase())
            ListCategory.DIFFICULTY -> exercises.get(difficultyLevel = uiState.value.selectedDifficultyLevel?.name?.lowercase())
            ListCategory.NONE -> exercises.get()
        }

        // We will return without posting results if the job is no longer active
        if (this.isActive.not()) return@withContext

        _exerciseItems.postValue(results)
    }
}