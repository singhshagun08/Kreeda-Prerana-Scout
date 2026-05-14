package com.example.kreedaperana.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kreedaperana.viewmodel.AuthState
import com.example.kreedaperana.viewmodel.AuthViewModel
import com.example.kreedaperana.ui.theme.*
import com.example.kreedaperana.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Reset Password", onBackClick = onBack)
        },
        containerColor = DarkBG
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Background Glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NeonBlue.copy(alpha = 0.05f), Color.Transparent)
                        )
                    )
            )

            Crossfade(targetState = authState, label = "state_transition") { state ->
                when (state) {
                    is AuthState.Success -> {
                        SuccessView(onBack)
                    }
                    else -> {
                        ResetForm(
                            email = email,
                            onEmailChange = { email = it; authViewModel.clearError() },
                            isLoading = state is AuthState.Loading,
                            error = (state as? AuthState.Error)?.message,
                            onResetClick = {
                                if (email.isNotEmpty()) {
                                    authViewModel.resetPassword(email)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResetForm(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onResetClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Email,
            contentDescription = null,
            tint = NeonBlue,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Enter your registered email to receive a password reset link.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        PremiumTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Email Address",
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            isError = error != null
        )

        AnimatedVisibility(visible = error != null) {
            Text(
                text = error ?: "",
                color = Color.Red,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = if (isLoading) "SENDING..." else "SEND RESET LINK",
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth(),
            containerColor = NeonBlue,
            enabled = !isLoading && email.isNotEmpty()
        )
    }
}

@Composable
fun SuccessView(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = NeonGreen,
            modifier = Modifier.size(80.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "EMAIL SENT!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "A password recovery link has been sent to your email address. Please check your inbox and spam folder.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(Dimens.CornerLarge),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue)
        ) {
            Text("BACK TO LOGIN", color = NeonBlue, fontWeight = FontWeight.Bold)
        }
    }
}
