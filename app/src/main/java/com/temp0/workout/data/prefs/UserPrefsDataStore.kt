package com.temp0.workout.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.temp0.core.Units
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "temp0_prefs")

data class UserPrefs(
    val units: Units = Units.KG,
    val restDurationSeconds: Int = 60,
    val notificationsEnabled: Boolean = true,
    val activeRoutineId: String? = null,
    val hasSeeded: Boolean = false,
)

/** DataStore-backed profile settings — the single source of truth for weight units and
 *  rest-timer duration used everywhere else in the app (Builder, Exercise screen), per the
 *  design chat: "the default unit should simply be that decided by the user in his config." */
class UserPrefsDataStore(private val context: Context) {

    private object Keys {
        val units = stringPreferencesKey("units")
        val restDurationSeconds = intPreferencesKey("rest_duration_seconds")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val activeRoutineId = stringPreferencesKey("active_routine_id")
        val hasSeeded = booleanPreferencesKey("has_seeded")
    }

    val prefsFlow: Flow<UserPrefs> = context.dataStore.data.map { prefs ->
        UserPrefs(
            units = prefs[Keys.units]?.let { runCatching { Units.valueOf(it) }.getOrNull() } ?: Units.KG,
            restDurationSeconds = prefs[Keys.restDurationSeconds] ?: 60,
            notificationsEnabled = prefs[Keys.notificationsEnabled] ?: true,
            activeRoutineId = prefs[Keys.activeRoutineId],
            hasSeeded = prefs[Keys.hasSeeded] ?: false,
        )
    }

    suspend fun setUnits(units: Units) {
        context.dataStore.edit { it[Keys.units] = units.name }
    }

    suspend fun setRestDurationSeconds(seconds: Int) {
        context.dataStore.edit { it[Keys.restDurationSeconds] = seconds }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.notificationsEnabled] = enabled }
    }

    suspend fun setActiveRoutineId(routineId: String) {
        context.dataStore.edit { it[Keys.activeRoutineId] = routineId }
    }

    suspend fun markSeeded() {
        context.dataStore.edit { it[Keys.hasSeeded] = true }
    }
}
