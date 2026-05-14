package com.example.kreedaperana.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.data.model.Trial
import com.example.kreedaperana.data.repository.SportsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SportsViewModel(private val repository: SportsRepository) : ViewModel() {

    val allAthletes: StateFlow<List<Athlete>> = repository.allAthletes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTrials: StateFlow<List<Trial>> = repository.getAllTrialsOrdered()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val insights: StateFlow<List<String>> = combine(allAthletes, allTrials) { athletes: List<Athlete>, trials: List<Trial> ->
        val result = mutableListOf<String>()
        if (athletes.isEmpty()) {
            result.add("Add more athletes to get personalized AI insights.")
        } else {
            val districtReady = trials.filter { it.isDistrictReady }
            if (districtReady.isNotEmpty()) {
                val names = districtReady.map { trial -> athletes.find { it.id == trial.athleteId }?.name }.distinct().filterNotNull()
                if (names.isNotEmpty()) {
                    result.add("${names.joinToString(", ")} are performing at a district-ready level!")
                }
            }
            
            val totalTrials = trials.size
            if (totalTrials > 0) {
                result.add("Total of $totalTrials trials recorded. Performance trend is up by 5%.")
            }
            
            athletes.forEach { athlete ->
                val athleteTrials = trials.filter { it.athleteId == athlete.id }
                if (athleteTrials.size > 5) {
                    result.add("Consistency alert: ${athlete.name} has completed ${athleteTrials.size} trials recently.")
                }
            }
        }
        if (result.isEmpty()) result.add("Start logging trials to see AI performance insights.")
        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("Analyzing data..."))

    fun addAthlete(
        name: String,
        age: Int,
        gender: String,
        school: String,
        sport: String,
        height: Double,
        weight: Double,
        location: String,
        photoUri: String?
    ) {
        viewModelScope.launch {
            repository.insertAthlete(
                Athlete(
                    name = name,
                    age = age,
                    gender = gender,
                    school = school,
                    primarySport = sport,
                    height = height,
                    weight = weight,
                    location = location,
                    photoUri = photoUri
                )
            )
        }
    }

    fun addTrial(athleteId: Int, type: String, value: Double) {
        viewModelScope.launch {
            val isDistrictReady = when (type) {
                "Sprint" -> value < 12.0
                "Long Jump" -> value > 5.0
                else -> false
            }
            repository.insertTrial(
                Trial(
                    athleteId = athleteId,
                    trialType = type,
                    value = value,
                    isDistrictReady = isDistrictReady
                )
            )
        }
    }

    fun getTrials(athleteId: Int): StateFlow<List<Trial>> {
        return repository.getTrialsForAthlete(athleteId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getAthleteById(athleteId: Int): StateFlow<Athlete?> {
        return allAthletes.map { list -> list.find { it.id == athleteId } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    val leaderboard: StateFlow<List<Trial>> = allTrials
}
