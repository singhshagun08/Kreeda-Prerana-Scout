package com.example.kreedaperana.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class Badge(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val athleteId: Int,
    val name: String,
    val description: String,
    val dateEarned: Long = System.currentTimeMillis()
)
