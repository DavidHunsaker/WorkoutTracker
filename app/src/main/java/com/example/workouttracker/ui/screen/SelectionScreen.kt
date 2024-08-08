package com.example.workouttracker.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workouttracker.R
import com.example.workouttracker.ui.button.MainScreenButton
import com.example.workouttracker.ui.screen.listScreen.ListCategory

@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SelectionScreen(
    onNavigateToMuscleGroup: (ListCategory) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.selection_screen_title),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.25f)
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                MainScreenButton(
                    buttonText = stringResource(id = R.string.selection_exercise_type),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onNavigateToMuscleGroup(ListCategory.TYPE)
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MainScreenButton(
                    buttonText = stringResource(id = R.string.muscle_group),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onNavigateToMuscleGroup(ListCategory.MUSCLE)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                MainScreenButton(
                    buttonText = stringResource(id = R.string.difficulty),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onNavigateToMuscleGroup(ListCategory.DIFFICULTY)
                    }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                MainScreenButton(
                    buttonText = stringResource(id = R.string.browse_all),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onNavigateToMuscleGroup(ListCategory.NONE)
                    }
                )
            }
        }
    }
}