package com.motionwall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motionwall.app.data.sampleWallpapers
import com.motionwall.app.model.Wallpaper

private val Background = Color(0xFF0B0713)
private val Panel = Color(0xFF171021)
private val Accent = Color(0xFFFF8CCF)
private val Muted = Color(0xFFB7A9C8)
private val Categories = listOf("All", "Aesthetic", "Gen Z", "Anime", "Dark", "Interactive")

/** Main Android entry point. The V0.2 app remains a native Compose application. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MotionWallTheme { MotionWallApp() } }
    }
}

/** Dark-first brand theme shared by every V0.2 destination. */
@Composable
private fun MotionWallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Accent,
            secondary = Color(0xFFAA91FF),
            background = Background,
            surface = Panel,
            onPrimary = Color(0xFF1B1026),
            onBackground = Color(0xFFFFF7FF),
            onSurface = Color(0xFFFFF7FF)
        ),
        content = content
    )
}

/**
 * App-level UI state. Favorites and preferences intentionally remain in-memory until
 * the persistence milestone (V0.7); no storage permission or database is needed here.
 */
@Composable
private fun MotionWallApp() {
    var selectedTab by remember { mutableStateOf("Home") }
    var selectedWallpaperId by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    var reduceMotion by remember { mutableStateOf(false) }
    var showArtworkHints by remember { mutableStateOf(true) }

    val detailWallpaper = sampleWallpapers.firstOrNull { it.id == selectedWallpaperId }
    val filteredWallpapers = sampleWallpapers.filter { wallpaper ->
        val categoryMatches = selectedCategory == "All" || wallpaper.category == selectedCategory
        val query = searchQuery.trim()
        val searchMatches = query.isEmpty() ||
            wallpaper.title.contains(query, ignoreCase = true) ||
            wallpaper.category.contains(query, ignoreCase = true) ||
            wallpaper.tags.any { it.contains(query, ignoreCase = true) }
        categoryMatches && searchMatches
    }
    val favoriteWallpapers = sampleWallpapers.filter { it.id in favorites }

    // Android system Back closes the detail destination before leaving the app.
    BackHandler(enabled = selectedWallpaperId != null) {
        selectedWallpaperId = null
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (selectedWallpaperId == null) {
                NavigationBar(containerColor = Color(0xFF151020), contentColor = Color.White) {
                    listOf("Home", "Explore", "Favorites", "Settings").forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = {
                                selectedTab = tab
                                selectedWallpaperId = null
                            },
                            icon = {
                                Text(
                                    text = when (tab) {
                                        "Home" -> "⌂"
                                        "Explore" -> "◈"
                                        "Favorites" -> "♡"
                                        else -> "⚙"
                                    },
                                    fontSize = 20.sp,
                                    modifier = Modifier.semantics { contentDescription = "$tab tab" }
                                )
                            },
                            label = { Text(tab, fontSize = 10.sp) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = selectedWallpaperId,
            label = "motionwall-destination",
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) { destinationId ->
            val wallpaper = sampleWallpapers.firstOrNull { it.id == destinationId }
            if (wallpaper != null) {
                WallpaperDetailScreen(
                    wallpaper = wallpaper,
                    isFavorite = wallpaper.id in favorites,
                    onBack = { selectedWallpaperId = null },
                    onFavorite = { favorites = toggleFavorite(favorites, wallpaper.id) }
                )
            } else {
                when (selectedTab) {
                    "Explore" -> ExploreScreen(
                        wallpapers = filteredWallpapers,
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        favorites = favorites,
                        onSearchChange = { searchQuery = it },
                        onCategorySelected = { selectedCategory = it },
                        onFavorite = { id -> favorites = toggleFavorite(favorites, id) },
                        onWallpaperSelected = { selectedWallpaperId = it }
                    )
                    "Favorites" -> FavoritesScreen(
                        wallpapers = favoriteWallpapers,
                        onExplore = { selectedTab = "Explore" },
                        onWallpaperSelected = { selectedWallpaperId = it },
                        onFavorite = { id -> favorites = toggleFavorite(favorites, id) }
                    )
                    "Settings" -> SettingsScreen(
                        reduceMotion = reduceMotion,
                        showArtworkHints = showArtworkHints,
                        onReduceMotionChange = { reduceMotion = it },
                        onArtworkHintsChange = { showArtworkHints = it }
                    )
                    else -> HomeScreen(
                        wallpapers = sampleWallpapers,
                        favorites = favorites,
                        onExplore = { selectedTab = "Explore" },
                        onWallpaperSelected = { selectedWallpaperId = it },
                        onFavorite = { id -> favorites = toggleFavorite(favorites, id) }
                    )
                }
            }
        }
    }
}

/** Pure state transformation kept separate so its behavior is easy to unit test. */
internal fun toggleFavorite(current: Set<String>, wallpaperId: String): Set<String> =
    if (wallpaperId in current) current - wallpaperId else current + wallpaperId

@Composable
private fun ScreenHeader(eyebrow: String, title: String, subtitle: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(eyebrow.uppercase(), color = Accent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.5.sp)
            Text(title, color = Color(0xFFFFF7FF), fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold)
            if (subtitle != null) Text(subtitle, color = Muted, fontSize = 12.sp, lineHeight = 18.sp)
        }
        Surface(shape = CircleShape, color = Color(0xFF251735), modifier = Modifier.size(46.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text("M", color = Color(0xFFFFA1D7), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun HomeScreen(
    wallpapers: List<Wallpaper>,
    favorites: Set<String>,
    onExplore: () -> Unit,
    onWallpaperSelected: (String) -> Unit,
    onFavorite: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { ScreenHeader("MotionWall", "Your screen.\nYour universe.", "Discover a little more life for your display.") }
        item { FeaturedMotionCard(onExplore) }
        item { SectionHeading("Made to move") }
        items(wallpapers.chunked(2)) { row ->
            WallpaperGridRow(row, favorites, onWallpaperSelected, onFavorite)
        }
        item {
            Text("V0.2 preview • Original gradient artwork", color = Color(0xFF8E7FA1), fontSize = 10.sp)
        }
    }
}

@Composable
private fun ExploreScreen(
    wallpapers: List<Wallpaper>,
    selectedCategory: String,
    searchQuery: String,
    favorites: Set<String>,
    onSearchChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onFavorite: (String) -> Unit,
    onWallpaperSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { ScreenHeader("Explore", "Find your next vibe.", "Search by wallpaper name, category, or mood.") }
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Search wallpapers or tags") },
                leadingIcon = { Text("⌕", fontSize = 22.sp, color = Muted) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Text("×", fontSize = 22.sp, color = Muted)
                        }
                    }
                }
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeading("Categories")
                CategoryChips(selectedCategory, onCategorySelected)
            }
        }
        item { SectionHeading(wallpapers.size.toString() + " wallpapers") }
        if (wallpapers.isEmpty()) {
            item {
                EmptyState(
                    title = "Nothing found",
                    message = "Try another search or reset your category filter.",
                    actionLabel = "Clear filters",
                    onAction = {
                        onSearchChange("")
                        onCategorySelected("All")
                    }
                )
            }
        } else {
            items(wallpapers.chunked(2)) { row ->
                WallpaperGridRow(row, favorites, onWallpaperSelected, onFavorite)
            }
        }
    }
}

@Composable
private fun FavoritesScreen(
    wallpapers: List<Wallpaper>,
    onExplore: () -> Unit,
    onWallpaperSelected: (String) -> Unit,
    onFavorite: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { ScreenHeader("Your collection", "Saved for later.", "Your favorites stay here while the app is open.") }
        if (wallpapers.isEmpty()) {
            item {
                EmptyState(
                    title = "Your collection is waiting",
                    message = "Tap the heart on any wallpaper to keep it close.",
                    actionLabel = "Explore wallpapers",
                    onAction = onExplore
                )
            }
        } else {
            val savedLabel = wallpapers.size.toString() + if (wallpapers.size == 1) " saved wallpaper" else " saved wallpapers"
            item { SectionHeading(savedLabel) }
            items(wallpapers.chunked(2)) { row ->
                WallpaperGridRow(row, wallpapers.map { it.id }.toSet(), onWallpaperSelected, onFavorite)
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    reduceMotion: Boolean,
    showArtworkHints: Boolean,
    onReduceMotionChange: (Boolean) -> Unit,
    onArtworkHintsChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { ScreenHeader("Personalize", "Make it yours.", "These preview preferences are stored for this session only.") }
        item {
            SettingsGroup(title = "Experience") {
                SettingsToggle(
                    title = "Reduce motion",
                    description = "A preview preference for calmer transitions. It does not change Android system animations yet.",
                    checked = reduceMotion,
                    onCheckedChange = onReduceMotionChange
                )
                SettingsToggle(
                    title = "Artwork hints",
                    description = "Show labels that identify generated V0.2 placeholder artwork.",
                    checked = showArtworkHints,
                    onCheckedChange = onArtworkHintsChange
                )
            }
        }
        item {
            SettingsGroup(title = "About MotionWall") {
                Text("A motion wallpaper playground, built step by step.", color = Color.White, fontSize = 14.sp)
                Text(
                    "Wallpaper preview and applying are not available yet. Video preview is planned for V0.4; Android live wallpaper support is planned for V0.5.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )
                Text("Version 0.2.0 • UI preview", color = Color(0xFF8E7FA1), fontSize = 11.sp)
            }
        }
        item {
            Text(
                "No account, network connection, or special device permission is required for this milestone.",
                color = Color(0xFF8E7FA1),
                fontSize = 11.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun WallpaperDetailScreen(
    wallpaper: Wallpaper,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavorite: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp).semantics { contentDescription = "Back" }) {
                    Text("←", color = Color.White, fontSize = 25.sp)
                }
                Text("WALLPAPER DETAILS", color = Accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
        }
        item { WallpaperArtwork(wallpaper, Modifier.fillMaxWidth().height(390.dp)) }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(wallpaper.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(wallpaper.category.uppercase(), color = Accent, fontSize = 10.sp, letterSpacing = 1.8.sp)
                }
                IconButton(
                    onClick = onFavorite,
                    modifier = Modifier.size(48.dp).semantics {
                        contentDescription = if (isFavorite) "Remove " + wallpaper.title + " from favorites" else "Add " + wallpaper.title + " to favorites"
                    }
                ) {
                    Text(if (isFavorite) "♥" else "♡", color = Accent, fontSize = 26.sp)
                }
            }
            Text(wallpaper.description, color = Muted, fontSize = 14.sp, lineHeight = 22.sp)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeading("Mood tags")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    wallpaper.tags.forEach { tag ->
                        Surface(color = Color(0xFF21182F), shape = RoundedCornerShape(50)) {
                            Text("#$tag", color = Color(0xFFE9DDF7), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF30243D))
            ) {
                Text("Live preview arrives in V0.4", color = Muted)
            }
        }
    }
}

@Composable
private fun WallpaperGridRow(
    wallpapers: List<Wallpaper>,
    favorites: Set<String>,
    onWallpaperSelected: (String) -> Unit,
    onFavorite: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        wallpapers.forEach { wallpaper ->
            WallpaperCard(
                wallpaper = wallpaper,
                isFavorite = wallpaper.id in favorites,
                onFavorite = { onFavorite(wallpaper.id) },
                onClick = { onWallpaperSelected(wallpaper.id) },
                modifier = Modifier.weight(1f)
            )
        }
        if (wallpapers.size == 1) Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun WallpaperCard(
    wallpaper: Wallpaper,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick).animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Panel)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                WallpaperArtwork(wallpaper, Modifier.fillMaxSize())
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    shape = CircleShape,
                    color = Color(0x990B0713)
                ) {
                    IconButton(
                        onClick = onFavorite,
                        modifier = Modifier.size(44.dp).semantics {
                            contentDescription = if (isFavorite) "Remove " + wallpaper.title + " from favorites" else "Add " + wallpaper.title + " to favorites"
                        }
                    ) {
                        Text(if (isFavorite) "♥" else "♡", color = Color.White, fontSize = 19.sp)
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(wallpaper.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(wallpaper.category.uppercase(), color = Accent, fontSize = 9.sp, letterSpacing = 1.sp)
                Text(wallpaper.description, color = Muted, fontSize = 10.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun WallpaperArtwork(wallpaper: Wallpaper, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(20.dp)).background(Brush.linearGradient(wallpaper.colors))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White.copy(alpha = .82f), wallpaper.colors.first().copy(alpha = .18f), Color.Transparent)
                ),
                radius = size.minDimension * .43f,
                center = Offset(size.width * .55f, size.height * .42f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(wallpaper.colors.first().copy(alpha = .72f), wallpaper.colors.last().copy(alpha = .08f), Color.Transparent)
                ),
                radius = size.minDimension * .34f,
                center = Offset(size.width * .82f, size.height * .83f)
            )
        }
    }
}

@Composable
private fun FeaturedMotionCard(onExplore: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(194.dp).clickable(onClick = onExplore),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF241338))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(brush = Brush.linearGradient(listOf(Color(0xFF35144E), Color(0xFF17102D), Color(0xFF101A36))))
                drawCircle(brush = Brush.radialGradient(listOf(Color(0xFFFFB8E6), Color(0xFFB45CFF), Color.Transparent)), radius = size.minDimension * .43f, center = Offset(size.width * .72f, size.height * .35f))
                drawCircle(brush = Brush.radialGradient(listOf(Color(0xFF8DEBFF), Color(0xFF4B65D8), Color.Transparent)), radius = size.minDimension * .32f, center = Offset(size.width * .87f, size.height * .82f))
            }
            Column(
                modifier = Modifier.align(Alignment.CenterStart).padding(20.dp).width(205.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("THE DAILY MOTION", color = Color(0xFFFFB4DF), fontSize = 9.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Text("A little more life on your screen.", color = Color.White, fontSize = 22.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
                Text("Tap to explore wallpapers  →", color = Color(0xFFE0D5EF), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun CategoryChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Categories.forEach { category ->
            val active = selectedCategory == category
            Surface(
                shape = RoundedCornerShape(50),
                color = if (active) Accent else Color(0xFF21182F),
                modifier = Modifier.clip(RoundedCornerShape(50)).clickable { onCategorySelected(category) }
            ) {
                Text(
                    category,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    color = if (active) Color(0xFF1B1026) else Color(0xFFE9DDF7),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String) {
    Text(title, color = Color(0xFFFFF7FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun EmptyState(title: String, message: String, actionLabel: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Panel).padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("✦", color = Accent, fontSize = 25.sp)
        Text(title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(message, color = Muted, fontSize = 12.sp, lineHeight = 19.sp)
        Button(onClick = onAction, shape = RoundedCornerShape(13.dp)) {
            Text(actionLabel)
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable Column.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Panel).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(title, color = Accent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        content()
    }
}

@Composable
private fun SettingsToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(description, color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
