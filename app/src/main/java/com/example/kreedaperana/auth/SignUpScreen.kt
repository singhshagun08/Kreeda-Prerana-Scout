package com.example.kreedaperana.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onSignUpSuccess()
        } else if (authState is AuthState.Error) {
            snackbarHostState.showSnackbar((authState as AuthState.Error).message)
            authViewModel.clearError()
        }
    }

    // Simple password strength logic
    val passwordStrength = remember(password) {
        when {
            password.isEmpty() -> 0f
            password.length < 6 -> 0.3f
            password.any { it.isDigit() } && password.any { it.isUpperCase() } -> 1f
            else -> 0.6f
        }
    }
    val strengthColor = when {
        passwordStrength < 0.4f -> Color.Red
        passwordStrength < 0.8f -> NeonOrange
        else -> NeonGreen
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkBG
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.Unspecified
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "CREATE ACCOUNT",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                Text(
                    "Join the future of sports scouting",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(40.dp))

                PremiumTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                PremiumTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                PremiumTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onVisibilityToggle = { passwordVisible = !passwordVisible }
                )

                // Password Strength Indicator
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
                    LinearProgressIndicator(
                        progress = { passwordStrength },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = strengthColor,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                    Text(
                        text = when {
                            passwordStrength == 0f -> ""
                            passwordStrength < 0.4f -> "Weak Password"
                            passwordStrength < 0.8f -> "Medium Strength"
                            else -> "Strong Password"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = strengthColor,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                PremiumTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    icon = Icons.Default.Lock,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    passwordVisible = confirmPasswordVisible,
                    onVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible }
                )

                Spacer(modifier = Modifier.height(32.dp))

                CustomButton(
                    text = if (authState is AuthState.Loading) "SIGNING UP..." else "SIGN UP",
                    onClick = {
                        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            scope.launch { snackbarHostState.showSnackbar("Please fill all fields") }
                            return@CustomButton
                        }
                        if (password != confirmPassword) {
                            scope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
                            return@CustomButton
                        }
                        
                        authViewModel.signUp(email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonBlue,
                    enabled = authState !is AuthState.Loading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Text("Already have an account? ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "Login",
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}
