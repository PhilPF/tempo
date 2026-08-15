package com.temp0.workout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.temp0.core.BuilderLogic
import com.temp0.core.CompleteSetResult
import com.temp0.core.SessionLogic
import com.temp0.core.SessionProgress
import com.temp0.core.Units
import com.temp0.workout.data.prefs.UserPrefs
import com.temp0.workout.data.repository.RoutineRepository
import com.temp0.workout.data.repository.SessionRepository
import com.temp0.workout.data.repository.SettingsRepository
import com.temp0.workout.domain.BuilderDraft
import com.temp0.workout.domain.BuilderExerciseConfig
import com.temp0.workout.domain.Routine
import com.temp0.workout.ui.state.AppUiState
import com.temp0.workout.ui.state.AppUiStateMapper
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The app's single shared ViewModel — mirrors the prototype's one `Component` class rather
 * than splitting into per-screen ViewModels, since state like the active routine is
 * genuinely shared across Home/Routines/Exercise at once. Owns domain/session state only;
 * *which screen is showing* is [androidx.navigation.NavController]'s job (see the plan).
 */
class AppViewModel(
    private val routineRepository: RoutineRepository,
    private val sessionRepository: SessionRepository,
    private val settingsRepository: SettingsRepository,
    private val zone: ZoneId = ZoneId.systemDefault(),
) : ViewModel() {

    private val transient = MutableStateFlow(TransientState())

    private val routines = routineRepository.routinesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val prefs = settingsRepository.prefsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserPrefs())
    private val records = sessionRepository.recordsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val uiState: StateFlow<AppUiState> = combine(routines, prefs, records, transient) { r, p, rec, t ->
        AppUiStateMapper.derive(r, p, rec, t, LocalDate.now(zone), zone)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppUiStateMapper.derive(emptyList(), UserPrefs(), emptyList(), TransientState(), LocalDate.now(zone), zone),
    )

    private var restJob: Job? = null

    // ---- Routine selection ----------------------------------------------------------

    fun selectRoutine(routineId: String) {
        transient.update {
            TransientState(builder = it.builder) // fresh session progress; keep any in-flight builder draft untouched
        }
        cancelRest()
        viewModelScope.launch { settingsRepository.setActiveRoutineId(routineId) }
    }

    // ---- Session / exercise flow ------------------------------------------------------

    fun openExercise(index: Int) {
        cancelRest()
        transient.update { it.copy(selectedExerciseIndex = index, currentSet = 1, restActive = false, restRemainingSeconds = 0) }
    }

    fun startNext() {
        val activeRoutine = currentActiveRoutine() ?: return
        val nextIndex = activeRoutine.items.indices.firstOrNull { it !in transient.value.completedExerciseIndices } ?: 0
        openExercise(nextIndex)
    }

    /** Stores the typed weight for whichever exercise is currently selected — the caller
     *  (the Exercise screen) only knows "the weight field changed," not which exercise key
     *  that corresponds to; this resolves it the same way [completeSet] resolves the
     *  current exercise. */
    fun setExerciseWeightInput(text: String) {
        val activeRoutine = currentActiveRoutine() ?: return
        val currentItem = activeRoutine.items.getOrNull(transient.value.selectedExerciseIndex) ?: return
        transient.update { it.copy(exerciseWeightInputs = it.exerciseWeightInputs + (currentItem.exercise.key to text)) }
    }

    fun completeSet() {
        val activeRoutine = currentActiveRoutine() ?: return
        val currentIndex = transient.value.selectedExerciseIndex
        val currentItem = activeRoutine.items.getOrNull(currentIndex) ?: return

        val progress = SessionProgress(
            selectedExerciseIndex = currentIndex,
            currentSet = transient.value.currentSet,
            completedExerciseIndices = transient.value.completedExerciseIndices,
        )
        when (val result = SessionLogic.completeSet(progress, currentItem.sets, activeRoutine.items.size)) {
            is CompleteSetResult.SetAdvanced -> {
                transient.update { it.copy(currentSet = result.progress.currentSet) }
                startRest()
            }
            is CompleteSetResult.ExerciseAdvanced -> {
                cancelRest()
                transient.update {
                    it.copy(
                        selectedExerciseIndex = result.progress.selectedExerciseIndex,
                        currentSet = result.progress.currentSet,
                        completedExerciseIndices = result.progress.completedExerciseIndices,
                    )
                }
            }
            is CompleteSetResult.SessionFinished -> {
                cancelRest()
                val startedAt = transient.value.sessionStartedAt ?: System.currentTimeMillis()
                val completedIndices = result.progress.completedExerciseIndices
                val exerciseCompletions = activeRoutine.items
                    .filterIndexed { index, _ -> index in completedIndices }
                    .map { it.exercise.key to it.sets }
                viewModelScope.launch {
                    sessionRepository.recordCompletedSession(
                        routineId = activeRoutine.id,
                        routineName = activeRoutine.name,
                        startedAt = startedAt,
                        completedAt = System.currentTimeMillis(),
                        exerciseCompletions = exerciseCompletions,
                    )
                }
                transient.update { TransientState(builder = it.builder) }
            }
        }
    }

    private fun startRest() {
        restJob?.cancel()
        val durationSeconds = prefs.value.restDurationSeconds
        transient.update { it.copy(restActive = true, restRemainingSeconds = durationSeconds) }
        restJob = viewModelScope.launch {
            while (transient.value.restRemainingSeconds > 1) {
                delay(1000)
                transient.update { it.copy(restRemainingSeconds = it.restRemainingSeconds - 1) }
            }
            delay(1000)
            transient.update { it.copy(restActive = false, restRemainingSeconds = 0) }
        }
    }

    fun skipRest() = cancelRest()

    private fun cancelRest() {
        restJob?.cancel()
        restJob = null
        transient.update { it.copy(restActive = false, restRemainingSeconds = 0) }
    }

    // ---- Builder ------------------------------------------------------------------

    fun startBuilderNew() {
        transient.update { it.copy(builder = BuilderDraft()) }
    }

    fun startEditRoutine(routineId: String) {
        val routine = routines.value.find { it.id == routineId } ?: return
        transient.update { it.copy(builder = BuilderDraft.fromRoutine(routine)) }
    }

    fun toggleLibraryExercise(key: String) {
        transient.update { state ->
            val draft = state.builder
            val newOrder = BuilderLogic.toggleExercise(draft.order, key)
            val newConfig = if (key in newOrder && key !in draft.config) {
                draft.config + (key to BuilderExerciseConfig())
            } else {
                draft.config
            }
            state.copy(builder = draft.copy(order = newOrder, config = newConfig))
        }
    }

    fun adjustSets(key: String, delta: Int) = adjustConfig(key) { it.copy(sets = BuilderLogic.adjust(it.sets, delta)) }
    fun adjustReps(key: String, delta: Int) = adjustConfig(key) { it.copy(reps = BuilderLogic.adjust(it.reps, delta)) }

    fun setBuilderWeight(key: String, rawText: String) = adjustConfig(key) { it.copy(weight = BuilderLogic.parseWeight(rawText)) }

    private fun adjustConfig(key: String, transform: (BuilderExerciseConfig) -> BuilderExerciseConfig) {
        transient.update { state ->
            val draft = state.builder
            val current = draft.config[key] ?: BuilderExerciseConfig()
            state.copy(builder = draft.copy(config = draft.config + (key to transform(current))))
        }
    }

    fun reorderBuilderItem(from: Int, to: Int) {
        transient.update { state ->
            state.copy(builder = state.builder.copy(order = BuilderLogic.reorder(state.builder.order, from, to)))
        }
    }

    fun setBuilderName(name: String) {
        transient.update { it.copy(builder = it.builder.copy(name = name)) }
    }

    fun setBuilderSearch(search: String) {
        transient.update { it.copy(builder = it.builder.copy(search = search)) }
    }

    /** Fire-and-forget save: the Builder screen's onClick pairs this with an immediate
     *  `navController.popBackStack("routines", false)` — safe because it's a local Room
     *  write that completes in milliseconds and the UI updates reactively regardless. */
    fun builderSave() {
        val draft = transient.value.builder
        if (draft.order.isEmpty()) return
        val wasEditing = draft.isEditing
        viewModelScope.launch {
            val savedId = routineRepository.saveRoutine(draft)
            if (!wasEditing) {
                settingsRepository.setActiveRoutineId(savedId)
            }
        }
        if (!wasEditing) {
            transient.update { TransientState() }
        } else {
            transient.update { it.copy(builder = BuilderDraft()) }
        }
    }

    // ---- Profile / settings --------------------------------------------------------

    fun setUnits(units: Units) = viewModelScope.launch { settingsRepository.setUnits(units) }
    fun setRestDurationSeconds(seconds: Int) = viewModelScope.launch { settingsRepository.setRestDurationSeconds(seconds) }
    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }

    // ---- Helpers --------------------------------------------------------------------

    private fun currentActiveRoutine(): Routine? =
        routines.value.find { it.id == prefs.value.activeRoutineId } ?: routines.value.firstOrNull()

    override fun onCleared() {
        restJob?.cancel()
    }

    class Factory(
        private val routineRepository: RoutineRepository,
        private val sessionRepository: SessionRepository,
        private val settingsRepository: SettingsRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AppViewModel(routineRepository, sessionRepository, settingsRepository) as T
    }
}
