package com.example.workouttracker.ui.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun MainScreenButton(
    modifier: Modifier = Modifier,
    buttonText: String = "Click Me",
    borderColor: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Text(
        text = buttonText,
        modifier = modifier
            .border(BorderStroke(1.dp, Color.Black))
            .padding(16.dp)
            .fillMaxWidth(0.5f)
            .clickable { onClick() },
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )
}