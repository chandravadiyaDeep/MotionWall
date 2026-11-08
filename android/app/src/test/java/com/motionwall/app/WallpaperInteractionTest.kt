package com.motionwall.app

import com.motionwall.app.data.WallpaperRepository
import com.motionwall.app.data.filterWallpapers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** V0.3 regression tests for catalog filtering and favorite transformations. */
class WallpaperInteractionTest {
    private val catalog = WallpaperRepository().parseCatalog(
        """
        {
          "schemaVersion": 1,
          "wallpapers": [
            {"id":"nebula-bloom","title":"Nebula Bloom","category":"Aesthetic","description":"Cosmic purple gradient","tags":["cosmic","purple"],"colors":["#FF8BCB","#7048C8"],"artwork":"nebula","source":"MotionWall original","license":"CC0-1.0"},
            {"id":"cyber-pulse","title":"Cyber Pulse","category":"Gen Z","description":"Electric blue energy","tags":["cyber","blue"],"colors":["#66E5FF","#4C58D8"],"artwork":"cyber","source":"MotionWall original","license":"CC0-1.0"},
            {"id":"solar-drift","title":"Solar Drift","category":"Aesthetic","description":"Warm sunset atmosphere","tags":["sunset","orange"],"colors":["#FFB36B","#E45B9A"],"artwork":"solar","source":"MotionWall original","license":"CC0-1.0"}
          ]
        }
        """.trimIndent(),
        artworkResolver = { 1 }
    )

    @Test
    fun searchMatchesTitleWithoutCaseSensitivity() {
        val result = filterWallpapers(catalog, "All", "NEBULA")
        assertEquals(listOf("nebula-bloom"), result.map { it.id })
    }

    @Test
    fun searchMatchesTags() {
        val result = filterWallpapers(catalog, "All", "sunset")
        assertEquals(listOf("solar-drift"), result.map { it.id })
    }

    @Test
    fun categoryAndSearchFiltersAreCombined() {
        val result = filterWallpapers(catalog, "Aesthetic", "solar")
        assertEquals(listOf("solar-drift"), result.map { it.id })
    }

    @Test
    fun categoriesCanBeDerivedFromTheCatalog() {
        assertEquals(listOf("Aesthetic", "Gen Z"), catalog.map { it.category }.distinct().sorted())
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
