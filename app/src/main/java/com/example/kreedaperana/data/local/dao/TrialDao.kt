package com.example.kreedaperana.data.local.dao

import androidx.room.*
import com.example.kreedaperana.data.model.Trial
import kotlinx.coroutines.flow.Flow

@Dao
interface TrialDao {
    @Query("SELECT * FROM trials WHERE athleteId = :athleteId ORDER BY date DESC")
    fun getTrialsForAthlete(athleteId: Int): Flow<List<Trial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrial(trial: Trial)

    @Query("SELECT * FROM trials ORDER BY value DESC")
    fun getAllTrialsOrdered(): Flow<List<Trial>>
}
