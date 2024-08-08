package com.example.workouttracker

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.workouttracker.modules.exercises.ExerciseResponseDto
import com.example.workouttracker.ui.screen.listScreen.ListCategory
import com.example.workouttracker.ui.screen.listScreen.ListScreen
import com.example.workouttracker.ui.screen.SelectionScreen
import com.example.workouttracker.ui.screen.exercise.ExerciseDetailScreen
import com.example.workouttracker.ui.screen.listScreen.ListScreenViewModel
import com.example.workouttracker.ui.theme.WorkoutTrackerTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.jvm.jvmName

@Serializable
object ListScreen

@Serializable
object Home

@Serializable
object ExerciseDetail

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ListScreenViewModel
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[ListScreenViewModel::class.java]

        setContent {
            val exerciseItems by viewModel.exerciseItems.observeAsState(emptyList())
            navController = rememberNavController()
            WorkoutTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WorkoutApp(navController, viewModel, exerciseItems)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Because I'm in between navigation paradigms, we need to alter the onBackPressed() method
        // I'm still deciding which way I want to go, but leaning towards handling my own to have better control over what happens


        val uiState = viewModel.uiState.value
        Log.d("", "onBackPressed, ui state is $uiState")

        if (uiState.showingDetail) {
            viewModel.updateShowingDetail(false)
            navController.popBackStack()

        // Clear the current list and go back up to the list of exercises
        } else if (viewModel.exerciseItems.value?.isNotEmpty() == true) {
            viewModel.clearItems()

            // If we are browsing all, we want to go back to the category screen instead
            if (uiState.listCategory == ListCategory.NONE) {
                return super.onBackPressed()
            }
        // Otherwise, we can do the default back operation, and pop our list off the stack
        } else
            if (uiState.selectedMuscleGroup != null || uiState.selectedExerciseType != null || uiState.selectedDifficultyLevel != null) {
            viewModel.updateSelection()

        } else
        {
            super.onBackPressed()
        }
    }
}

@Composable
fun WorkoutApp(navController: NavHostController, viewModel: ListScreenViewModel, exerciseItems: List<ExerciseResponseDto>) {

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
            viewModel.updateCategory(category = category)
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

            // This isn't my favorite way of doing this, but I was giving Compose Navigation a whirl.
            // The API I'm using doesn't provide ids for anything, so instead of being able to request a single exercise
            // I end up passing this around from the results we've gathered already.
            // I set this up before I added the view model, so it's very likely this goes away very soon and we just use the view model instead
            val exercise: ExerciseResponseDto = navController.previousBackStackEntry?.savedStateHandle?.get("exercise") ?: return@composable

            viewModel.updateShowingDetail(true)
            ExerciseDetailScreen(
                exerciseDetail = exercise,
                onNavigateToListScreen = {
                    viewModel.updateShowingDetail(false)
                    navController.popBackStack()
                }
            )
        }
    }
}
