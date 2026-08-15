package com.temp0.workout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.temp0.workout.ui.AppViewModel
import com.temp0.workout.ui.Temp0App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as Temp0Application).container
        val factory = AppViewModel.Factory(container.routineRepository, container.sessionRepository, container.settingsRepository)

        setContent {
            val viewModel: AppViewModel = viewModel(factory = factory)
            Temp0App(viewModel = viewModel)
        }
    }
}
