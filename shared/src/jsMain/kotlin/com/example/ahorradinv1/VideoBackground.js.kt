package com.example.ahorradinv1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun VideoBackground(modifier: Modifier, videoUrl: String, isPlaying: Boolean) {
    Box(modifier = modifier.background(Color.Black))
}
