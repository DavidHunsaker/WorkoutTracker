package com.example.workouttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.workouttracker.modules.exercises.ExerciseResponseDto
import com.example.workouttracker.ui.screen.ExerciseDetail
import com.example.workouttracker.ui.screen.Home
import com.example.workouttracker.ui.screen.ListCategory
import com.example.workouttracker.ui.screen.ListScreen
import com.example.workouttracker.ui.screen.SelectionScreen
import com.example.workouttracker.ui.screen.exercise.ExerciseDetailScreen
import com.example.workouttracker.ui.screen.listScreen.ListScreenViewModel
import com.example.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlin.reflect.jvm.jvmName

class MainActivity : ComponentActivity() {

    lateinit var viewModel: ListScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[ListScreenViewModel::class.java]
        viewModel.setUpUiState()

        setContent {
            val exerciseItems by viewModel.exerciseItems.observeAsState(emptyList())
            WorkoutTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TheAppOfAllApps(viewModel, exerciseItems)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        with (viewModel.uiState.value) {

            if (viewModel.exerciseItems.value?.isNotEmpty() == true) {
                viewModel.clearItems()

                // If we are browsing all, we want to go back to the category screen
                if (this.listCategory == ListCategory.NONE) {
                    return super.onBackPressed()
                }

                false
            } else if (this.selectedMuscleGroup != null || this.selectedExerciseType != null || this.selectedDifficultyLevel != null) {
                viewModel.updateSelection()
                false
            } else {
                super.onBackPressed()
            }
        }
    }
}

@Composable
fun TheAppOfAllApps(viewModel: ListScreenViewModel, exerciseItems: List<ExerciseResponseDto>) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Home::class.jvmName) {
        composable(Home::class.jvmName) {
            viewModel.reset()
            SelectionScreen(onNavigateToMuscleGroup = { category ->
                navController.navigate(route = "${ListScreen::class.jvmName}?category=${category.name}")
            })
        }
        composable(
            route = "${ListScreen::class.jvmName}?category={category}",
            arguments = listOf(
                navArgument("category") {
                    defaultValue = ListCategory.NONE.name
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val category: ListCategory = navBackStackEntry.arguments?.getString("category")?.let { ListCategory.valueOf(it) } ?: ListCategory.NONE
            viewModel.setUpUiState(category = category)
            ListScreen(
                viewModel = viewModel,
                exerciseItems = exerciseItems,
                onNavigateToExerciseDetailScreen = { exercise ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("exercise", exercise)
                    navController.navigate(route = ExerciseDetail::class.jvmName)
                }
            )
        }
        composable(route = ExerciseDetail::class.jvmName) {
            // TODO build in handling for when exercise isn't found. Toast? Reroute somewhere?
            val exercise: ExerciseResponseDto = navController.previousBackStackEntry?.savedStateHandle?.get("exercise") ?: return@composable

            ExerciseDetailScreen(
                exerciseDetail = exercise,
                onNavigateToListScreen = {
                    navController.popBackStack()
                },
                onNavigateToLandingScreen = {
                    viewModel.reset()
                    navController.navigate(route = Home::class.jvmName)
                }
            )
        }
    }
}
