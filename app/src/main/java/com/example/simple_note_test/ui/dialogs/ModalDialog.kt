package com.example.simple_note_test.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.simple_note_test.ui.theme.NotesShapes
import com.example.simple_note_test.ui.controls.OutlinedButton
import com.example.simple_note_test.ui.controls.PrimaryButton

@Composable
fun ModalDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String,
    dismissText: String,
    confirmEnabled: Boolean = true
) {
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Surface(
            shape = NotesShapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier
                .padding(32.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, style = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(16.dp))
                Text(message, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(24.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        text = dismissText
                    )
                    PrimaryButton(
                        onClick = onConfirm,
                        text = confirmText,
                        enabled = confirmEnabled
                    )
                }
            }
        }
    }
}
