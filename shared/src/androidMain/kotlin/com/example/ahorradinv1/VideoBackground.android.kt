package com.example.ahorradinv1

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun VideoBackground(modifier: Modifier, videoUrl: String, isPlaying: Boolean) {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Usamos MediaPlayer + TextureView para que el Modifier.blur() funcione correctamente
    // TextureView permite que Compose aplique efectos visuales sobre el contenido del video
    val mediaPlayer = remember {
        MediaPlayer().apply {
            val packageName = context.packageName
            val resId = context.resources.getIdentifier("background_video", "raw", packageName)
            if (resId != 0) {
                setDataSource(context, Uri.parse("android.resource://$packageName/$resId"))
                isLooping = true
                setVolume(0f, 0f)
                prepareAsync()
            }
        }
    }

    // Gestionar pausa/reproducción
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            mediaPlayer.start()
        } else {
            mediaPlayer.pause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextureView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                
                surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(st: SurfaceTexture, w: Int, h: Int) {
                        mediaPlayer.setSurface(Surface(st))
                    }
                    override fun onSurfaceTextureSizeChanged(st: SurfaceTexture, w: Int, h: Int) {}
                    override fun onSurfaceTextureDestroyed(st: SurfaceTexture): Boolean {
                        mediaPlayer.setSurface(null)
                        return true
                    }
                    override fun onSurfaceTextureUpdated(st: SurfaceTexture) {}
                }
            }
        }
    )
}
