package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.ui.theme.*
import java.util.Locale

import androidx.compose.ui.platform.LocalContext
import com.example.kreedaperana.utils.ShareUtils
import com.example.kreedaperana.ui.components.*

@Composable
fun ReportsScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    val allAthletes by viewModel.allAthletes.collectAsState()
    val allTrials by viewModel.allTrials.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Reports",
                onBackClick = onBack,
                actions = {
                    IconButton(onClick = { ShareUtils.shareSchoolSummary(context, allAthletes.size, allTrials.size) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share All", tint = NeonBlue)
                    }
                }
            )
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
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                ReportSectionHeader("School Performance Overview")
                SchoolSummaryCard(allAthletes.size, allTrials.size)
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                ReportSectionHeader("Individual Athlete Report Cards")
            }

            items(allAthletes) { athlete ->
                val athleteTrials = allTrials.filter { it.athleteId == athlete.id }
                AthleteReportCard(
                    athlete = athlete,
                    trialCount = athleteTrials.size,
                    bestPerformance = athleteTrials.maxByOrNull { it.value }?.value ?: 0.0,
                    onShare = { ShareUtils.shareReport(context, athlete, athleteTrials) }
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                ExportActionsSection(onExportAll = { ShareUtils.shareSchoolSummary(context, allAthletes.size, allTrials.size) })
                Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))
            }
        }
    }
}

@Composable
fun SchoolSummaryCard(athleteCount: Int, trialCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerLarge),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryStat("Total Athletes", athleteCount.toString(), NeonBlue)
                SummaryStat("Total Trials", trialCount.toString(), NeonGreen)
                SummaryStat("Avg Per Athlete", if(athleteCount > 0) (trialCount/athleteCount).toString() else "0", NeonOrange)
            }
        }
    }
}

@Composable
fun SummaryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
fun AthleteReportCard(athlete: Athlete, trialCount: Int, bestPerformance: Double, onShare: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(athlete.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${athlete.primarySport} | $trialCount Trials", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text("Best: ${String.format(Locale.ROOT, "%.2f", bestPerformance)}", style = MaterialTheme.typography.labelMedium, color = NeonGreen)
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = NeonBlue)
            }
        }
    }
}

@Composable
fun ExportActionsSection(onExportAll: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomButton(
            text = "SHARE MONTHLY REPORT",
            onClick = onExportAll,
            modifier = Modifier.fillMaxWidth(),
            containerColor = NeonBlue
        )
        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
        OutlinedButton(
            onClick = onExportAll,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            border = BorderStroke(1.dp, NeonGreen),
            shape = RoundedCornerShape(Dimens.CornerLarge)
        ) {
            Text("EXPORT DATA SUMMARY", color = NeonGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ReportSectionHeader(title: String) {
    Text(
        title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = NeonOrange,
        modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
    )
}
