package com.example.simple_note_test.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.NotesShapes
import com.example.simple_note_test.ui.theme.Primary
import com.example.simple_note_test.ui.theme.GreyBase
import com.example.simple_note_test.ui.theme.White

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDark: Boolean = false,                 // when background is dark/purple use true
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    // New behavior: render as filled pill button for primary actions
    val container = if (onDark) White else Primary
    val content = if (onDark) Primary else White

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = NotesShapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.5f),
            disabledContentColor = content.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyLarge, color = content)
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                CompositionLocalProvider(LocalContentColor provides content) {
                    it()
                }
            }
        }
    }
}
