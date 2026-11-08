# MotionWall Testing Guide

## Every milestone

1. Build from a clean checkout.
2. Install on a physical Android device when the milestone supports it.
3. Test the happy path and at least one failure/cancellation path.
4. Record device model, Android version, app version, steps, and result.
5. Check Logcat for exceptions and resource leaks.
6. Update the milestone checklist and known limitations.

## V0.1 test cases

- [ ] Gradle sync succeeds.
- [ ] Debug APK builds.
- [ ] APK installs.
- [ ] App launches from launcher.
- [ ] Welcome screen renders.
- [ ] App survives background/foreground.
- [ ] App exits and reopens without crash.

## Wallpaper tests planned for later milestones

- [ ] User cancellation leaves existing wallpaper unchanged.
- [ ] Home wallpaper application works on test device.
- [ ] Lock-screen option is available only where device supports it.
- [ ] Video loops and stops when not visible.
- [ ] Interactive rendering stops when surface is destroyed.
- [ ] Download cancellation and low-storage paths behave safely.
- [ ] Offline playback works.

## Test report template

- Version:
- Commit:
- Device:
- Android version:
- Build command:
- Test case:
- Expected:
- Actual:
- Result: PASS / FAIL / BLOCKED
- Logs / screenshots:
- Follow-up issue:


## V0.2 UI verification commands

Run from the `android/` directory:

```bash
gradle --no-daemon assembleDebug testDebugUnitTest
```

For connected emulator/device UI smoke tests:

```bash
gradle --no-daemon connectedDebugAndroidTest
```

The repository's current GitHub Actions workflow runs the debug APK build and local JVM unit tests. It does not currently run connected-device instrumentation tests. Run those separately on an available emulator or physical Android device.

V0.2 manual checks:
- [ ] Launch from a cold start and verify Home renders.
- [ ] Switch between Home, Explore, Favorites, and Settings.
- [ ] Search by title, category, and tag; clear search.
- [ ] Select each category and reset to All.
- [ ] Add/remove favorites from a card and from detail.
- [ ] Open a wallpaper detail and return using both visible Back and Android system Back.
- [ ] Verify empty Favorites and no-results recovery actions.
- [ ] Toggle Reduce motion and Artwork hints.
- [ ] Check narrow phone and wider/tablet layout.
- [ ] Confirm no unexpected permission prompts or network dependency.


## V0.3 local wallpaper library checks

Automated JVM tests cover catalog schema parsing, unique IDs, unknown artwork keys, empty catalogs, color parsing, search, category filtering, and favorite state transformations.

Manual checks on a physical Android device:
- [ ] Launch in airplane mode; Home still shows the bundled wallpaper catalog.
- [ ] Confirm all eight catalog entries render their matching local thumbnail artwork.
- [ ] Search by a wallpaper title, category, and tag.
- [ ] Combine a category filter with a search query.
- [ ] Clear filters and confirm all entries return.
- [ ] Open a wallpaper detail and verify title, category, tags, source, and license metadata.
- [ ] Confirm no unexpected permission prompt or network request.
- [ ] Check that the app still opens after force-stop and cold start.
- [ ] Record device model, Android version, app version, and screenshots in the test report.

Catalog recovery check:
- Temporarily rename or invalidate the local catalog only in a local development build, launch the app, and verify the error message and Retry action. Restore the valid catalog before committing.
