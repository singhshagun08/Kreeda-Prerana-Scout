package com.example.kreedaperana.data.local.dao

import androidx.room.*
import com.example.kreedaperana.data.model.Athlete
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athletes ORDER BY name ASC")
    fun getAllAthletes(): Flow<List<Athlete>>

    @Query("SELECT * FROM athletes WHERE id = :id")
    fun getAthleteById(id: Int): Flow<Athlete?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: Athlete)

    @Delete
    suspend fun deleteAthlete(athlete: Athlete)
}
