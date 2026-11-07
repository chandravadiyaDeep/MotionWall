package com.motionwall.app

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/** Critical navigation smoke test; run with connectedDebugAndroidTest on an emulator/device. */
class MotionWallNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun exploreCanOpenWallpaperDetailsAndNavigateBack() {
        composeRule.onNodeWithText("Explore").performClick()
        composeRule.onNodeWithText("Find your next vibe.").assertExists()

        composeRule.onNodeWithText("Nebula Bloom").performClick()
        composeRule.onNodeWithText("WALLPAPER DETAILS").assertExists()

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.onNodeWithText("Find your next vibe.").assertExists()
    }

    @Test
    fun favoritesEmptyStateCanNavigateToExplore() {
        composeRule.onNodeWithText("Favorites").performClick()
        composeRule.onNodeWithText("Your collection is waiting").assertExists()
        composeRule.onNodeWithText("Explore wallpapers").performClick()
        composeRule.onNodeWithText("Find your next vibe.").assertExists()
    }
}
