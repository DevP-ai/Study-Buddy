package com.developer.android.dev.technologia.androidapp.studybuddy.presentation.components

import android.icu.text.CaseMap.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AddSubjectDialog(
    title: String = "Add/Update Subject",
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text(text = title) },
            text = {
                Text(text = "Dialog Body")
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirmClick() }) {
                    Text(text = "Save")
                }
            }
        )
    }
}