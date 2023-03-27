package com.otraupe.fallingwords.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otraupe.fallingwords.R

@Composable
fun InstructionsDialog(visible: Boolean, onDismiss: () -> Unit) {
    if (visible) {
        AlertDialog(
            onDismissRequest = { /* not allowed */ },
            title = {
                Text(text = stringResource(id = R.string.ui_instructions_title))
            },
            text = {
                Text(text = stringResource(id = R.string.ui_instructions_body))
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(id = R.string.ui_instructions_ok))
                }
            },
        )
    }
}