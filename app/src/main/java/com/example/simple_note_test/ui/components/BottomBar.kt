package com.example.simple_note_test.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.Primary
import com.example.simple_note_test.ui.theme.White

@Composable
fun BottomBar(
    selected: BottomBarItem,
    onHome: () -> Unit,
    onFab: () -> Unit,
    onSettings: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHome) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home",
                    tint = if (selected == BottomBarItem.Home) Primary else Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(48.dp)) // Space for FAB
            IconButton(onClick = onSettings) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = if (selected == BottomBarItem.Settings) Primary else Color.Gray
                )
            }
        }
        FloatingActionButton(
            onClick = onFab,
            containerColor = Primary,
            contentColor = White,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Note")
        }
    }
}

enum class BottomBarItem { Home, Settings }
