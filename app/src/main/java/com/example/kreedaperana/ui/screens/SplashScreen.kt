package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.ui.theme.DarkBG
import com.example.kreedaperana.ui.theme.NeonBlue
import com.example.kreedaperana.ui.theme.NeonGreen
import kotlinx.coroutines.delay

import androidx.compose.ui.res.painterResource
import com.example.kreedaperana.R

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBG, Color(0xFF001214))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = NeonBlue.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue.copy(alpha = 0.1f))
                ) {}
                
                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = Color.Unspecified
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "KREEDA PRERANA",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                ),
                color = Color.White
            )
            
            Text(
                text = "PRECISION SPORTS ANALYTICS",
                style = MaterialTheme.typography.labelLarge,
                color = NeonGreen,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            LinearProgressIndicator(
                modifier = Modifier.width(140.dp).height(2.dp),
                color = NeonBlue,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
        }
    }
}
