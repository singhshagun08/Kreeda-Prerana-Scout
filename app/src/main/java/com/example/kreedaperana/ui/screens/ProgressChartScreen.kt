package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.ui.components.AnalyticsCard
import com.example.kreedaperana.ui.components.MPBarChart
import com.example.kreedaperana.ui.components.MPLineChart
import com.example.kreedaperana.ui.theme.Dimens
import com.example.kreedaperana.ui.theme.NeonBlue
import com.example.kreedaperana.ui.theme.NeonGreen
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressChartScreen(
    athleteId: Int,
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    val athlete by viewModel.getAthleteById(athleteId).collectAsState()
    val trials by viewModel.getTrials(athleteId).collectAsState()

    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${athlete?.name?.uppercase() ?: "ATHLETE"} PROGRESS", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Dimens.PaddingMedium)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                // Sprint Improvement Graph
                AnalyticsCard(title = "SPRINT IMPROVEMENT (SECONDS)") {
                    val sprintTrials = trials.filter { it.trialType.contains("Sprint", ignoreCase = true) }
                        .sortedBy { it.date }
                    
                    if (sprintTrials.isNotEmpty()) {
                        val entries = sprintTrials.mapIndexed { index, trial ->
                            Entry(index.toFloat(), trial.value.toFloat())
                        }
                        val labels = sprintTrials.map { dateFormat.format(Date(it.date)) }
                        
                        MPLineChart(
                            modifier = Modifier.height(250.dp).fillMaxWidth(),
                            entries = entries,
                            label = "Sprint Time",
                            lineColor = NeonBlue.toArgb(),
                            xAxisLabels = labels
                        )
                    } else {
                        Text("No sprint data available", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                // Weekly Progress / Growth Trends
                AnalyticsCard(title = "GROWTH TRENDS (ALL TRIALS)") {
                    if (trials.isNotEmpty()) {
                        val sortedTrials = trials.sortedBy { it.date }
                        val entries = sortedTrials.mapIndexed { index, trial ->
                            BarEntry(index.toFloat(), trial.value.toFloat())
                        }
                        val labels = sortedTrials.map { dateFormat.format(Date(it.date)) }

                        MPBarChart(
                            modifier = Modifier.height(250.dp).fillMaxWidth(),
                            entries = entries,
                            label = "Trial Value",
                            barColor = NeonGreen.toArgb(),
                            xAxisLabels = labels
                        )
                    } else {
                        Text("No trial data available", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
            }
        }
    }
}
