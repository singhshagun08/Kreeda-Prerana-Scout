package com.example.kreedaperana.data.local.dao

import androidx.room.*
import com.example.kreedaperana.data.model.Performance
import kotlinx.coroutines.flow.Flow

@Dao
interface PerformanceDao {
    @Query("SELECT * FROM performances WHERE athleteId = :athleteId ORDER BY date DESC")
    fun getPerformancesForAthlete(athleteId: Int): Flow<List<Performance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerformance(performance: Performance)

    @Query("SELECT * FROM performances WHERE athleteId = :athleteId AND metricName = :metricName ORDER BY date DESC")
    fun getMetricHistory(athleteId: Int, metricName: String): Flow<List<Performance>>
}
