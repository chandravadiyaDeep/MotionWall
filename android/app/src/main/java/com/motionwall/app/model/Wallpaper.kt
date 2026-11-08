package com.motionwall.app.model

import androidx.compose.ui.graphics.Color

/** Immutable wallpaper metadata loaded from the bundled V0.3 catalog. */
data class Wallpaper(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val tags: List<String>,
    val colors: List<Color>,
    val thumbnailResId: Int,
    val source: String,
    val license: String
)
