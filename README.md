# MotionWall

MotionWall is a native Android live-wallpaper app focused on anime, Gen Z, aesthetic, dark/AMOLED, and interactive motion wallpapers.

> Development approach: learn in small versions, build a working APK at each milestone, test on a physical Android device, and document what we learn.

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
| 0.3.0 | Wallpaper library | Data models, local assets, search and filters |
| 0.4.0 | Preview system | Media playback, lifecycle, video controls |
| 0.5.0 | Video live wallpaper | WallpaperService, Media3, Android system integration |
| 0.6.0 | Interactive engine | Canvas/OpenGL rendering, touch input, frame pacing |
| 0.7.0 | User library | Favorites, Room, DataStore, downloads, offline |
| 0.8.0 | Optimization | Battery, memory, compatibility, reliability |
| 1.0.0 | First complete release | Integration, regression tests, release APK |

## Current milestone: V0.1.0

V0.1 is intentionally small. Its acceptance criteria are:

- [ ] Android project builds with the Gradle wrapper.
- [ ] App launches on a physical Android device.
- [ ] App shows a simple MotionWall foundation screen.
- [ ] Debug APK can be downloaded and installed.
- [ ] GitHub Actions build succeeds.
- [ ] Learning and test notes are documented.

## Build locally

Open the `android/` directory in Android Studio and let Gradle sync. Then run the `app` configuration on an emulator or connected device.

To build a debug APK from a machine with the Android SDK installed:

```bash
cd android
./gradlew assembleDebug
```

On Windows PowerShell:

```powershell
cd android
.\gradlew.bat assembleDebug
```

APK output: `android/app/build/outputs/apk/debug/app-debug.apk`.

## Project website

The static project website is in `website/`. It is deployed by GitHub Actions to GitHub Pages. APK builds are produced separately by the Android workflow and published as workflow artifacts; tagged releases can later attach signed release APKs.

## Important product constraints

- Live wallpaper application is mediated by Android's native wallpaper UI; MotionWall never silently changes the user's wallpaper.
- Lock-screen live wallpaper availability varies by Android version and manufacturer.
- Wallpaper assets must be original, public-domain, or appropriately licensed. Record asset provenance and license before distribution.
- Keep large videos out of the APK. Bundle a small demo set and add downloadable catalogs in a later milestone.
- Never commit signing keys, API tokens, or other secrets.

## License

The source-code license will be selected before public distribution. Third-party wallpaper assets may have separate licenses.
