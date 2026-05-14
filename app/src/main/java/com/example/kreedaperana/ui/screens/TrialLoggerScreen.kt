package com.example.kreedaperana.ui.screens

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.data.model.Trial
import com.example.kreedaperana.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrialLoggerScreen(
    athleteId: Int,
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var trialType by remember { mutableStateOf("Sprint") }
    var manualValue by remember { mutableStateOf("") }
    
    val trials by viewModel.getTrials(athleteId).collectAsState()
    val personalBest = remember(trials, trialType) {
        val filtered = trials.filter { it.trialType == trialType }
        if (trialType == "Sprint") filtered.minByOrNull { it.value }
        else filtered.maxByOrNull { it.value }
    }

    val trialTypes = listOf("Sprint", "Long Jump", "High Jump", "Pushups", "Endurance")
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
            TopAppBar(
                title = { Text("LIVE PERFORMANCE LOG", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Event Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.CornerLarge),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                    Text("SELECT EVENT", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = trialType,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonBlue,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            trialTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        trialType = type
                                        expanded = false
                                        if (type != "Sprint") {
                                            isRunning = false
                                            elapsedMillis = 0L
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

            // Comparison Stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)) {
                PerformanceMetricCard(
                    label = "Personal Best",
                    value = personalBest?.let { String.format(Locale.ROOT, "%.2f", it.value) } ?: "--",
                    unit = if (trialType == "Sprint") "s" else "m/pts",
                    icon = Icons.Default.Star,
                    accentColor = NeonOrange,
                    modifier = Modifier.weight(1f)
                )
                PerformanceMetricCard(
                    label = "Target",
                    value = if (trialType == "Sprint") "12.00" else "5.50",
                    unit = if (trialType == "Sprint") "s" else "m/pts",
                    icon = Icons.Default.Info,
                    accentColor = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Main Input Area
            if (trialType == "Sprint") {
                TimerDisplay(elapsedMillis, isRunning, personalBest?.value, 
                    onStart = { isRunning = true },
                    onStop = { isRunning = false },
                    onReset = { isRunning = false; elapsedMillis = 0L }
                )
            } else {
                ManualInputArea(trialType, manualValue, personalBest?.value, onValueChange = { manualValue = it })
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val valueToSave = if (trialType == "Sprint") {
                        elapsedMillis / 1000.0
                    } else {
                        manualValue.toDoubleOrNull() ?: 0.0
                    }
                    
                    if (valueToSave > 0) {
                        viewModel.addTrial(athleteId, trialType, valueToSave)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                enabled = (trialType == "Sprint" && !isRunning && elapsedMillis > 0) || 
                          (trialType != "Sprint" && manualValue.isNotBlank()),
                shape = RoundedCornerShape(Dimens.CornerLarge),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
            ) {
                Text("FINALIZE & SAVE DATA", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun PerformanceMetricCard(
    label: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.CornerLarge),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                Text(unit, style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.padding(start = 2.dp, bottom = 4.dp))
            }
        }
    }
}

@Composable
fun TimerDisplay(
    millis: Long,
    isRunning: Boolean,
    pbValue: Double?,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    val currentTime = millis / 1000.0
    val isNewPB = !isRunning && pbValue != null && currentTime < pbValue && currentTime > 0
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = String.format(Locale.ROOT, "%.2f", currentTime),
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Black, color = if (isNewPB) NeonOrange else NeonGreen)
        )
        Text("SECONDS", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
        
        if (isNewPB) {
            Text("NEW PERSONAL BEST!", color = NeonOrange, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)) {
            Button(
                onClick = onStart,
                enabled = !isRunning,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue, contentColor = Color.Black)
            ) { Text("START", fontWeight = FontWeight.Bold) }
            
            Button(
                onClick = onStop,
                enabled = isRunning,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) { Text("STOP", fontWeight = FontWeight.Bold) }
        }
        
        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        
        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("RESET TIMER") }
    }
}

@Composable
fun ManualInputArea(type: String, value: String, pbValue: Double?, onValueChange: (String) -> Unit) {
    val unit = when (type) {
        "Long Jump", "High Jump" -> "METERS"
        "Pushups", "Endurance" -> "REPS / SCORE"
        else -> ""
    }
    
    val currentVal = value.toDoubleOrNull() ?: 0.0
    val isNewPB = pbValue != null && currentVal > pbValue
    
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("ENTER RECORDED $unit") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isNewPB) NeonOrange else NeonBlue,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
            )
        )
        
        if (isNewPB) {
            Text("SURPASSES PERSONAL BEST!", color = NeonOrange, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp, start = 4.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NeonBlue.copy(alpha = 0.05f))
        ) {
            Row(modifier = Modifier.padding(Dimens.PaddingMedium), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = NeonBlue)
                Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
                Text(
                    "Recording $type data will automatically update this athlete's global ranking.",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonBlue
                )
            }
        }
    }
}
