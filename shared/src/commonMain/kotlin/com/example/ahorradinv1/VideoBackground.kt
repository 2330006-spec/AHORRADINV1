package com.example.ahorradinv1

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoBackground(modifier: Modifier, videoUrl: String, isPlaying: Boolean)
