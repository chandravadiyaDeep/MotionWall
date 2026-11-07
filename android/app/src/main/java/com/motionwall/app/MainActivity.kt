package com.motionwall.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * V0.1 entry point.
 *
 * Keep this Activity deliberately small: later milestones will move screens,
 * theme tokens, and reusable components into their own packages.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MotionWallTheme {
                FoundationScreen()
            }
        }
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

@Composable
private fun FoundationScreen() {
    val background = Brush.linearGradient(
        colors = listOf(
            Color(0xFF25113C),
            Color(0xFF100B1C),
            Color(0xFF090710)
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "MOTIONWALL",
                color = Color(0xFFFF8CCF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Your screen.\nYour universe\nin motion.",
                color = Color(0xFFFFF7FF),
                fontSize =  fortyEightSp,
                lineHeight =  fiftySixSp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "A new wallpaper experience is taking shape.",
                color = Color(0xFFC2B5D2),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFF2B1C3F)
            ) {
                Text(
                    text = "VERSION 0.1.0  •  FOUNDATION",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = Color(0xFFE9DDF7),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Learn it. Build it. Test it.",
                color = Color(0xFF9D8EAF),
                fontSize = 13.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

private val fortyEightSp = 48.sp
private val fiftySixSp = 56.sp
