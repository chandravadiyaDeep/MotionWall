# Changelog

All notable project changes are recorded here.

## 0.3.0 — In progress

- Replaced the in-memory sample list with a versioned local JSON catalog.
- Added a typed repository with required-field, duplicate-ID, artwork, and color validation.
- Added eight original bundled XML thumbnail compositions with a provenance/license register.
- Connected Home, Explore, Favorites, and Detail to repository-backed catalog data.
- Categories are derived from the catalog; search and category filters remain combined.
- Added a visible retry state for catalog loading/validation failures.
- Bumped Android version metadata and CI prerelease naming to 0.3.0.

## 0.2.0 — Completed

- Responsive Compose app shell with Home, Explore, Favorites, and Settings navigation.
- Gradient-based wallpaper cards, category filters, search, and local favorites.
- Added detail navigation, favorite controls, system/visible back, and empty-state recovery.
- Added reduced-motion and artwork-hint preview preferences.
- Added unit and Compose UI smoke tests.

## 0.1.0 — Foundation

- Initial Kotlin + Jetpack Compose Android app.
- Cloud build, unit tests, and direct GitHub Release APK distribution.
- Permanent responsive GitHub Pages project landing page.
