package com.motionwall.app.data

import com.motionwall.app.model.Wallpaper

/** Case-insensitive catalog filtering shared by the Explore screen and unit tests. */
internal fun filterWallpapers(
    wallpapers: List<Wallpaper>,
    selectedCategory: String,
    searchQuery: String
): List<Wallpaper> {
    val query = searchQuery.trim()
    return wallpapers.filter { wallpaper ->
        val categoryMatches = selectedCategory == "All" || wallpaper.category == selectedCategory
        val searchMatches = query.isEmpty() ||
            wallpaper.title.contains(query, ignoreCase = true) ||
            wallpaper.category.contains(query, ignoreCase = true) ||
            wallpaper.tags.any { it.contains(query, ignoreCase = true) }
        categoryMatches && searchMatches
    }
}
