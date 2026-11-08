package com.motionwall.app

import com.motionwall.app.data.WallpaperRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/** Validation tests for the bundled, offline wallpaper catalog contract. */
class WallpaperRepositoryTest {
    private val repository = WallpaperRepository()

    private fun catalog(wallpapers: String, schemaVersion: Int = 1): String = """
        {
          "schemaVersion": $schemaVersion,
          "wallpapers": [$wallpapers]
        }
    """.trimIndent()

    private fun wallpaper(id: String = "sample", artwork: String = "nebula"): String = """
        {
          "id": "$id",
          "title": "Sample Wallpaper",
          "category": "Aesthetic",
          "description": "A local sample",
          "tags": ["sample", "gradient"],
          "colors": ["#112233", "#AABBCC"],
          "artwork": "$artwork",
          "source": "MotionWall original",
          "license": "Pending"
        }
    """.trimIndent()

    @Test
    fun parsesTypedWallpaperAndResolvesLocalArtwork() {
        val result = repository.parseCatalog(catalog(wallpaper()), artworkResolver = { 42 }).single()
        assertEquals("sample", result.id)
        assertEquals("Aesthetic", result.category)
        assertEquals(42, result.thumbnailResId)
        assertEquals(2, result.colors.size)
    }

    @Test
    fun rejectsDuplicateWallpaperIds() {
        val json = catalog(wallpaper("same") + "," + wallpaper("same"))
        assertThrows(IllegalArgumentException::class.java) {
            repository.parseCatalog(json) { 42 }
        }
    }

    @Test
    fun rejectsUnknownArtworkAsset() {
        val json = catalog(wallpaper(artwork = "not-in-resource-map"))
        assertThrows(IllegalArgumentException::class.java) {
            repository.parseCatalog(json) { 0 }
        }
    }

    @Test
    fun rejectsUnsupportedCatalogSchema() {
        assertThrows(IllegalArgumentException::class.java) {
            repository.parseCatalog(catalog(wallpaper(), schemaVersion = 99)) { 42 }
        }
    }

    @Test
    fun rejectsEmptyCatalog() {
        assertThrows(IllegalArgumentException::class.java) {
            repository.parseCatalog(catalog("")) { 42 }
        }
    }
}
