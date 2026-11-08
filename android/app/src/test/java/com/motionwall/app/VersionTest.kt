package com.motionwall.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** V0.3 tests the version metadata generated for the debug build variant. */
class VersionTest {
    @Test
    fun debugVersionNameIncludesDevelopmentSuffix() {
        // The debug build type intentionally appends "-debug" to the base version.
        assertEquals("0.3.0-debug", BuildConfig.VERSION_NAME)
    }

    @Test
    fun versionCodeIsThreeForV030() {
        assertEquals(3, BuildConfig.VERSION_CODE)
    }

    @Test
    fun applicationIdUsesMotionWallNamespace() {
        // Debug builds append ".debug" to the base application ID.
        assertTrue(BuildConfig.APPLICATION_ID.startsWith("com.motionwall.app"))
    }
}
