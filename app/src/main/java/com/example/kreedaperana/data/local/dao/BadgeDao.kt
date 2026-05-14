package com.example.kreedaperana.data.local.dao

import androidx.room.*
import com.example.kreedaperana.data.model.Badge
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges WHERE athleteId = :athleteId ORDER BY dateEarned DESC")
    fun getBadgesForAthlete(athleteId: Int): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: Badge)

    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<Badge>>
}
