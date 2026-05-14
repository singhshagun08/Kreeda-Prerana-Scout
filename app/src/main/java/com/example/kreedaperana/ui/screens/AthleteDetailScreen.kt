package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.data.model.Trial
import com.example.kreedaperana.ui.theme.Dimens
import com.example.kreedaperana.ui.theme.NeonBlue
import com.example.kreedaperana.ui.theme.NeonGreen
import com.example.kreedaperana.ui.theme.NeonOrange
import java.text.SimpleDateFormat
import java.util.*

import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.ui.theme.*

@Composable
fun AthleteDetailScreen(
    athleteId: Int,
    viewModel: SportsViewModel,
    onBack: () -> Unit,
    onSeeAllAchievements: (Int) -> Unit,
    onViewProgress: (Int) -> Unit
) {
    val athlete by viewModel.getAthleteById(athleteId).collectAsState()
    val trials by viewModel.getTrials(athleteId).collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = athlete?.name ?: "Athlete Details", onBackClick = onBack)
        },
        containerColor = DarkBG
    ) { padding ->
        athlete?.let { a ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = Dimens.PaddingMedium)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                    ProfileHeader(a)
                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                    BadgesSection(a, onSeeAll = { onSeeAllAchievements(athleteId) })
                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "ANALYTICS HISTORY", 
                            style = MaterialTheme.typography.labelLarge, 
                            color = NeonBlue,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { onViewProgress(athleteId) }) {
                            Text("VIEW CHART", color = NeonGreen, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                }
                
                if (trials.isEmpty()) {
                    item {
                        EmptyStateView(message = "No trial records found", icon = Icons.Default.Info)
                    }
                } else {
                    items(trials) { trial ->
                        TrialHistoryItem(trial)
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                    }
                }
            }
        } ?: LoadingAnimation()
    }
}

@Composable
fun ProfileHeader(athlete: Athlete) {
    GradientCard(
        accentColor = NeonBlue
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(Dimens.CornerMedium),
                color = NeonBlue.copy(alpha = 0.1f)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(16.dp).fillMaxSize(), tint = NeonBlue)
            }
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column {
                Text(athlete.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("${athlete.age} YRS | ${athlete.gender}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Text(athlete.school.uppercase(), style = MaterialTheme.typography.labelMedium, color = NeonOrange)
                Text("SPORT: ${athlete.primarySport}", style = MaterialTheme.typography.titleMedium, color = NeonGreen, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun BadgesSection(athlete: Athlete, onSeeAll: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "ACHIEVEMENTS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onSeeAll) {
                Text("SEE ALL", color = NeonBlue, style = MaterialTheme.typography.labelMedium)
            }
        }
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        Row(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
            BadgeChip("DISTRICT READY", NeonBlue)
            BadgeChip("SPEED STAR", NeonGreen)
        }
    }
}

@Composable
fun BadgeChip(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(Dimens.CornerSmall)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TrialHistoryItem(trial: Trial) {
    val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(trial.date))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Star, contentDescription = null, tint = if (trial.isDistrictReady) NeonOrange else NeonBlue)
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(trial.trialType, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(date, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            Text(
                String.format(Locale.ROOT, "%.2f", trial.value),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = NeonGreen
            )
        }
    }
}
