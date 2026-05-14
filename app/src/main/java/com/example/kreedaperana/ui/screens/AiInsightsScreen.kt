package com.example.kreedaperana.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.ui.theme.*
import com.example.kreedaperana.ui.components.*

@Composable
fun AiInsightsScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    val dynamicInsights by viewModel.insights.collectAsState()
    val athletes by viewModel.allAthletes.collectAsState()
    val trials by viewModel.allTrials.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "AI Insights", onBackClick = onBack)
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
                AiAssistantHeader(athletes.size, trials.size)
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                Text(
                    "CRITICAL HIGHLIGHTS",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonOrange,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
            }

            items(dynamicInsights) { insightText ->
                AiInsightCard(
                    AiInsight(
                        text = insightText,
                        tag = "Analysis",
                        icon = Icons.Default.Info,
                        accentColor = if (insightText.contains("district-ready")) NeonGreen else NeonBlue
                    )
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                Text(
                    "TRAINING RECOMMENDATIONS",
                    style = MaterialTheme.typography.labelLarge,
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                RecommendationCard(
                    "Encourage athletes to maintain a consistent trial frequency for better data accuracy.",
                    "General Training"
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))
            }
        }
    }
}

@Composable
fun AiAssistantHeader(athleteCount: Int, trialCount: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerLarge),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonBlue.copy(alpha = pulseAlpha), Color.Transparent)
                        )
                    )
                    .border(2.dp, NeonBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column {
                Text(
                    "KREEDA AI ASSISTANT",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "Analyzing $athleteCount athletes and $trialCount trial records...",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun AiInsightCard(insight: AiInsight) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CornerMedium),
        colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, insight.accentColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(insight.icon, contentDescription = null, tint = insight.accentColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
            Column {
                Text(insight.tag.uppercase(), style = MaterialTheme.typography.labelSmall, color = insight.accentColor, fontWeight = FontWeight.Bold)
                Text(insight.text, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}

@Composable
fun RecommendationCard(text: String, tag: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CornerMedium))
            .background(
                Brush.horizontalGradient(
                    listOf(NeonBlue.copy(alpha = 0.1f), Color.Transparent)
                )
            )
            .border(1.dp, NeonBlue.copy(alpha = 0.1f), RoundedCornerShape(Dimens.CornerMedium))
            .padding(Dimens.PaddingMedium)
    ) {
        Column {
            Text(tag.uppercase(), style = MaterialTheme.typography.labelSmall, color = NeonBlue, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}

data class AiInsight(
    val text: String,
    val tag: String,
    val icon: ImageVector,
    val accentColor: Color
)
