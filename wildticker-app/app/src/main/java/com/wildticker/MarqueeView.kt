package com.wildticker

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MarqueeView(text: String) {
    // Minimal placeholder; replace with animated
    Text(text = text, modifier = Modifier.fillMaxWidth())
}
