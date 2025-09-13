package com.example.simple_note_test.ui.components

// This file previously contained the full NotesTextField implementation.
// It's now a small wrapper that delegates to the implementation in ui.widgets
// so callers importing from ui.components will still work while the real
// implementation lives in ui.widgets.

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.simple_note_test.ui.theme.NotesShapes

@Composable
fun NotesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true
) {
    com.example.simple_note_test.ui.widgets.NotesTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        isPassword = isPassword,
        enabled = enabled
    )
}
