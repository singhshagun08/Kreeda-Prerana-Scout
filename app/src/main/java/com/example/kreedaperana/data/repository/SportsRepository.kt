package com.example.kreedaperana.data.repository

import com.example.kreedaperana.data.local.dao.AthleteDao
import com.example.kreedaperana.data.local.dao.BadgeDao
import com.example.kreedaperana.data.local.dao.PerformanceDao
import com.example.kreedaperana.data.local.dao.TrialDao
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.data.model.Badge
import com.example.kreedaperana.data.model.Performance
import com.example.kreedaperana.data.model.Trial
import kotlinx.coroutines.flow.Flow

import com.example.kreedaperana.data.remote.FirestoreService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SportsRepository(
    private val athleteDao: AthleteDao,
    private val trialDao: TrialDao,
    private val performanceDao: PerformanceDao,
    private val badgeDao: BadgeDao,
    private val firestoreService: FirestoreService
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    // Athlete operations
    val allAthletes: Flow<List<Athlete>> = athleteDao.getAllAthletes()
    
    fun getAthleteById(id: Int): Flow<Athlete?> = athleteDao.getAthleteById(id)

    suspend fun insertAthlete(athlete: Athlete) {
        athleteDao.insertAthlete(athlete)
        scope.launch {
            try { firestoreService.saveAthlete(athlete) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteAthlete(athlete: Athlete) {
        athleteDao.deleteAthlete(athlete)
    }

    // Trial operations
    fun getTrialsForAthlete(athleteId: Int): Flow<List<Trial>> {
        return trialDao.getTrialsForAthlete(athleteId)
    }

    suspend fun insertTrial(trial: Trial) {
        trialDao.insertTrial(trial)
        scope.launch {
            try { firestoreService.saveTrial(trial) } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun getAllTrialsOrdered(): Flow<List<Trial>> {
        return trialDao.getAllTrialsOrdered()
    }

    // Performance operations
    fun getPerformancesForAthlete(athleteId: Int): Flow<List<Performance>> {
        return performanceDao.getPerformancesForAthlete(athleteId)
    }

    suspend fun insertPerformance(performance: Performance) {
        performanceDao.insertPerformance(performance)
    }

    fun getMetricHistory(athleteId: Int, metricName: String): Flow<List<Performance>> {
        return performanceDao.getMetricHistory(athleteId, metricName)
    }

    // Badge operations
    fun getBadgesForAthlete(athleteId: Int): Flow<List<Badge>> {
        return badgeDao.getBadgesForAthlete(athleteId)
    }

    suspend fun insertBadge(badge: Badge) {
        badgeDao.insertBadge(badge)
    }

    val allBadges: Flow<List<Badge>> = badgeDao.getAllBadges()
}
