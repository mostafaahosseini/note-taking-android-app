package com.example.simple_note_test.ui.screens.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.simple_note_test.ui.cards.NoteCard
import com.example.simple_note_test.util.NetworkResult
import com.example.simple_note_test.ui.screens.ScreenRoutes
import com.example.simple_note_test.ui.theme.Primary
import com.example.simple_note_test.ui.theme.White
import com.example.simple_note_test.ui.theme.Background
import com.example.simple_note_test.ui.theme.Black
import com.example.simple_note_test.ui.theme.GreyDark
import com.example.simple_note_test.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: com.example.simple_note_test.ui.screens.HomeViewModel = hiltViewModel()) {
    val notesState by viewModel.notesState.collectAsState()
    var query by remember { mutableStateOf("") }

    val allNotes: List<com.example.simple_note_test.data.models.NoteResponse> = when (val s = notesState) {
        is NetworkResult.Success -> s.data
        else -> emptyList()
    }
    val filteredNotes = if (query.isBlank()) allNotes else allNotes.filter { it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }

    val hasAnyNotes = allNotes.isNotEmpty()

    val palette = listOf(
        Color(0xFFFFF9C4),
        Color(0xFFFFF8E1),
        Color(0xFFFFFDE7),
        Color(0xFFFFF3E0),
        Color(0xFFE1F5FE),
        Color(0xFFE8F5E9),
        Color(0xFFF3E5F5),
        Color(0xFFFFEBEE)
    )

    LaunchedEffect(notesState) {
        if (notesState is NetworkResult.Error && (notesState as NetworkResult.Error).message.contains("No access token found")) {
            navController.navigate(ScreenRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    val fabSize = 64.dp
    val bottomBarHeight = 72.dp

    Scaffold(
        topBar = {
            if (hasAnyNotes) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search…") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Spacer(modifier = Modifier.height(0.dp))
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ScreenRoutes.NoteEdit.route) },
                containerColor = Primary,
                contentColor = White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                shape = CircleShape,
                modifier = Modifier
                    .size(fabSize)
                    .offset(y = (-16).dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Note")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            Surface(shadowElevation = 2.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(bottomBarHeight)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home",
                                tint = White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Home", color = Primary, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.width(fabSize))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { navController.navigate(ScreenRoutes.Settings.route) }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Settings", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = notesState) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NetworkResult.Error -> {
                    Text(
                        text = (state as NetworkResult.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is NetworkResult.Success -> {
                    if (!hasAnyNotes) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Background)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = bottomBarHeight + 24.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(48.dp))
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = R.drawable.home_background),
                                    contentDescription = "Onboarding Illustration",
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .heightIn(min = 180.dp)
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Start Your Journey",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 32.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Every big step start with small step.\nNotes your first idea and start your journey!",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = GreyDark,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 48.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    } else {
                        if (filteredNotes.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "No notes found.", style = MaterialTheme.typography.bodyLarge, color = GreyDark)
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                items(filteredNotes) { note ->
                                    val color = palette[filteredNotes.indexOf(note) % palette.size]
                                    NoteCard(
                                        title = note.title,
                                        preview = note.description,
                                        lastEdited = note.updated_at,
                                        onClick = { navController.navigate(ScreenRoutes.NoteEdit.createRoute(note.id)) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        backgroundColor = color
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                }
            }
        }
    }
}
