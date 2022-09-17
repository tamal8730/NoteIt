package com.github.tamal8730.noteit.core.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun NoteItTextField(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle? = null,
    hint: String,
    hintStyle: TextStyle? = null,
    maxLines: Int = Int.MAX_VALUE,
    imeAction: ImeAction = ImeAction.Default,
    onValueChanged: (String) -> Unit,
    onImeAction: ((String) -> Unit)? = null,
) {

    Box(modifier = modifier) {
        if (text.isEmpty()) {
            Text(
                text = hint,
                style = hintStyle ?: MaterialTheme.typography.body1.copy(
                    color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
                )
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = text,
                onValueChange = { onValueChanged(it) },
                modifier = Modifier.weight(1f),
                singleLine = maxLines == 1,
                maxLines = maxLines,
                textStyle = textStyle
                    ?: MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.onSurface
                    ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.primary,
                    1.00f to MaterialTheme.colors.primary
                ),
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions {
                    if (text.isNotEmpty() && onImeAction != null) {
                        onImeAction(text)
                    }
                }
            )
        }
    }

}