# MotionWall Architecture

## Architectural principles

- Native Android first: Kotlin and Jetpack Compose.
- Modular boundaries between UI, catalog/data, media preview, wallpaper rendering, and persistence.
- Local-first MVP; no backend required for the first milestones.
- Android owns wallpaper installation/confirmation.
- Separate render strategies for video and interactive wallpapers.
- Lifecycle-aware playback and rendering.

## Planned modules / packages

```text
com.motionwall
├── MainActivity.kt
├── ui/
│   ├── screens/
│   ├── components/
│   └── theme/
├── catalog/
│   ├── model/
│   ├── data/
│   └── repository/
├── preview/
├── wallpaper/
│   ├── service/
│   ├── video/
│   └── interactive/
├── library/
│   ├── favorites/
│   └── downloads/
├── preferences/
└── core/
```

## Planned technology

| Concern | Technology | Introduced |
|---|---|---|
| Language | Kotlin | V0.1 |
| UI | Jetpack Compose | V0.2 |
| Navigation | AndroidX Navigation Compose | V0.2 |
| Video | AndroidX Media3 | V0.4 |
| Native live wallpaper | Android WallpaperService | V0.5 |
| Interactive rendering | Android Canvas initially; OpenGL ES if justified | V0.6 |
| Local database | Room | V0.7 |
| Preferences | DataStore | V0.7 |
| Downloads | WorkManager | V0.7 |
| Build automation | GitHub Actions | V0.1 |

## Wallpaper execution model

The Android wallpaper service owns the wallpaper Engine lifecycle. Rendering implementation is selected by wallpaper type:

- Video: a media playback component owns decoder/surface resources.
- Interactive: a renderer owns its rendering surface and frame loop.

The engine must react to visibility and surface lifecycle callbacks, release resources on destruction, and avoid keeping a continuous render loop active when invisible.

## Data model concept

A wallpaper record should include stable ID, title, category, tags, wallpaper type, thumbnail reference, asset reference, duration/loop settings for video, and license/provenance metadata. Interactive wallpapers should reference a validated effect configuration rather than executable remote code.

## GitHub and website architecture

- `android/`: Android source project.
- `website/`: static documentation/landing page published with GitHub Pages.
- `.github/workflows/android-build.yml`: debug APK build and artifact upload.
- `.github/workflows/pages.yml`: deploy website from `website/`.
- GitHub Releases: milestone APKs and release notes.

GitHub Pages hosts static files; it does not compile Android apps. GitHub Actions performs the build and stores the APK as an artifact/release asset.
