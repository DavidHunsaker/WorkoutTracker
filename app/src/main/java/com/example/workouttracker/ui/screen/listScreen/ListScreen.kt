package com.example.workouttracker.ui.screen.listScreen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workouttracker.modules.exercises.DifficultyLevel
import com.example.workouttracker.modules.exercises.ExerciseResponseDto
import com.example.workouttracker.modules.exercises.ExerciseType
import com.example.workouttracker.modules.exercises.MuscleGroup

enum class ListCategory(val displayName: String) {
    MUSCLE("Muscle"),
    TYPE("Type"),
    DIFFICULTY("Difficulty"),
    NONE(""),
}

@Composable
fun ListScreen(
    viewModel: ListScreenViewModel = ListScreenViewModel(),
    exerciseItems: List<ExerciseResponseDto> = emptyList(),
    onNavigateToExerciseDetailScreen: (ExerciseResponseDto) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    if (exerciseItems.isNotEmpty()) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(
                items = exerciseItems,
                key = { exerciseItems.indexOf(it) },
                itemContent = { exercise ->
                    ExerciseRowItem(exercise, onNavigateToExerciseDetailScreen)
                    Divider()
                }
            )
        }
    } else {
        when (uiState.listCategory) {
            ListCategory.MUSCLE -> MuscleList(viewModel, onNavigateToExerciseDetailScreen)
            ListCategory.TYPE -> ExerciseTypeList(viewModel)
            ListCategory.DIFFICULTY -> DifficultyLevelList(viewModel)
            ListCategory.NONE -> viewModel.startListScreenJob()
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL, fontScale = 1.0f
)
@Composable
fun MuscleList(
    viewModel: ListScreenViewModel = ListScreenViewModel(),
    onNavigateToExerciseDetailScreen: (ExerciseResponseDto) -> Unit = {}
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(
            items = MuscleGroup.entries.toTypedArray(),
            key = { it },
            itemContent = { group ->
                RowSelector(
                    viewModel = viewModel,
                    muscleGroup = group,
                    displayText = group.displayName
                )
                Divider()
            }
        )
    }
}


@Composable
fun ExerciseTypeList(
    viewModel: ListScreenViewModel
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(
            items = ExerciseType.entries.toTypedArray(),
            key = { it },
            itemContent = { type ->
                RowSelector(viewModel, displayText = type.displayName)
                Divider()
            }
        )
    }
}

@Composable
fun DifficultyLevelList(
    viewModel: ListScreenViewModel
) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(
            items = DifficultyLevel.entries.toTypedArray(),
            key = { it },
            itemContent = { level ->
                RowSelector(viewModel, displayText = level.displayName)
                Divider()
            }
        )
    }
}

@Composable
fun RowSelector(
    viewModel: ListScreenViewModel,
    displayText: String,
    muscleGroup: MuscleGroup? = null,
    difficultyLevel: DifficultyLevel? = null,
    exerciseType: ExerciseType? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.clickable {
            viewModel.updateSelection(
                selectedMuscleGroup = muscleGroup,
                selectedDifficultyLevel = difficultyLevel,
                selectedExerciseType = exerciseType
            )
            viewModel.startListScreenJob()
        }
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = displayText, modifier = Modifier.padding(all = 16.dp))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ExerciseRowItem(
    exercise: ExerciseResponseDto = ExerciseResponseDto(
        "Totally awesome bicep curl",
        "strength",
        "biceps",
        "barbell",
        "beginner",
        "Stand up with your torso upright while holding a barbell at the wide outer handle. The palm of your hands should be facing forward. The elbows should be close to the torso. This will be your starting position. While holding the upper arms stationary, curl the weights forward while contracting the biceps as you breathe out. Tip: Only the forearms should move. Continue the movement until your biceps are fully contracted and the bar is at shoulder level. Hold the contracted position for a second and squeeze the biceps hard. Slowly begin to bring the bar back to starting position as your breathe in. Repeat for the recommended amount of repetitions.  Variations:  You can also perform this movement using an E-Z bar or E-Z attachment hooked to a low pulley. This variation seems to really provide a good contraction at the top of the movement. You may also use the closer grip for variety purposes."
    ),
    onNavigateToExerciseDetailScreen: (ExerciseResponseDto) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable {
                onNavigateToExerciseDetailScreen(exercise)
            }
            .padding(16.dp)
    ) {
        Column {
            Text(text = exercise.name, fontSize = 24.sp)
            Text(text = "Equipment: ${exercise.equipmentType}")
            Text(text = "Muscle: ${exercise.muscleGroup}")
        }
    }
}
