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
import com.example.kreedaperana.ui.components.MPPieChart
import com.example.kreedaperana.ui.theme.Dimens
import com.example.kreedaperana.ui.theme.NeonBlue
import com.example.kreedaperana.ui.theme.NeonGreen
import com.example.kreedaperana.ui.theme.NeonOrange
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    val allAthletes by viewModel.allAthletes.collectAsState()
    val allTrials by viewModel.allTrials.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PERFORMANCE ANALYTICS", fontWeight = FontWeight.Bold) },
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
                AnalyticsCard(title = "SPORT DISTRIBUTION") {
                    val sportCounts = allAthletes.groupBy { it.primarySport }.mapValues { it.value.size }
                    val entries = sportCounts.map { PieEntry(it.value.toFloat(), it.key) }
                    MPPieChart(
                        modifier = Modifier.height(300.dp).fillMaxWidth(),
                        entries = entries,
                        label = "Sports",
                        colors = listOf(NeonBlue.toArgb(), NeonGreen.toArgb(), NeonOrange.toArgb())
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                AnalyticsCard(title = "TOP PERFORMERS (RECENT)") {
                    val topTrials = allTrials.take(5)
                    val entries = topTrials.mapIndexed { index, trial -> 
                        BarEntry(index.toFloat(), trial.value.toFloat()) 
                    }
                    val labels = topTrials.map { it.trialType }
                    MPBarChart(
                        modifier = Modifier.height(300.dp).fillMaxWidth(),
                        entries = entries,
                        label = "Trial Value",
                        barColor = NeonGreen.toArgb(),
                        xAxisLabels = labels
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
            }
        }
    }
}
