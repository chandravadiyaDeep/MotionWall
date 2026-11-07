package com.motionwall.app.model

import androidx.compose.ui.graphics.Color

/**
 * Immutable local wallpaper metadata used by the V0.2 UI preview.
 *
 * Artwork remains generated gradient placeholders until the licensed catalog is introduced in V0.3.
 */
data class Wallpaper(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val tags: List<String>,
    val colors: List<Color>
)
