package com.example.simple_note_test.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_note_test.ui.widgets.NotesTextField
import com.example.simple_note_test.ui.controls.OutlinedButton
import com.example.simple_note_test.ui.theme.GreyBase
import com.example.simple_note_test.ui.theme.GreyDark
import com.example.simple_note_test.ui.theme.Primary
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import com.example.simple_note_test.util.NetworkResult
import com.example.simple_note_test.data.models.RegisterRequest

@Composable
fun RegisterScreen(navController: NavController, viewModel: com.example.simple_note_test.ui.screens.RegisterViewModel = hiltViewModel()) {
    var givenName by remember { mutableStateOf("") }
    var familyName by remember { mutableStateOf("") }
    var userHandle by remember { mutableStateOf("") }
    var emailAddr by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var pwdConfirm by remember { mutableStateOf("") }
    val state by viewModel.registerState.collectAsState()
    var transientError by remember { mutableStateOf<String?>(null) }

    // Handle navigation on success and capture errors
    LaunchedEffect(state) {
        when (state) {
            is NetworkResult.Success -> {
                navController.navigate("auth/login") {
                    popUpTo(0) { inclusive = true }
                }
            }
            is NetworkResult.Error -> {
                transientError = (state as NetworkResult.Error).message
            }
            else -> {
                // Idle or Loading -> no action
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.popBackStack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to Login",
                    tint = Primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Login",
                    color = Primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Register",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "And start taking notes",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = GreyDark,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "First Name",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = givenName,
                onValueChange = { givenName = it },
                placeholder = "Example: Taha",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Last Name",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = familyName,
                onValueChange = { familyName = it },
                placeholder = "Example: Hamifar",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Username",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = userHandle,
                onValueChange = { userHandle = it },
                placeholder = "Example: @HamifarTaha",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Email Address",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = emailAddr,
                onValueChange = { emailAddr = it },
                placeholder = "Example: hamifar.taha@gmail.com",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = pwd,
                onValueChange = { pwd = it },
                placeholder = "********",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Retype Password",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF180E25),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesTextField(
                value = pwdConfirm,
                onValueChange = { pwdConfirm = it },
                placeholder = "********",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedButton(
                text = if (state is NetworkResult.Loading) "Registering..." else "Register",
                onClick = {
                    if (pwd != pwdConfirm) {
                        transientError = "Passwords do not match"
                    } else {
                        viewModel.register(
                            RegisterRequest(
                                first_name = givenName,
                                last_name = familyName,
                                username = userHandle,
                                email = emailAddr,
                                password = pwd
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state !is NetworkResult.Loading,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow",
                        tint = Color.White
                    )
                }
            )
            if (state is NetworkResult.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
            transientError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Already have an account? Login here",
                color = Primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
