package com.example.kreedaperana.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "performances")
data class Performance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val athleteId: Int,
    val metricName: String,
    val score: Double,
    val date: Long = System.currentTimeMillis()
)
