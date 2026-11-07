package com.motionwall.app

import com.motionwall.app.data.filterWallpapers
import com.motionwall.app.data.sampleWallpapers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** V0.2 regression tests for deterministic catalog filtering and favorite updates. */
class WallpaperInteractionTest {
    @Test
    fun searchMatchesTitleWithoutCaseSensitivity() {
        val result = filterWallpapers(sampleWallpapers, "All", "NEBULA")
        assertEquals(listOf("nebula-bloom"), result.map { it.id })
    }

    @Test
    fun searchMatchesTags() {
        val result = filterWallpapers(sampleWallpapers, "All", "sunset")
        assertEquals(listOf("solar-drift"), result.map { it.id })
    }

    @Test
    fun categoryAndSearchFiltersAreCombined() {
        val result = filterWallpapers(sampleWallpapers, "Aesthetic", "solar")
        assertEquals(listOf("solar-drift"), result.map { it.id })
    }

    @Test
    fun allCategoryDoesNotRemoveSearchResults() {
        val result = filterWallpapers(sampleWallpapers, "All", "purple")
        assertTrue(result.isNotEmpty())
        assertTrue(result.all { wallpaper -> wallpaper.tags.any { it == "purple" } || wallpaper.description.contains("purple", true) || wallpaper.title.contains("purple", true) })
    }

    @Test
    fun favoriteCanBeAddedAndRemovedWithoutMutatingOriginalSet() {
        val initial = emptySet<String>()
        val added = toggleFavorite(initial, "nebula-bloom")
        val removed = toggleFavorite(added, "nebula-bloom")
        assertTrue("nebula-bloom" in added)
        assertFalse("nebula-bloom" in removed)
        assertTrue(initial.isEmpty())
    }
}
