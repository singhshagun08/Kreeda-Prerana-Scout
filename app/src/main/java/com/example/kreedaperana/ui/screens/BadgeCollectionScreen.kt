package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.ui.theme.*
import com.example.kreedaperana.data.model.Badge as BadgeEntity

data class BadgeUiModel(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val isUnlocked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeCollectionScreen(
    athleteId: Int,
    onBack: () -> Unit
) {
    // Mock data for UI design - in a real app, this would come from ViewModel based on trial data
    val badges = listOf(
        BadgeUiModel("1", "District Ready", "Met district level benchmarks", Icons.Default.ThumbUp, NeonBlue, true),
        BadgeUiModel("2", "State Ready", "Qualified for state level selection", Icons.Default.Star, NeonGreen, false),
        BadgeUiModel("3", "National Ready", "Elite national standards reached", Icons.Default.Person, NeonOrange, false),
        BadgeUiModel("4", "Speed Star", "Recorded sub-12s in 100m sprint", Icons.Default.Info, NeonBlue, true),
        BadgeUiModel("5", "Jump Master", "Exceeded 6m in long jump", Icons.Default.Face, NeonGreen, true),
        BadgeUiModel("6", "Endurance King", "Top 1% in endurance scoring", Icons.Default.Build, Color(0xFFE040FB), false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ACHIEVEMENTS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
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
                .padding(Dimens.PaddingMedium)
        ) {
            Text(
                "ATHLETE RANK: GOLD PROSPECT",
                style = MaterialTheme.typography.labelLarge,
                color = NeonOrange,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = Dimens.PaddingMedium)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                items(badges) { badge ->
                    BadgeCardView(badge)
                }
            }
        }
    }
}

@Composable
fun BadgeCardView(badge: BadgeUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f),
        shape = RoundedCornerShape(Dimens.CornerLarge),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) DarkSurface else DarkBG
        ),
        border = if (badge.isUnlocked) null else androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (badge.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(badge.color.copy(alpha = 0.3f), Color.Transparent)
                                )
                            )
                    )
                }
                
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = if (badge.isUnlocked) badge.color.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f),
                    border = if (badge.isUnlocked) androidx.compose.foundation.BorderStroke(2.dp, badge.color) else null
                ) {
                    Icon(
                        imageVector = badge.icon,
                        contentDescription = null,
                        tint = if (badge.isUnlocked) badge.color else TextSecondary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = badge.name.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = if (badge.isUnlocked) Color.White else TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = badge.description,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
                lineHeight = 14.sp
            )
            
            if (!badge.isUnlocked) {
                Spacer(modifier = Modifier.height(12.dp))
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TextSecondary.copy(alpha = 0.5f)
                )
            }
        }
    }
}
