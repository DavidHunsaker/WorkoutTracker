package com.example.workouttracker.ui.screen.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workouttracker.R
import com.example.workouttracker.modules.exercises.ExerciseResponseDto
import com.example.workouttracker.ui.screen.listScreen.ListCategory
import com.example.workouttracker.ui.theme.WorkoutTrackerTheme

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ExerciseDetailScreen(
    exerciseDetail: ExerciseResponseDto = ExerciseResponseDto(
        "Wide-grip barbell curl",
        "strength",
        "biceps",
        "barbell",
        "beginner",
        "Stand up with your torso upright while holding a barbell at the wide outer handle. The palm of your hands should be facing forward. The elbows should be close to the torso. This will be your starting position. While holding the upper arms stationary, curl the weights forward while contracting the biceps as you breathe out. Tip: Only the forearms should move. Continue the movement until your biceps are fully contracted and the bar is at shoulder level. Hold the contracted position for a second and squeeze the biceps hard. Slowly begin to bring the bar back to starting position as your breathe in. Repeat for the recommended amount of repetitions.  Variations:  You can also perform this movement using an E-Z bar or E-Z attachment hooked to a low pulley. This variation seems to really provide a good contraction at the top of the movement. You may also use the closer grip for variety purposes."
    ),
    category: ListCategory = ListCategory.NONE,
    onNavigateToListScreen: (ListCategory) -> Unit = {}
) {
    WorkoutTrackerTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                IconButton(onClick = { onNavigateToListScreen(category) }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "Go back to list"
                    )
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = exerciseDetail.name,
                    fontSize = 24.sp
                )
            }
            Divider(thickness = 2.dp)
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                text = stringResource(id = R.string.details_exercise_type, exerciseDetail.type)
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                text = stringResource(id = R.string.details_muscle, exerciseDetail.muscleGroup)
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                text = stringResource(id = R.string.details_equipment, exerciseDetail.equipmentType)
            )
            Text(
                modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                text = stringResource(
                    id = R.string.details_difficulty,
                    exerciseDetail.difficultyLevel
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                text = stringResource(id = R.string.details_instructions_title),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
            Divider(thickness = 2.dp)
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = exerciseDetail.instructions.split(". ")
                        .joinToString(separator = "• ", prefix = "• ") { "$it\n" })
            }
        }
    }
}