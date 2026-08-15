package com.temp0.workout.di

import android.content.Context
import com.temp0.workout.data.local.Temp0Database
import com.temp0.workout.data.prefs.UserPrefsDataStore
import com.temp0.workout.data.repository.RoutineRepository
import com.temp0.workout.data.repository.SessionRepository
import com.temp0.workout.data.repository.SettingsRepository

/**
 * Hand-written dependency graph — no Hilt. The app has exactly one ViewModel and three
 * repositories, so annotation-processing overhead (an extra KSP pass, generated component
 * graph) isn't worth it at this size; this class is the seam where Hilt could be introduced
 * later if the app grows.
 */
class AppContainer(context: Context) {
    private val database = Temp0Database.get(context)
    private val userPrefsDataStore = UserPrefsDataStore(context)

    val routineRepository = RoutineRepository(database.routineDao())
    val sessionRepository = SessionRepository(database.sessionDao())
    val settingsRepository = SettingsRepository(userPrefsDataStore)
}
