package com.example.simple_note_test.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator
import com.example.simple_note_test.ui.widgets.NotesTextField
import com.example.simple_note_test.ui.controls.OutlinedButton
import com.example.simple_note_test.ui.theme.GreyBase
import com.example.simple_note_test.ui.theme.GreyDark
import com.example.simple_note_test.ui.theme.Primary
import com.example.simple_note_test.util.NetworkResult

@Composable
fun LoginScreen(navController: NavController, viewModel: com.example.simple_note_test.ui.screens.LoginViewModel = hiltViewModel()) {
    // Local form state (renamed variables)
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    // Observe ViewModel state
    val loginState by viewModel.loginState.collectAsState()
    var transientError by remember { mutableStateOf<String?>(null) }

    // Compute derived boolean for loading to avoid repeated instanceof checks in UI
    val isLoading by remember(loginState) { derivedStateOf { loginState is NetworkResult.Loading } }

    // React to state changes (navigation on success, capture error message)
    LaunchedEffect(loginState) {
        when (val s = loginState) {
            is NetworkResult.Success<*> -> {
                // Expecting a boolean payload indicating success
                val ok = (s.data as? Boolean) ?: false
                if (ok) {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            is NetworkResult.Error -> {
                transientError = s.message
            }
            else -> {
                // No-op for Loading or Idle
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
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Let’s Login",
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
                text = "And notes your idea",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = GreyDark,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                value = usernameInput,
                onValueChange = { usernameInput = it },
                placeholder = "Example: johndoe",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                value = passwordInput,
                onValueChange = { passwordInput = it },
                placeholder = "********",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                text = if (isLoading) "Logging in..." else "Login",
                onClick = { viewModel.login(usernameInput, passwordInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow",
                        tint = Color.White
                    )
                }
            )

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            transientError?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = err,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = GreyBase)
                Text(
                    text = "  Or  ",
                    color = GreyBase,
                    fontSize = 14.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = GreyBase)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don’t have any account? Register here",
                color = Primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { navController.navigate(com.example.simple_note_test.ui.screens.ScreenRoutes.Register.route) }
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
