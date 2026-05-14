@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.kreedaperana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kreedaperana.viewmodel.SportsViewModel
import com.example.kreedaperana.ui.components.CustomButton
import com.example.kreedaperana.ui.theme.DarkBG
import com.example.kreedaperana.ui.theme.Dimens
import com.example.kreedaperana.ui.theme.NeonBlue
import kotlinx.coroutines.launch

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kreedaperana.ui.components.*
import com.example.kreedaperana.ui.theme.*

@Composable
fun AddAthleteScreen(
    viewModel: SportsViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var school by remember { mutableStateOf("") }
    var sport by remember { mutableStateOf("Athletics") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    
    val genders = listOf("Male", "Female", "Other")
    val sports = listOf("Athletics", "Kabaddi", "Football", "Volleyball", "Kho Kho")

    var genderExpanded by remember { mutableStateOf(false) }
    var sportExpanded by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photoUri = uri?.toString()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(title = "Add Athlete", onBackClick = onBack)
        },
        containerColor = DarkBG
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Photo Picker UI
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(DarkSurface)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = NeonBlue)
                        Text("PHOTO", style = MaterialTheme.typography.labelSmall, color = NeonBlue)
                    }
                } else {
                    Icon(Icons.Default.Person, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(60.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Form Section
            GradientCard(
                accentColor = NeonBlue
            ) {
                Text("PERSONAL DETAILS", style = MaterialTheme.typography.labelLarge, color = NeonBlue, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonBlue) },
                    shape = RoundedCornerShape(Dimens.CornerMedium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.CornerMedium),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Gender", color = TextSecondary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
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
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false },
                            modifier = Modifier.background(DarkSurface)
                        ) {
                            genders.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption, color = Color.White) },
                                    onClick = {
                                        gender = selectionOption
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                Text("PHYSICAL ATTRIBUTES", style = MaterialTheme.typography.labelLarge, color = NeonBlue, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text("Height (cm)", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.CornerMedium),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.CornerMedium),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonBlue,
                            unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                Text("ACADEMIC & SPORT", style = MaterialTheme.typography.labelLarge, color = NeonBlue, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                
                ExposedDropdownMenuBox(
                    expanded = sportExpanded,
                    onExpandedChange = { sportExpanded = !sportExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = sport,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Primary Sport", color = TextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sportExpanded) },
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
                        expanded = sportExpanded,
                        onDismissRequest = { sportExpanded = false },
                        modifier = Modifier.background(DarkSurface)
                    ) {
                        sports.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = Color.White) },
                                onClick = {
                                    sport = selectionOption
                                    sportExpanded = false
                                }
                            )
                        }
                    }
                }
                
                OutlinedTextField(
                    value = school,
                    onValueChange = { school = it },
                    label = { Text("School / Academy Name", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(Dimens.CornerMedium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Village / Location", color = TextSecondary) },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(Dimens.CornerMedium),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonBlue,
                        unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))
            
            if (isLoading) {
                CircularProgressIndicator(color = NeonBlue)
            } else {
                CustomButton(
                    text = "REGISTER ATHLETE",
                    onClick = {
                        if (name.isBlank() || age.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Name and Age are required") }
                            return@CustomButton
                        }
                        
                        isLoading = true
                        viewModel.addAthlete(
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            gender = gender,
                            school = school,
                            sport = sport,
                            height = height.toDoubleOrNull() ?: 0.0,
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            location = location,
                            photoUri = photoUri
                        )
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("Athlete registered successfully!")
                            isLoading = false
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
        }
    }
}
