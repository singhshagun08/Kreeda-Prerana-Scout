package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.ui.theme.*

@Composable
fun AthleteListScreen(
    viewModel: SportsViewModel,
    onAthleteClick: (Int) -> Unit,
    onAddAthleteClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val athletes by viewModel.allAthletes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredAthletes = athletes.filter { 
        it.name.contains(searchQuery, ignoreCase = true) || 
        it.primarySport.contains(searchQuery, ignoreCase = true) 
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(currentRoute = "athleteList", onNavigate = onNavigate)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAthleteClick,
                containerColor = NeonBlue,
                contentColor = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Athlete")
            }
        },
        topBar = {
            AppTopBar(
                title = "Athlete Database",
                actions = {
                    IconButton(onClick = onLeaderboardClick) {
                        Icon(Icons.Default.Star, contentDescription = "Leaderboard", tint = NeonOrange)
                    }
                }
            )
        },
        containerColor = DarkBG
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = Dimens.PaddingMedium)
        ) {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by name or sport..."
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

            if (filteredAthletes.isEmpty()) {
                EmptyStateView(
                    message = if (searchQuery.isEmpty()) "No athletes found. Add some to get started!" else "No athletes match your search.",
                    icon = Icons.Default.Person
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredAthletes) { athlete ->
                        AthleteCard(
                            name = athlete.name,
                            info = "${athlete.age} yrs | ${athlete.gender} | ${athlete.school}",
                            sport = athlete.primarySport,
                            onClick = { onAthleteClick(athlete.id) }
                        )
                    }
                }
            }
        }
    }
}
