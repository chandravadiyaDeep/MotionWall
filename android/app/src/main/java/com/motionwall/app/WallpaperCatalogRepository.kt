package com.motionwall.app

import android.content.Context
import org.json.JSONArray
import androidx.compose.ui.graphics.Color

/** Loads and validates the bundled catalog without allowing malformed data to crash app startup. */
object WallpaperCatalogRepository {
    fun load(context: Context): List<WallpaperPreview> = runCatching {
        val source = context.assets.open("wallpapers.json").bufferedReader().use { it.readText() }
        val records = JSONArray(source)
        buildList {
            for (index in 0 until records.length()) {
                val item = records.optJSONObject(index) ?: continue
                val id = item.optString("id").trim()
                val title = item.optString("title").trim()
                val category = item.optString("category").trim()
                if (id.isEmpty() || title.isEmpty() || category.isEmpty()) continue
                val tagItems = item.optJSONArray("tags") ?: JSONArray()
                val tags = buildList {
                    for (tagIndex in 0 until tagItems.length()) {
                        tagItems.optString(tagIndex).trim().takeIf(String::isNotEmpty)?.let(::add)
                    }
                }
                val colorItems = item.optJSONArray("colors") ?: JSONArray()
                val colors = buildList {
                    for (colorIndex in 0 until colorItems.length()) {
                        runCatching { Color(android.graphics.Color.parseColor(colorItems.optString(colorIndex))) }
                            .getOrNull()?.let(::add)
                    }
                }
                if (colors.size >= 2) add(WallpaperPreview(id, title, category, item.optString("description"), tags, colors))
            }
        }.ifEmpty { fallbackCatalog() }
    }.getOrElse { fallbackCatalog() }

    // The fallback keeps browsing available if the asset is missing or malformed.
    private fun fallbackCatalog() = listOf(
        WallpaperPreview("fallback-nebula", "Nebula Bloom", "Aesthetic", "Soft cosmic colour flow", listOf("space", "pink"), listOf(Color(0xFFFF8BCB), Color(0xFF7048C8))),
        WallpaperPreview("fallback-cyber", "Cyber Pulse", "Gen Z", "Electric blue energy", listOf("neon", "blue"), listOf(Color(0xFF66E5FF), Color(0xFF4C58D8)))
    )
}
