package com.example.kreedaperana.data.remote

import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.data.model.Trial
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService(private val db: FirebaseFirestore) {

    private val athletesCollection = db.collection("athletes")
    private val trialsCollection = db.collection("trials")

    suspend fun saveAthlete(athlete: Athlete) {
        athletesCollection.document(athlete.id.toString()).set(athlete).await()
    }

    suspend fun saveTrial(trial: Trial) {
        trialsCollection.document(trial.id.toString()).set(trial).await()
    }

    suspend fun getAllAthletes(): List<Athlete> {
        return athletesCollection.get().await().toObjects(Athlete::class.java)
    }

    suspend fun getTrialsForAthlete(athleteId: Int): List<Trial> {
        return trialsCollection.whereEqualTo("athleteId", athleteId).get().await().toObjects(Trial::class.java)
    }
}
