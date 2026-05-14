package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.ui.theme.*

import com.example.kreedaperana.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var darkMode by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            AppTopBar(title = "Settings", onBackClick = onBack)
        },
        bottomBar = {
            AppBottomBar(currentRoute = "settings", onNavigate = onNavigate)
        },
        containerColor = DarkBG
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(DarkBG)
                .padding(horizontal = Dimens.PaddingMedium)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                SettingsSection("Appearance") {
                    SettingsToggleItem(
                        title = "Dark Mode",
                        icon = Icons.Default.Settings,
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )
                }

                SettingsSection("Notifications") {
                    SettingsToggleItem(
                        title = "Push Notifications",
                        icon = Icons.Default.Notifications,
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }

                SettingsSection("General") {
                    SettingsClickItem(
                        title = "Language",
                        subtitle = "English",
                        icon = Icons.Default.Menu
                    ) {
                        // Open language selection
                    }
                    SettingsClickItem(
                        title = "About App",
                        subtitle = "Version 1.0.0",
                        icon = Icons.Default.Info
                    ) {
                        // Open about info
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))
                
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("LOGOUT", color = Color.Red, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(vertical = Dimens.PaddingSmall)) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = NeonOrange,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimens.PaddingSmall)
        )
        Surface(
            shape = MaterialTheme.shapes.large,
            color = DarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = Color.White, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonBlue,
                checkedTrackColor = NeonBlue.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun SettingsClickItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(Dimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
    }
}
