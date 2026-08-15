package com.temp0.workout.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.temp0.workout.data.local.dao.RoutineDao
import com.temp0.workout.data.local.dao.SessionDao
import com.temp0.workout.data.local.entity.RoutineEntity
import com.temp0.workout.data.local.entity.RoutineExerciseEntity
import com.temp0.workout.data.local.entity.SessionEntity
import com.temp0.workout.data.local.entity.SessionExerciseEntity

@Database(
    entities = [
        RoutineEntity::class,
        RoutineExerciseEntity::class,
        SessionEntity::class,
        SessionExerciseEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class Temp0Database : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile private var instance: Temp0Database? = null

        fun get(context: Context): Temp0Database =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, Temp0Database::class.java, "temp0.db")
                    .build()
                    .also { instance = it }
            }
    }
}
