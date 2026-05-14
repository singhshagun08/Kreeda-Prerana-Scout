package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.R
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.data.model.Trial
import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.ui.theme.*
import com.example.kreedaperana.viewmodel.SportsViewModel

@Composable
fun HomeScreen(
    viewModel: SportsViewModel,
    onNavigate: (String) -> Unit
) {
    val athletes by viewModel.allAthletes.collectAsState()
    val trials by viewModel.allTrials.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            AppBottomBar(currentRoute = "home", onNavigate = onNavigate)
        },
        containerColor = DarkBG
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = Dimens.PaddingMedium)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                GreetingSection()
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search athletes, trials..."
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                StatsSection(athletes.size, trials.size)
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                SectionHeader("Quick Actions")
                QuickActionsGrid(onNavigate)
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                SectionHeader("Recent Activities")
            }

            if (trials.isEmpty()) {
                item {
                    EmptyStateView(
                        message = "No recent activities found",
                        icon = Icons.Default.Info
                    )
                }
            } else {
                items(trials.take(5)) { trial ->
                    val athlete = athletes.find { it.id == trial.athleteId }
                    RecentActivityItem(trial, athlete)
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
            }
        }
    }
}

@Composable
fun GreetingSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Welcome Coach,",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Text(
                "Ready for training?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(NeonBlue.copy(alpha = 0.05f))
                .border(1.dp, NeonBlue.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun StatsSection(athleteCount: Int, trialCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        StatCard("Total Athletes", athleteCount.toString(), NeonBlue, Modifier.weight(1f))
        StatCard("Total Trials", trialCount.toString(), NeonGreen, Modifier.weight(1f))
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = NeonOrange,
        modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
    )
}

@Composable
fun QuickActionsGrid(onNavigate: (String) -> Unit) {
    val actions = listOf(
        ActionItem("Add Athlete", Icons.Default.Add, NeonBlue, "addAthlete"),
        ActionItem("Start Trial", Icons.Default.AddCircle, NeonGreen, "athleteList"),
        ActionItem("Leaderboard", Icons.Default.Star, NeonOrange, "leaderboard"),
        ActionItem("Analytics", Icons.Default.Info, NeonGreen, "analytics"),
        ActionItem("Reports", Icons.Default.Info, Color(0xFFE040FB), "reports"),
        ActionItem("AI Insights", Icons.Default.Info, NeonBlue, "aiInsights")
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        items(actions) { action ->
            Card(
                modifier = Modifier
                    .size(110.dp)
                    .clickable { onNavigate(action.route) },
                shape = RoundedCornerShape(Dimens.CornerLarge),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(action.color.copy(alpha = 0.15f), Color.Transparent)
                                )
                            )
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(action.icon, contentDescription = null, tint = action.color, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            action.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentActivityItem(trial: Trial, athlete: Athlete?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(athlete?.name ?: "Unknown", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                Text(trial.trialType, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Text(
                "${trial.value}${if (trial.trialType == "Sprint") "s" else "m"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = NeonGreen
            )
        }
    }
}

data class ActionItem(val title: String, val icon: ImageVector, val color: Color, val route: String)
