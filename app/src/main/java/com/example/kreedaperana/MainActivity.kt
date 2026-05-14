package com.example.kreedaperana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.kreedaperana.data.local.AppDatabase
import com.example.kreedaperana.data.remote.FirestoreService
import com.example.kreedaperana.data.repository.AuthRepository
import com.example.kreedaperana.data.repository.SportsRepository
import com.example.kreedaperana.viewmodel.AuthViewModel
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.ui.navigation.NavGraph
import com.example.kreedaperana.ui.theme.KreedaperanaTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KreedaperanaTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val firestoreService = FirestoreService(FirebaseFirestore.getInstance())
    val sportsRepository = SportsRepository(
        database.athleteDao(),
        database.trialDao(),
        database.performanceDao(),
        database.badgeDao(),
        firestoreService
    )
    val authRepository = AuthRepository(FirebaseAuth.getInstance())
    
    val sportsViewModel: SportsViewModel = viewModel(factory = SportsViewModelFactory(sportsRepository))
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))
    val navController = rememberNavController()

    NavGraph(
        navController = navController, 
        sportsViewModel = sportsViewModel,
        authViewModel = authViewModel
    )
}

class SportsViewModelFactory(private val repository: SportsRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SportsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SportsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AuthViewModelFactory(private val repository: AuthRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
