package vip.mystery0.l.skipper.ui.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TextEditDialog(
    onDismissRequest: () -> Unit,
    title: String,
    storedValue: String,
    onSave: (String) -> Unit,
    onCheck: (String) -> Boolean,
) {
    var currentInput by remember { mutableStateOf(TextFieldValue(storedValue)) }
    var isValid by remember { mutableStateOf(onCheck(storedValue)) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            TextField(currentInput, onValueChange = {
                isValid = onCheck(it.text)
                currentInput = it
            })
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onSave(currentInput.text)
                    onDismissRequest()
                },
            ) {
                Text("保存")
            }
        },
    )
}