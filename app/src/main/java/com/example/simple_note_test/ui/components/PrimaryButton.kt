package com.example.simple_note_test.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.NotesShapes
import com.example.simple_note_test.ui.theme.Primary

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    val active = enabled && !loading

    Button(
        onClick = onClick,
        enabled = active,
        shape = NotesShapes.large,
        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth()
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(text, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}
