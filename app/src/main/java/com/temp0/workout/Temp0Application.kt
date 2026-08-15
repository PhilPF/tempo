package com.temp0.workout

import android.app.Application
import com.temp0.workout.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class Temp0Application : Application() {

    lateinit var container: AppContainer
        private set

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)

        // First-run seeding: sample routines always (idempotent — a no-op once the routines
        // table is non-empty) and a handful of demo sessions so Progress isn't a bare
        // zero-state on a fresh install.
        applicationScope.launch {
            container.routineRepository.seedIfEmpty()
            container.sessionRepository.seedDemoSessionsIfEmpty()
        }
    }
}
