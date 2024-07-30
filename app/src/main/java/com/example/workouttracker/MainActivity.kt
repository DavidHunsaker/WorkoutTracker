package com.example.workouttracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workouttracker.modules.exercises.MuscleGroup
import com.example.workouttracker.modules.network.retrofit.RetrofitApi
import com.example.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LandingScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Blah", "Getting exercises!")
            Log.d("Blah", "Exercises returned: ${RetrofitApi.exercises.get()}")
        }
    }
}

@Composable
fun AnotherStartingScreen() {
    Text("To begin, select a category to search by")
    Column {
        Row {
            Text(text = "Exercise Type")
        }
        Row {
            Text(text = "Muscle Group")
        }
        Row {
            Text(text = "Difficulty")
        }
        Row {
            Text(text = "Browse all")
        }
    }
}

@Composable
fun LandingScreen() {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        items(
            items = MuscleGroup.values(),
            key = { it },
            itemContent = { group ->
                RowSelector(displayText = group.displayName)
                Divider()
            }
        )
    }
}

@Composable
fun RowSelector(displayText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.clickable {
            // Navigate to list of exercises for selected muscle group
            Log.d("blah", "Selected: $displayText")
        }
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(all = 16.dp)
        ) {
            Text(text = displayText)
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WorkoutTrackerTheme {
        Greeting("Android")
    }
}