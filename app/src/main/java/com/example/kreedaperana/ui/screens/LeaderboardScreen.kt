package com.example.kreedaperana.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Trial
import com.example.kreedaperana.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    val fullLeaderboard by viewModel.leaderboard.collectAsState()
    var selectedEvent by remember { mutableStateOf("Sprint") }
    val events = listOf("Sprint", "Long Jump", "High Jump", "Pushups", "Endurance")

    val filteredLeaderboard = remember(fullLeaderboard, selectedEvent) {
        fullLeaderboard.filter { it.trialType == selectedEvent }
            .sortedWith { t1, t2 ->
                if (selectedEvent == "Sprint") t1.value.compareTo(t2.value)
                else t2.value.compareTo(t1.value)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GLOBAL RANKINGS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBG,
                    titleContentColor = NeonBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DarkBG)
        ) {
            // Event Filter Chips
            LazyRow(
                modifier = Modifier.padding(vertical = Dimens.PaddingMedium),
                contentPadding = PaddingValues(horizontal = Dimens.PaddingMedium),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
            ) {
                items(events) { event ->
                    FilterChip(
                        selected = selectedEvent == event,
                        onClick = { selectedEvent = event },
                        label = { Text(event.uppercase()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonBlue.copy(alpha = 0.2f),
                            selectedLabelColor = NeonBlue,
                            containerColor = DarkSurface,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = Dimens.PaddingMedium)
            ) {
                // Podium Section
                if (filteredLeaderboard.isNotEmpty()) {
                    item {
                        LeaderboardPodium(filteredLeaderboard.take(3))
                        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                    }
                }

                // Header for the list
                item {
                    Text(
                        "TOP PERFORMERS",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
                    )
                }

                // Ranked List
                itemsIndexed(filteredLeaderboard) { index, trial ->
                    LeaderboardListItem(index + 1, trial)
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                }
                
                if (filteredLeaderboard.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No records found for $selectedEvent", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardPodium(top3: List<Trial>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        // Silver (2nd)
        if (top3.size >= 2) {
            PodiumMember(
                trial = top3[1],
                rank = 2,
                podiumHeight = 100.dp,
                color = Color(0xFFC0C0C0),
                modifier = Modifier.weight(1f)
            )
        } else { Spacer(modifier = Modifier.weight(1f)) }

        // Gold (1st)
        if (top3.isNotEmpty()) {
            PodiumMember(
                trial = top3[0],
                rank = 1,
                podiumHeight = 140.dp,
                color = NeonOrange,
                modifier = Modifier.weight(1.2f)
            )
        }

        // Bronze (3rd)
        if (top3.size >= 3) {
            PodiumMember(
                trial = top3[2],
                rank = 3,
                podiumHeight = 70.dp,
                color = Color(0xFFCD7F32),
                modifier = Modifier.weight(1f)
            )
        } else { Spacer(modifier = Modifier.weight(1f)) }
    }
}

@Composable
fun PodiumMember(
    trial: Trial,
    rank: Int,
    podiumHeight: androidx.compose.ui.unit.Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(2.dp, color)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp).fillMaxSize(),
                    tint = color
                )
            }
            Surface(
                modifier = Modifier.size(24.dp).align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp),
                shape = CircleShape,
                color = color
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        rank.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "ID: ${trial.athleteId}",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            String.format(Locale.ROOT, "%.2f", trial.value),
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Black
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(podiumHeight)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(
                    Brush.verticalGradient(listOf(color.copy(alpha = 0.4f), color.copy(alpha = 0.05f)))
                )
        )
    }
}

@Composable
fun LeaderboardListItem(rank: Int, trial: Trial) {
    val rankColor = when (rank) {
        1 -> NeonOrange
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format(Locale.ROOT, "%02d", rank),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = rankColor,
                modifier = Modifier.width(32.dp)
            )
            
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.05f)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.padding(8.dp))
            }
            
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Athlete ID: ${trial.athleteId}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Rating", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    String.format(Locale.ROOT, "%.2f", trial.value),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = NeonGreen
                )
                if (trial.isDistrictReady) {
                    Text("ELITE", style = MaterialTheme.typography.labelSmall, color = NeonOrange, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
