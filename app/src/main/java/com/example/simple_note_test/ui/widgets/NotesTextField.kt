package com.example.simple_note_test.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.GreyBase
import com.example.simple_note_test.ui.theme.NotesShapes
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@Composable
fun NotesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    var showPassword by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        modifier = modifier
            .height(54.dp)
            .clip(NotesShapes.small)
    ) { content ->
        Box(
            modifier = Modifier
                .height(54.dp)
                .clip(NotesShapes.small)
                .border(1.dp, GreyBase, NotesShapes.small)
                .padding(start = 16.dp, end = if (isPassword) 44.dp else 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(color = GreyBase)
                )
            }
            content()

            if (isPassword) {
                IconButton(
                    onClick = { showPassword = !showPassword },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(if (showPassword) "🙈" else "👁️")
                }
            }
        }
    }
}
