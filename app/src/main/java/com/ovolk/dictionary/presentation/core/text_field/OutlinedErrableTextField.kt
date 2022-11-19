package com.ovolk.dictionary.presentation.core.text_field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MaxErrorLines { ONE, TWO }

@Composable
fun OutlinedErrableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(4.dp),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = Color.Gray,
        disabledTextColor = Color.Transparent,
        backgroundColor = Color.Transparent,
        focusedIndicatorColor = colorResource(id = com.ovolk.dictionary.R.color.blue),
        unfocusedIndicatorColor = Color.Gray,
        disabledIndicatorColor = Color.Transparent,
    ),
    errorMessage: String? = null,
    maxErrorLines: MaxErrorLines = MaxErrorLines.TWO
) {
    val appliedTrailingIcon = if (trailingIcon == null && isError) {
        {
            Icon(
                painter = painterResource(id = com.ovolk.dictionary.R.drawable.error_sign),
                contentDescription = "error sign",
                modifier = Modifier.size(25.dp)
            )
        }
    } else {
        trailingIcon
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            isError = isError,
            trailingIcon = appliedTrailingIcon,
            modifier = modifier.fillMaxWidth(),
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )

        val height = if (maxErrorLines == MaxErrorLines.ONE) {
            15.dp
        } else {
            30.dp
        }
        Column(modifier = Modifier.height(height)) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    maxLines = 2,
                    fontSize = 12.sp,
                    color = colorResource(id = com.ovolk.dictionary.R.color.red),
                    overflow = Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    Column() {
        OutlinedErrableTextField(
            value = "Слава Україні!",
            label = { Text(text = "label") },
            onValueChange = {},
            isError = true,
            errorMessage = "this field is required"
        )
    }
}