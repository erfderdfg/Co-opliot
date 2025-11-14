package com.app.co_opilot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputField(
    label: String,
    value: String?,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value ?: "",
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ListInputField(
    label: String,
    items: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    var newItem by remember { mutableStateOf("") }

    Column(Modifier.fillMaxWidth()) {
        // Input to add new item
        OutlinedTextField(
            value = newItem,
            onValueChange = { newItem = it },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default,
        )

        Button(
            onClick = {
                if (newItem.isNotBlank()) {
                    onAdd(newItem)
                    newItem = ""
                }
            },
            modifier = Modifier.padding(top = 6.dp)
        ) {
            Text("Add")
        }

        // Existing items
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                TextButton(onClick = { onRemove(item) }) { Text("Remove") }
            }
        }
    }
    Spacer(Modifier.height(10.dp))
}