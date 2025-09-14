package com.example.simple_note_test.ui.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(navController: NavController, id: String?) {
    val viewModel: com.example.simple_note_test.ui.screens.NoteEditViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var lastEditedTime by remember { mutableStateOf<String?>(null) }

    val editState by viewModel.editState.collectAsState()
    val noteState by viewModel.noteState.collectAsState()
    val generationState by viewModel.generationState.collectAsState()
    var showError by remember { mutableStateOf<String?>(null) }
    val isEditingExistingNote = id != null

    // Load existing note if we're editing
    LaunchedEffect(id) {
        if (id != null) {
            viewModel.loadNote(id)
        }
    }

    // Handle note loading
    LaunchedEffect(noteState) {
        when (val state = noteState) {
            is com.example.simple_note_test.util.NetworkResult.Success -> {
                val note = state.data
                title = note.title
                description = note.description
                lastEditedTime = note.updated_at
            }
            is com.example.simple_note_test.util.NetworkResult.Loading -> {
                // no-op
            }
            is com.example.simple_note_test.util.NetworkResult.Error -> {
                showError = state.message
            }
            else -> { }
        }
    }

    // When generation succeeds, populate description
    LaunchedEffect(generationState) {
        when (generationState) {
            is com.example.simple_note_test.util.NetworkResult.Success -> {
                val generated = (generationState as com.example.simple_note_test.util.NetworkResult.Success).data
                // Only set description if still empty to avoid overwriting user's typing
                if (description.isBlank()) description = generated
            }
            is com.example.simple_note_test.util.NetworkResult.Error -> {
                // expose error to UI
                showError = (generationState as com.example.simple_note_test.util.NetworkResult.Error).message
            }
            else -> {}
        }
    }

    // Handle save/delete success
    LaunchedEffect(editState) {
        when (val state = editState) {
            is com.example.simple_note_test.util.NetworkResult.Success -> {
                navController.navigate(com.example.simple_note_test.ui.screens.ScreenRoutes.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            is com.example.simple_note_test.util.NetworkResult.Error -> {
                showError = state.message
            }
            else -> {}
        }
    }

    // Function to save note
    fun saveNote() {
        if (title.isNotBlank() || description.isNotBlank()) {
            if (isEditingExistingNote) {
                viewModel.updateNote(id, title, description)
            } else {
                viewModel.createNote(title, description)
            }
        } else {
            // If both are empty, just navigate back without saving
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { saveNote() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Loading indicator when an operation is in progress
                    if (editState is com.example.simple_note_test.util.NetworkResult.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
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
                    text = lastEditedTime?.let { "Last updated: $it" } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))

                // Keep a single prominent red delete button in the bottom bar when editing so it's easy to find
                if (isEditingExistingNote) {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Note",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                // Show generate button when title is present and body is empty
                if (title.isNotBlank() && description.isBlank()) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        val isGenerating = generationState is com.example.simple_note_test.util.NetworkResult.Loading
                        Button(
                            onClick = { viewModel.generateBodyFromTitle(title) },
                            enabled = !isGenerating,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generating...")
                            } else {
                                Text("Generate body from title")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
                if (showError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = showError!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }

    // Confirmation sheet for deleting a note (styled to match screenshot)
    if (showDeleteDialog && isEditingExistingNote) {
        ModalBottomSheet(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.Transparent
        ) {
            // Use a rounded white Surface as the visible sheet area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    // Title row with small close button on the right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Want to Delete this Note?",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { showDeleteDialog = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    }

                    Divider()

                    Spacer(modifier = Modifier.height(8.dp))

                    // Red delete row similar to screenshot: icon + label
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDeleteDialog = false
                                id?.let { viewModel.deleteNote(it) }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Delete Note", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
