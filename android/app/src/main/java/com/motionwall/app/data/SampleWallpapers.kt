package com.motionwall.app.data

import androidx.compose.ui.graphics.Color
import com.motionwall.app.model.Wallpaper

/**
 * Original gradient-only sample catalog. Do not add third-party or copyrighted artwork here.
 */
val sampleWallpapers = listOf(
    Wallpaper(
        id = "nebula-bloom",
        title = "Nebula Bloom",
        category = "Aesthetic",
        description = "Soft cosmic colour flow with a warm violet glow.",
        tags = listOf("cosmic", "gradient", "pink", "purple", "space"),
        colors = listOf(Color(0xFFFF8BCB), Color(0xFF7048C8), Color(0xFF17112E))
    ),
    Wallpaper(
        id = "cyber-pulse",
        title = "Cyber Pulse",
        category = "Gen Z",
        description = "Electric blue energy for a bold, futuristic screen.",
        tags = listOf("cyber", "blue", "neon", "technology", "gradient"),
        colors = listOf(Color(0xFF66E5FF), Color(0xFF4C58D8), Color(0xFF10152F))
    ),
    Wallpaper(
        id = "velvet-orbit",
        title = "Velvet Orbit",
        category = "Dark",
        description = "Deep violet ambient tones with a quiet orbital glow.",
        tags = listOf("dark", "violet", "space", "minimal", "purple"),
        colors = listOf(Color(0xFFBD8CFF), Color(0xFF542D91), Color(0xFF090713))
    ),
    Wallpaper(
        id = "solar-drift",
        title = "Solar Drift",
        category = "Aesthetic",
        description = "A warm sunset atmosphere in coral, amber, and rose.",
        tags = listOf("sunset", "orange", "warm", "pink", "gradient"),
        colors = listOf(Color(0xFFFFB36B), Color(0xFFE45B9A), Color(0xFF28112E))
    )
)
