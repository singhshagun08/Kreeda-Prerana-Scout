@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.kreedaperana.ui.screens

import android.os.SystemClock
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Athlete
import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TimerScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    
    val athletes by viewModel.allAthletes.collectAsState()
    var selectedAthleteId by remember { mutableIntStateOf(-1) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            val baseTime = SystemClock.elapsedRealtime() - elapsedMillis
            while (isRunning) {
                elapsedMillis = SystemClock.elapsedRealtime() - baseTime
                delay(10)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Precision Timer", onBackClick = onBack)
        },
        bottomBar = {
            AppBottomBar(currentRoute = "timer", onNavigate = onNavigate)
        },
        containerColor = DarkBG
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = String.format(Locale.ROOT, "%02d:%02d.%02d", 
                    (elapsedMillis / 60000), 
                    (elapsedMillis % 60000) / 1000,
                    (elapsedMillis % 1000) / 10
                ),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = NeonGreen,
                    letterSpacing = 2.sp
                )
            )
            Text(
                text = "MIN : SEC . MS",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                CustomButton(
                    text = "START",
                    onClick = { isRunning = true },
                    modifier = Modifier.weight(1f),
                    containerColor = if (!isRunning) NeonBlue else DarkSurface,
                    enabled = !isRunning
                )
                CustomButton(
                    text = "STOP",
                    onClick = { isRunning = false },
                    modifier = Modifier.weight(1f),
                    containerColor = if (isRunning) Color.Red else DarkSurface,
                    enabled = isRunning
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            
            OutlinedButton(
                onClick = {
                    isRunning = false
                    elapsedMillis = 0L
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(Dimens.CornerLarge),
                border = BorderStroke(1.dp, TextSecondary.copy(alpha = 0.3f))
            ) {
                Text("RESET TIMER", color = TextSecondary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.weight(1f))
            
            GradientCard(
                accentColor = NeonBlue
            ) {
                Text("RECORD PERFORMANCE", style = MaterialTheme.typography.labelLarge, color = NeonBlue, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // Athlete Selector
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val selectedAthlete = athletes.find { it.id == selectedAthleteId }
                    OutlinedTextField(
                        value = selectedAthlete?.name ?: "SELECT ATHLETE",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ATHLETE", color = TextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(Dimens.CornerMedium),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(DarkSurface)
                    ) {
                        athletes.forEach { athlete ->
                            DropdownMenuItem(
                                text = { Text(athlete.name, color = Color.White) },
                                onClick = {
                                    selectedAthleteId = athlete.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                CustomButton(
                    text = "SAVE RESULT",
                    onClick = {
                        if (selectedAthleteId != -1 && elapsedMillis > 0) {
                            viewModel.addTrial(selectedAthleteId, "Sprint", elapsedMillis / 1000.0)
                            isRunning = false
                            elapsedMillis = 0L
                            selectedAthleteId = -1
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonGreen,
                    enabled = !isRunning && elapsedMillis > 0 && selectedAthleteId != -1
                )
            }
            Spacer(modifier = Modifier.height(80.dp)) // Extra space for bottom bar
        }
    }
}
