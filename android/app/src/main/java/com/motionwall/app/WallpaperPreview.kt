package com.motionwall.app

import androidx.compose.ui.graphics.Color

/** Local catalog item. Gradient colours are original placeholders pending licensed artwork. */
data class WallpaperPreview(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val tags: List<String>,
    val colors: List<Color>
)
