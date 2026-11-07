# Version Roadmap

Every milestone is a learning unit and must produce a testable result. A version is complete only when implementation, build, physical-device testing, documentation, and acceptance checks are complete.

## V0.1.0 — Android foundation

**Learn:** Kotlin basics, Android project structure, Gradle, activities, resources, debug builds, device installation.

**Deliver:** Minimal app opens and shows a MotionWall welcome screen.

**Exit checklist**
- [ ] Gradle sync succeeds.
- [ ] Debug build succeeds.
- [ ] App launches on a physical Android phone.
- [ ] No crash on cold start.
- [ ] APK artifact is available from GitHub Actions.
- [ ] Notes explain the manifest, Activity, Gradle files, and APK output.

## V0.2.0 — UI and design system

**Learn:** Jetpack Compose, composables, state, Material components, navigation, animation.

**Deliver:** Home, Explore, Favorites, and Settings screens with reusable components and a coherent design system.

**Exit checklist**
- [ ] Dark-first visual system and typography.
- [ ] Bottom navigation.
- [ ] Reusable wallpaper card and category chip.
- [ ] Responsive layouts and accessible touch targets.
- [ ] UI state survives ordinary recomposition.
- [ ] Physical-device UI test notes.

## V0.3.0 — Wallpaper library

**Learn:** Data classes, repository pattern, local JSON/assets, filtering, sorting, image loading.

**Deliver:** Catalog backed by local metadata and thumbnail assets.

**Exit checklist**
- [ ] Typed wallpaper model.
- [ ] Local catalog repository.
- [ ] Categories and filters.
- [ ] Search by title/category/tags.
- [ ] Empty and error states.
- [ ] Asset provenance/license register.

## V0.4.0 — In-app preview

**Learn:** Media3/ExoPlayer, media lifecycle, surfaces, playback state, resource cleanup.

**Deliver:** Full-screen wallpaper preview with controlled looping playback.

**Exit checklist**
- [ ] Supported video plays.
- [ ] Looping works without an obvious gap for prepared assets.
- [ ] Playback pauses when screen/route is not active.
- [ ] Unsupported media displays a useful error.
- [ ] Decoder and surface resources are released.

## V0.5.0 — Native video live wallpaper

**Learn:** WallpaperService, Engine lifecycle, Android manifest service declaration, native wallpaper chooser, Media3 integration in a service.

**Deliver:** User can preview and apply a bundled video through Android's native wallpaper UI.

**Exit checklist**
- [ ] WallpaperService declared correctly.
- [ ] Native system preview opens.
- [ ] User confirms before wallpaper changes.
- [ ] Video loops on home screen.
- [ ] Hidden/visibility lifecycle is respected.
- [ ] Home/lock behavior recorded for test devices.
- [ ] Battery and memory baseline captured.

## V0.6.0 — Interactive wallpaper engine

**Learn:** Canvas/OpenGL ES rendering, touch events, frame pacing, surface lifecycle, rendering optimization.

**Deliver:** At least three interactive effects, such as ambient particles, flowing gradient, and touch ripple.

**Exit checklist**
- [ ] Three effects selectable in app.
- [ ] Touch effect responds correctly.
- [ ] Rendering stops when surface is destroyed or hidden.
- [ ] Frame-rate cap works.
- [ ] No unbounded allocations in render loop.
- [ ] Battery/thermal behavior tested.

## V0.7.0 — Favorites, downloads, offline

**Learn:** Room, DataStore, WorkManager, scoped storage, download progress and cancellation.

**Deliver:** Persistent favorites, download library, and offline playback.

**Exit checklist**
- [ ] Favorites persist across restarts.
- [ ] Download progress and cancellation work.
- [ ] Storage errors handled.
- [ ] Offline assets remain discoverable.
- [ ] Delete downloaded content safely.

## V0.8.0 — Optimization and compatibility

**Learn:** Profiling, Android lifecycle testing, memory analysis, compatibility matrices, instrumentation tests.

**Deliver:** Stable experience across a representative device matrix.

**Exit checklist**
- [ ] Test on at least two distinct Android devices where available.
- [ ] Test multiple screen sizes/aspect ratios.
- [ ] No critical crash or ANR in core flows.
- [ ] Decoder/renderer cleanup verified.
- [ ] Battery-saving mode works.
- [ ] Known limitations documented.

## V1.0.0 — First complete release

**Learn:** Integration, regression testing, release builds, versioning, signing and release notes.

**Deliver:** Complete MotionWall MVP and downloadable release APK.

**Exit checklist**
- [ ] All V1 functional requirements pass.
- [ ] At least 20 licensed wallpaper assets.
- [ ] Three interactive effects.
- [ ] Regression suite passes.
- [ ] Release APK built and tested.
- [ ] Asset/license register complete.
- [ ] Changelog and installation instructions published.
