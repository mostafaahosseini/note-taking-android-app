package com.example.simple_note_test.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.GreyLight
import com.example.simple_note_test.ui.theme.NotesShapes

@Composable
fun NoteCard(
    title: String,
    preview: String,
    lastEdited: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White
) {
    Surface(
        shape = NotesShapes.small,
        tonalElevation = 1.dp,
        color = backgroundColor,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = preview,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = lastEdited,
                style = MaterialTheme.typography.bodySmall,
                color = GreyLight
            )
        }
    }
}
