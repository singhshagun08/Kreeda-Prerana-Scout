package com.example.kreedaperana.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trials")
data class Trial(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val athleteId: Int = 0,
    val trialType: String = "",
    val value: Double = 0.0,
    val date: Long = System.currentTimeMillis(),
    val isDistrictReady: Boolean = false
)
