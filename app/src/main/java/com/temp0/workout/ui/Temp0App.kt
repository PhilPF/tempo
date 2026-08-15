package com.temp0.workout.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.temp0.workout.ui.builder.BuilderScreen
import com.temp0.workout.ui.components.BottomTab
import com.temp0.workout.ui.components.Temp0BottomNav
import com.temp0.workout.ui.components.Temp0TopBar
import com.temp0.workout.ui.exercise.ExerciseScreen
import com.temp0.workout.ui.home.HomeScreen
import com.temp0.workout.ui.profile.ProfileScreen
import com.temp0.workout.ui.progress.ProgressScreen
import com.temp0.workout.ui.routines.RoutinesListScreen
import com.temp0.workout.ui.theme.LocalTemp0Colors
import com.temp0.workout.ui.theme.Temp0Theme

private object Routes {
    const val HOME = "home"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
    const val ROUTINES = "routines"
    const val EXERCISE = "exercise"
    const val BUILDER = "builder"
}

/**
 * The app's root composable and navigation graph. Screen-to-screen navigation is
 * [androidx.navigation.NavController] — a real back stack — so system back (button and
 * predictive-back gesture) works without any hand-rolled `BackHandler`, per the user's
 * explicit direction. The persistent TEMP0 wordmark bar sits above the [NavHost]; the
 * floating bottom nav is layered over tab-screen content by [MainTabScaffold], shown only
 * for the three bottom-nav destinations, exactly like the source design.
 */
@Composable
fun Temp0App(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Temp0Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalTemp0Colors.current.background),
        ) {
            Temp0TopBar()

            NavHost(navController = navController, startDestination = Routes.HOME, modifier = Modifier.weight(1f)) {
                composable(Routes.HOME) {
                    MainTabScaffold(navController, BottomTab.HOME) {
                        HomeScreen(
                            state = uiState.home,
                            onOpenRoutines = { navController.navigate(Routes.ROUTINES) },
                            onExerciseClick = { index ->
                                viewModel.openExercise(index)
                                navController.navigate(Routes.EXERCISE)
                            },
                            onStartNext = {
                                viewModel.startNext()
                                navController.navigate(Routes.EXERCISE)
                            },
                        )
                    }
                }

                composable(Routes.PROGRESS) {
                    MainTabScaffold(navController, BottomTab.PROGRESS) {
                        ProgressScreen(state = uiState.progress)
                    }
                }

                composable(Routes.PROFILE) {
                    MainTabScaffold(navController, BottomTab.PROFILE) {
                        ProfileScreen(
                            state = uiState.profile,
                            onSetUnits = viewModel::setUnits,
                            onSetRestDuration = viewModel::setRestDurationSeconds,
                            onSetNotifications = viewModel::setNotificationsEnabled,
                        )
                    }
                }

                composable(Routes.ROUTINES) {
                    RoutinesListScreen(
                        state = uiState.routines,
                        onBack = { navController.popBackStack() },
                        onSelectRoutine = { id ->
                            viewModel.selectRoutine(id)
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                        onEditRoutine = { id ->
                            viewModel.startEditRoutine(id)
                            navController.navigate(Routes.BUILDER)
                        },
                        onNewRoutine = {
                            viewModel.startBuilderNew()
                            navController.navigate(Routes.BUILDER)
                        },
                    )
                }

                composable(Routes.EXERCISE) {
                    ExerciseScreen(
                        state = uiState.exercise,
                        onBack = { navController.popBackStack() },
                        onFinishSet = viewModel::completeSet,
                        onSkipRest = viewModel::skipRest,
                        onWeightChange = viewModel::setExerciseWeightInput,
                    )
                }

                composable(Routes.BUILDER) {
                    BuilderScreen(
                        state = uiState.builder,
                        onBack = { navController.popBackStack() },
                        onNameChange = viewModel::setBuilderName,
                        onSearchChange = viewModel::setBuilderSearch,
                        onToggleExercise = viewModel::toggleLibraryExercise,
                        onAdjustSets = viewModel::adjustSets,
                        onAdjustReps = viewModel::adjustReps,
                        onWeightChange = viewModel::setBuilderWeight,
                        onReorder = viewModel::reorderBuilderItem,
                        onSave = {
                            viewModel.builderSave()
                            navController.popBackStack()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MainTabScaffold(
    navController: NavHostController,
    tab: BottomTab,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        Temp0BottomNav(
            selected = tab,
            onSelect = { target ->
                if (target != tab) {
                    navController.navigate(target.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
