package com.motionwall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Main Android entry point. V0.2 introduces the first responsive Compose workspace. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MotionWallTheme { MotionWallApp() } }
    }
}

@Composable
private fun MotionWallTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFF78C8),
            secondary = Color(0xFFAA91FF),
            background = Color(0xFF0B0713),
            surface = Color(0xFF171021)
        ),
        content = content
    )
}

private data class WallpaperPreview(
    val title: String,
    val category: String,
    val description: String,
    val colors: List<Color>
)

// Original gradient placeholders only; licensed wallpaper artwork arrives in V0.3.
private val sampleWallpapers = listOf(
    WallpaperPreview("Nebula Bloom", "Aesthetic", "Soft cosmic colour flow", listOf(Color(0xFFFF8BCB), Color(0xFF7048C8), Color(0xFF17112E))),
    WallpaperPreview("Cyber Pulse", "Gen Z", "Electric blue energy", listOf(Color(0xFF66E5FF), Color(0xFF4C58D8), Color(0xFF10152F))),
    WallpaperPreview("Velvet Orbit", "Dark", "Deep violet ambient motion", listOf(Color(0xFFBD8CFF), Color(0xFF542D91), Color(0xFF090713))),
    WallpaperPreview("Solar Drift", "Aesthetic", "Warm sunset atmosphere", listOf(Color(0xFFFFB36B), Color(0xFFE45B9A), Color(0xFF28112E)))
)

@Composable
private fun MotionWallApp() {
    var selectedTab by remember { mutableStateOf("Home") }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var favorites by remember { mutableStateOf(setOf<String>()) }
    val categories = listOf("All", "Aesthetic", "Gen Z", "Anime", "Dark", "Interactive")
    val visibleWallpapers = sampleWallpapers.filter { wallpaper ->
        val matchesCategory = selectedCategory == "All" || wallpaper.category == selectedCategory
        val matchesSearch = wallpaper.title.contains(searchQuery, ignoreCase = true) ||
            wallpaper.category.contains(searchQuery, ignoreCase = true)
        val matchesTab = selectedTab != "Favorites" || favorites.contains(wallpaper.title)
        matchesCategory && matchesSearch && matchesTab
    }

    Scaffold(
        containerColor = Color(0xFF0B0713),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF151020), contentColor = Color.White) {
                listOf("Home", "Explore", "Favorites", "Settings").forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Text(when (tab) { "Home" -> "⌂"; "Explore" -> "◈"; "Favorites" -> "♡"; else -> "⚙" }, fontSize = 20.sp) },
                        label = { Text(tab, fontSize = 10.sp) }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("MOTIONWALL", color = Color(0xFFFF8CCF), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
                        Text(
                            when (selectedTab) {
                                "Explore" -> "Find your next vibe."
                                "Favorites" -> "Your saved universe."
                                "Settings" -> "Make it yours."
                                else -> "Your screen. Your universe."
                            },
                            color = Color(0xFFFFF7FF), fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(shape = CircleShape, color = Color(0xFF251735), modifier = Modifier.size(46.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("M", color = Color(0xFFFFA1D7), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
            }
            if (selectedTab == "Settings") {
                item { SettingsContent() }
            } else {
                item { FeaturedMotionCard() }
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        placeholder = { Text("Search wallpapers") },
                        leadingIcon = { Text("⌕", fontSize = 22.sp) }
                    )
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionHeading("Explore categories")
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            categories.forEach { category ->
                                val active = selectedCategory == category
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (active) Color(0xFFFF82CA) else Color(0xFF21182F),
                                    modifier = Modifier.clickable { selectedCategory = category }
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
                }
                item { SectionHeading(if (selectedTab == "Favorites") "Your collection" else "Made to move") }
                if (visibleWallpapers.isEmpty()) {
                    item {
                        Text(
                            if (selectedTab == "Favorites") "No favorites yet. Tap the heart on a wallpaper to save it." else "No wallpapers match this search yet.",
                            color = Color(0xFFB7A9C8), modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(visibleWallpapers.chunked(2)) { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowItems.forEach { wallpaper ->
                                WallpaperCard(
                                    wallpaper = wallpaper,
                                    isFavorite = favorites.contains(wallpaper.title),
                                    onFavorite = {
                                        favorites = if (favorites.contains(wallpaper.title)) favorites - wallpaper.title
                                        else favorites + wallpaper.title
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedMotionCard() {
    Card(
        modifier = Modifier.fillMaxWidth().height(190.dp),
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
                modifier = Modifier.align(Alignment.CenterStart).padding(20.dp).width(190.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("THE DAILY MOTION", color = Color(0xFFFFB4DF), fontSize = 9.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Text("A little more life on your screen.", color = Color.White, fontSize = 22.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
                Text("Discover motion made for your mood.", color = Color(0xFFE0D5EF), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String) {
    Text(title, color = Color(0xFFFFF7FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun WallpaperCard(
    wallpaper: WallpaperPreview,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171021))
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth().height(145.dp)
                    .background(Brush.linearGradient(wallpaper.colors))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(listOf(Color.White.copy(alpha = .8f), wallpaper.colors.first().copy(alpha = .15f), Color.Transparent)),
                        radius = size.minDimension * .42f,
                        center = Offset(size.width * .55f, size.height * .42f)
                    )
                    drawCircle(
                        color = wallpaper.colors.last().copy(alpha = .55f),
                        radius = size.minDimension * .24f,
                        center = Offset(size.width * .82f, size.height * .83f)
                    )
                }
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(9.dp).clickable { onFavorite() },
                    shape = CircleShape,
                    color = Color(0x660B0713)
                ) {
                    Text(if (isFavorite) "♥" else "♡", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = Color.White, fontSize = 17.sp)
                }
            }
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(wallpaper.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(wallpaper.category.uppercase(), color = Color(0xFFFF8CCF), fontSize = 9.sp, letterSpacing = 1.sp)
                Text(wallpaper.description, color = Color(0xFFB7A9C8), fontSize = 10.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun SettingsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Appearance", color = Color(0xFFFF8CCF), fontWeight = FontWeight.Bold)
        Text("Dark theme is active for the V0.2 preview.", color = Color(0xFFB7A9C8), fontSize = 13.sp)
        Text("About MotionWall", color = Color(0xFFFF8CCF), fontWeight = FontWeight.Bold)
        Text("A motion wallpaper playground, built step by step. Wallpaper applying and device permissions arrive in later milestones.", color = Color(0xFFB7A9C8), fontSize = 13.sp, lineHeight = 20.sp)
        Text("Version 0.2.0 • UI preview", color = Color(0xFF8E7FA1), fontSize = 11.sp)
    }
}
