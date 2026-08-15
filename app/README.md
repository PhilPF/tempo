# TEMP0 (Android)

A native Android implementation of the `REGIMEN.dc.html` / "TEMP0" design from the
Claude Design handoff bundle in this repo (`README.md`, `chats/`, `project/`) — a
minimal, dark-themed workout-tracking app.

## Stack

- Kotlin + Jetpack Compose, Material3 as a thin base (the app's actual visual language is
  bespoke — see `ui/theme/`)
- Navigation-Compose for screen-to-screen navigation (real back stack — system back
  button/predictive-back gesture work without any custom handling)
- Room (routines, session history) + DataStore Preferences (units, rest-timer duration,
  notifications, active routine) for local persistence
- No DI framework — a small hand-written `AppContainer` (`di/AppContainer.kt`)

## Module layout

- `:core` — pure Kotlin/JVM module: the exercise catalog, muscle/mannequin geometry, and
  state-reducer logic (session progress, builder reordering, stat derivation). No Android
  dependency, so it builds and unit-tests anywhere a JVM + Kotlin toolchain exists.
- `:app` — the Android application: Compose UI, Room/DataStore, ViewModel, navigation.

## Building

Open the repo root in Android Studio (Iguana/2023.2+ recommended) and let it sync — it's
a standard Gradle project (AGP 8.5.2, Kotlin 2.0.21, compileSdk/targetSdk 34, minSdk 26).

From the command line: `./gradlew assembleDebug` (needs the Android SDK — Studio will
prompt to install missing platforms/build-tools on first sync).

**Note on this session's own verification:** the sandbox this was built in has a network
policy that blocks `dl.google.com`, which is where Gradle's `google()` repository actually
resolves AGP and every AndroidX artifact from (`maven.google.com` 301-redirects there). That
made it impossible to run *any* Gradle task touching the `:app` module in this session —
not even a bare `./gradlew tasks` gets past plugin resolution. `:core` has no such
dependency (only Maven Central + the Gradle Plugin Portal, both reachable), so it was
built and unit-tested for real in this session (`./gradlew :core:test` — 23 tests,
all green) via a throwaway single-module copy that excludes the AGP plugin block.
`:app`'s Compose/Room/Navigation code was written carefully against the real APIs and was
read back through in full for structural correctness, but it has not been compiled — that
first real build needs to happen in Android Studio (or any environment with normal access
to Google's Maven repo), and any error found there should be reported back.

## Fonts

`res/font/` bundles static `.ttf` files for Libre Caslon Text and JetBrains Mono
(fetched from Google Fonts' own CDN under their OFL license — see
`assets/licenses/`), used directly rather than through the Downloadable Fonts API, so
first render never depends on Play Services being present. `ui/theme/GoogleFontsFallback.kt`
documents the alternative downloadable-fonts wiring if that's ever preferred.

## First-run data

On first launch the app seeds the 3 sample routines from the design (Push/Pull/Leg Day)
and a few demo session-history entries (dated relative to install time, not hardcoded)
so Progress isn't empty on a fresh install. Real workouts you complete are recorded
alongside/after that seed data — nothing is reset once you start using the app for real.
