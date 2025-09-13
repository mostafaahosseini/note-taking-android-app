package com.example.simple_note_test.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.R
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simple_note_test.ui.screens.ScreenRoutes
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: com.example.simple_note_test.ui.screens.SettingsViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val profile by viewModel.profile.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) }, // To center title
            )
        },
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Text(
                    text = "Taha Notes v1.1",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Profile section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Show avatar image if available, otherwise default avatar (portrait_placeholder)
                val avatarPainter = if (profile?.avatar?.isNotEmpty() == true) {
                    // For remote avatars, an image loading library is required; fall back to bundled placeholder here
                    painterResource(id = R.drawable.portrait_placeholder)
                } else {
                    painterResource(id = R.drawable.portrait_placeholder)
                }

                Image(
                    painter = avatarPainter,
                    contentDescription = "Profile avatar",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile?.username ?: "Loading...",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Use outlined email icon (no filled background)
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Email",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = profile?.email ?: "Loading...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            // App Settings label
            Text(
                text = "APP SETTINGS",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Change Password row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(ScreenRoutes.ChangePassword.route) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Outlined rounded square with lock icon inside (like screenshot)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color(0xFF504EC3)), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Change Password",
                        tint = Color(0xFF504EC3),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Change Password",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Chevron",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            // Log Out row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            viewModel.logout()
                            navController.navigate(ScreenRoutes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Outlined rounded box with red border and exit icon inside
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color(0xFFCE3A54)), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = "Log Out",
                        tint = Color(0xFFCE3A54),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFCE3A54))
                )
            }
        }
    }
}
