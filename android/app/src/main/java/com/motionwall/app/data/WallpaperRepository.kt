package com.motionwall.app.data

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.motionwall.app.R
import com.motionwall.app.model.Wallpaper
import org.json.JSONArray
import org.json.JSONObject

/** Loads the bundled, versioned wallpaper catalog without network access. */
class WallpaperRepository {
    fun load(context: Context): CatalogResult = try {
        val json = context.assets.open("wallpapers.json").bufferedReader().use { it.readText() }
        CatalogResult.Success(parseCatalog(json, ::resolveArtwork))
    } catch (error: Exception) {
        CatalogResult.Failure(error.message ?: "The local wallpaper catalog could not be loaded.")
    }

    /** Kept pure apart from the injected drawable resolver so catalog rules can be unit-tested. */
    internal fun parseCatalog(
        json: String,
        artworkResolver: (String) -> Int
    ): List<Wallpaper> {
        val root = JSONObject(json)
        require(root.optInt("schemaVersion") == 1) { "Unsupported wallpaper catalog version." }
        val entries = root.optJSONArray("wallpapers")
            ?: throw IllegalArgumentException("The wallpaper catalog has no wallpapers array.")
        require(entries.length() > 0) { "The wallpaper catalog is empty." }

        val wallpapers = (0 until entries.length()).map { index ->
            val item = entries.getJSONObject(index)
            val id = item.requiredText("id")
            val artworkName = item.requiredText("artwork")
            val artworkId = artworkResolver(artworkName)
            require(artworkId != 0) { "Missing artwork asset: $artworkName" }
            val colors = item.getJSONArray("colors").toStringList().map(::parseCatalogColor)
            require(colors.size >= 2) { "Wallpaper $id needs at least two colors." }
            Wallpaper(
                id = id,
                title = item.requiredText("title"),
                category = item.requiredText("category"),
                description = item.requiredText("description"),
                tags = item.getJSONArray("tags").toStringList(),
                colors = colors,
                thumbnailResId = artworkId,
                source = item.requiredText("source"),
                license = item.requiredText("license")
            )
        }
        require(wallpapers.map { it.id }.distinct().size == wallpapers.size) {
            "Wallpaper IDs must be unique."
        }
        return wallpapers
    }

    private fun resolveArtwork(name: String): Int = when (name) {
        "nebula" -> R.drawable.wallpaper_nebula
        "cyber" -> R.drawable.wallpaper_cyber
        "velvet" -> R.drawable.wallpaper_velvet
        "solar" -> R.drawable.wallpaper_solar
        "emerald" -> R.drawable.wallpaper_emerald
        "amethyst" -> R.drawable.wallpaper_amethyst
        "rose" -> R.drawable.wallpaper_rose
        "arctic" -> R.drawable.wallpaper_arctic
        else -> 0
    }
}

sealed interface CatalogResult {
    data class Success(val wallpapers: List<Wallpaper>) : CatalogResult
    data class Failure(val message: String) : CatalogResult
}

private fun JSONObject.requiredText(key: String): String =
    optString(key).trim().also { require(it.isNotEmpty()) { "Catalog field '$key' is missing." } }

private fun JSONArray.toStringList(): List<String> =
    (0 until length()).map { getString(it).trim() }.filter { it.isNotEmpty() }

/** Parse #RRGGBB or #AARRGGBB without relying on Android framework calls. */
private fun parseCatalogColor(value: String): Color {
    val hex = value.removePrefix("#")
    require(hex.length == 6 || hex.length == 8) { "Invalid catalog color: $value" }
    val argb = if (hex.length == 6) "FF$hex" else hex
    return Color(argb.toLong(16))
}
