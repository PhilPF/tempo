package com.temp0.workout.data.repository

import com.temp0.core.Units
import com.temp0.workout.data.prefs.UserPrefs
import com.temp0.workout.data.prefs.UserPrefsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dataStore: UserPrefsDataStore) {

    val prefsFlow: Flow<UserPrefs> = dataStore.prefsFlow

    suspend fun setUnits(units: Units) = dataStore.setUnits(units)
    suspend fun setRestDurationSeconds(seconds: Int) = dataStore.setRestDurationSeconds(seconds)
    suspend fun setNotificationsEnabled(enabled: Boolean) = dataStore.setNotificationsEnabled(enabled)
    suspend fun setActiveRoutineId(routineId: String) = dataStore.setActiveRoutineId(routineId)
    suspend fun markSeeded() = dataStore.markSeeded()
}
