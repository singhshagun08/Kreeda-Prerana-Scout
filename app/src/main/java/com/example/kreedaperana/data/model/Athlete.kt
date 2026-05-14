package com.example.kreedaperana.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class Athlete(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val school: String = "",
    val primarySport: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val location: String = "",
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
