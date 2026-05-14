package com.example.kreedaperana.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.kreedaperana.R
import com.example.kreedaperana.viewmodel.AuthState

import com.example.kreedaperana.viewmodel.AuthViewModel
import com.example.kreedaperana.ui.theme.*
import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.utils.Validator

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    
    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onLoginSuccess()
        } else if (authState is AuthState.Error) {
            snackbarHostState.showSnackbar((authState as AuthState.Error).message)
            authViewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBG
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Decorative background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonBlue.copy(alpha = 0.05f), Color.Transparent),
                            radius = 1000f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo Branding
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(NeonBlue.copy(alpha = 0.05f))
                        .border(1.dp, NeonBlue.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Unspecified
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "KREEDA PERANA",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 4.sp
                )
                
                Text(
                    "Discovering Future Champions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeonGreen,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Inputs
                PremiumTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = if (Validator.isValidEmail(it)) null else "Invalid email format"
                        authViewModel.clearError() 
                    },
                    label = "Email Address",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError = emailError != null
                )
                if (emailError != null) {
                    Text(emailError!!, color = Color.Red, style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.Start).padding(start = 16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                PremiumTextField(
                    value = password,
                    onValueChange = { password = it; authViewModel.clearError() },
                    label = "Password",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onVisibilityToggle = { passwordVisible = !passwordVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Forgot Password?",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonBlue,
                    modifier = Modifier.align(Alignment.End).clickable { onNavigateToForgotPassword() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                CustomButton(
                    text = if (authState is AuthState.Loading) "LOGGING IN..." else "LOGIN",
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty() && emailError == null) {
                            authViewModel.login(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonBlue,
                    enabled = authState !is AuthState.Loading && email.isNotEmpty() && password.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Signup Prompt
                Row {
                    Text("Don't have an account? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Sign Up",
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable { onNavigateToSignUp() }
                    )
                }
            }
        }
    }
}
