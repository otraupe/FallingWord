package com.otraupe.fallingwords.ui.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
                Text(text = stringResource(id = R.string.ui_instructions_body, stringResource(id = R.string.ui_buttons_next_pairing)))
            },
            confirmButton = {
                TextButton(
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