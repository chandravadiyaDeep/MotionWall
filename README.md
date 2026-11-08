# MotionWall

MotionWall is a native Android live-wallpaper app focused on aesthetic, dark/AMOLED, and interactive motion wallpapers.

> Development approach: build a testable APK at each milestone, verify it on a physical Android device, and document what we learn.

## Repository layout

- `android/` — native Android application (Kotlin + Jetpack Compose).
- `website/` — static project website deployed with GitHub Pages.
- `docs/` — product requirements, architecture, learning notes, and test plans.
- `.github/workflows/` — automated Android build and Pages deployment.

## Version roadmap

| Version | Milestone | Main learning |
|---|---|---|
| 0.1.0 | Android foundation | Kotlin, Gradle, Android app lifecycle, APK install |
| 0.2.0 | UI and design system | Jetpack Compose, navigation, state, animation |
| 0.3.0 | Wallpaper library | Local JSON catalog, repository, bundled artwork, filters |
| 0.4.0 | Preview system | Media playback, lifecycle, video controls |
| 0.5.0 | Video live wallpaper | WallpaperService, Media3, Android system integration |
| 0.6.0 | Interactive engine | Canvas/OpenGL rendering, touch input, frame pacing |
| 0.7.0 | User library | Favorites, Room, DataStore, downloads, offline |
| 0.8.0 | Optimization | Battery, memory, compatibility, reliability |
| 1.0.0 | First complete release | Integration, regression tests, release APK |

## Current milestone: V0.3.0 — Wallpaper library

- [x] Typed wallpaper model with local thumbnail and provenance metadata.
- [x] Local JSON catalog repository with schema and data validation.
- [x] Categories derived from catalog data and combined category/search filtering.
- [x] Search by title, category, and tags.
- [x] Empty results and retryable catalog error states.
- [x] Original bundled thumbnail assets and asset provenance register.
- [ ] CI build and unit tests pass for this branch.
- [ ] Physical-device catalog and artwork checks recorded.

## Build locally

Open the `android/` directory in Android Studio and let Gradle sync. Then run the `app` configuration on an emulator or connected device.

To build a debug APK from a machine with the Android SDK installed:

```powershell
cd android
.\gradlew.bat assembleDebug testDebugUnitTest
```

APK output: `android/app/build/outputs/apk/debug/app-debug.apk`. Development APKs are published as GitHub prereleases after a successful push to `main`.

## Project website

The static project website is in `website/`. It is deployed by GitHub Actions to GitHub Pages. APK builds are produced by GitHub Actions and published as prerelease assets for direct phone download.

## Important product constraints

- Live wallpaper application is mediated by Android's native wallpaper UI; MotionWall never silently changes the user's wallpaper.
- Lock-screen live wallpaper availability varies by Android version and manufacturer.
- Wallpaper assets must be original, public-domain, or appropriately licensed. Record asset provenance and license before distribution.
- Keep large videos out of the APK. Bundle a small demo set and add downloadable catalogs in a later milestone.
- No network permission or remote catalog is introduced in V0.3.
- Never commit signing keys, API tokens, or other secrets.

## License

The source-code license and final artwork distribution license will be selected before public distribution.
