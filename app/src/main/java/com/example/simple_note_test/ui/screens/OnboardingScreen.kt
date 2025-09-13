package com.example.simple_note_test.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simple_note_test.R
import com.example.simple_note_test.ui.controls.OutlinedButton
import com.example.simple_note_test.ui.theme.Primary
import com.example.simple_note_test.ui.theme.White

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            // Illustration
            Image(
                painter = painterResource(id = R.drawable.onboarding_illustration),
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Jot Down anything you want to achieve, today or in the future",
                color = White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                text = "Let's Get Started",
                onClick = onGetStarted,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                onDark = true, // چون پس‌زمینه Primary است
                trailingIcon = { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
