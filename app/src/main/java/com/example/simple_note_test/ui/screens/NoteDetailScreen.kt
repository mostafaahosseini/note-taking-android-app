package com.example.simple_note_test.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(navController: NavController, id: String) {
    val viewModel: NoteDetailViewModel = hiltViewModel()
    val noteState by viewModel.note.collectAsState()
    val editState by viewModel.editState.collectAsState()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf<String?>(null) }
    var initialized by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Load note on entry
    LaunchedEffect(id) {
        viewModel.loadNote(id)
    }

    // Initialize fields when note is loaded
    LaunchedEffect(noteState) {
        noteState?.let {
            if (!initialized) {
                title = it.title
                description = it.description
                initialized = true
            }
        }
    }

    // Handle save/delete success
    LaunchedEffect(editState) {
        if (editState is com.example.simple_note_test.util.NetworkResult.Success && initialized) {
            showError = null // Clear error state on success
            navController.popBackStack(ScreenRoutes.Home.route, false)
        } else if (editState is com.example.simple_note_test.util.NetworkResult.Error) {
            // Only show error if not navigating away
            showError = (editState as com.example.simple_note_test.util.NetworkResult.Error).message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        // Allow navigation back to Home even if fields are blank, without showing error
                        navController.popBackStack(ScreenRoutes.Home.route, false)
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Last edited on " + (noteState?.updated_at?.let { formatTime(it) } ?: "-"),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = description,
                onValueChange = { description = it },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) {
                        Text(
                            text = "Feel Free to Write Here…",
                            style = TextStyle(
                                fontSize = 18.sp,
                                color = Color.LightGray
                            )
                        )
                    }
                    innerTextField()
                }
            )
            if (editState is com.example.simple_note_test.util.NetworkResult.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (showError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = showError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (showDeleteDialog) {
            ModalBottomSheet(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White
            ) {
                Box(Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { showDeleteDialog = false },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Want to Delete this Note?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        showDeleteDialog = false
                        showError = null // Clear error state before delete
                        viewModel.deleteNote(id) {
                            showError = null // Clear error state after delete
                            navController.popBackStack(ScreenRoutes.Home.route, false)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Delete Note", color = Color.Red)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

private fun formatTime(isoString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val date = parser.parse(isoString)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.format(date ?: Date())
    } catch (e: Exception) {
        "-"
    }
}
