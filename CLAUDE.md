# TailorPass — Claude Code Guidelines (Kotlin Multiplatform)

TailorPass is an offline **Kotlin Multiplatform + Compose Multiplatform** app (Android + iOS). Users take clothing measurements from photos (on-device AI) or with a tape, then share them with a tailor as PDF or PNG. The whole UI is written once in Compose in `shared/commonMain`; there is no SwiftUI UI.

* **What to build:** `for-agent/plan.md` (steps 0–11, source-of-truth order, design rules; original Bengali version in `for-agent/plan.bn.md`) plus the design export in `for-agent/` (`html/`, `screens/`, `SCREENS.md`, `navigation.json`, `design-tokens.json`, `kmp-resources/`).
* **How to build it:** this file.
* If the two conflict, stop and ask. Do not resolve it silently.

## 1. Working rules

* Read `plan.md` and the relevant `for-agent/` files before a planned step. Implement only what the current step asks for.
* **One step = one session.** When the step is done, built, and compared with the PNGs, stop. Never start the next step on your own.
* Read existing code first; reuse existing components and patterns; keep changes small and scoped to the step. Do not reformat or refactor unrelated code.
* When something is ambiguous, check the code, `plan.md` and `for-agent/` first; ask only if you still cannot proceed.
* Show each command you run.
* Never run `git commit` — the user commits. If the project is a git repository, `git add` new files you create.

## 2. Project structure

The layout follows JetBrains' [`examples/chat`](https://github.com/JetBrains/compose-multiplatform/tree/master/examples/chat), Android + iOS only (none of its desktop/js/wasm/macOS parts).

```
settings.gradle.kts     include(":androidApp"), include(":shared") — nothing else
build.gradle.kts        only `alias(libs.plugins.X) apply false`
gradle/libs.versions.toml   ALL versions (Kotlin, AGP, CMP and libraries)
androidApp/             thin shell: MainActivity, manifest, launcher icon, res/values{,-en}/strings.xml (app_name)
shared/                 KMP library: all UI, logic, data, composeResources
  src/commonMain/kotlin/com/tech_hunt/tailorpass/   model/, util/, ui/{theme,components,diagram,util,nav,screens/<section>}/, App.kt (later data/)
  src/commonMain/composeResources/                  values/ (bn, default), values-en/, drawable/, font/
  src/commonTest/  src/androidHostTest/  src/iosTest/
  src/androidMain/  src/iosMain/                    platform `actual`s only
iosApp/                 Xcode shell (touched only in Step 11)
for-agent/              agent docs — not part of the app
```

* Never restructure, rename, merge, or split modules, or move files between modules. If a task seems to need it, stop and ask.
* Allowed additions beyond the chat format: an `Application` class in `androidApp` (when DI arrives), FileProvider `res/xml/file_paths.xml` + manifest entries, `androidApp/res/values-en/strings.xml`, `iosApp/iosApp/InfoPlist.xcstrings`, Swift bridge files for native SDKs (Step 11), and the test source sets. Anything else needs approval.
* Root package `com.tech_hunt.tailorpass`; shared code uses sub-packages.
* Platform file suffixes: `<name>.android.kt` / `<name>.ios.kt` for `actual`s.
* Keep the existing design-system names from `kmp-resources` (`MaapTheme`, `MaapTheme.colors`, `MaapTheme.numbers`, `Dimens`). Do not rename them to "TailorPass".

### Gradle

* Versions live in `gradle/libs.versions.toml` (this project's convention; there is no `gradle.properties` version block). No inline versions in build files.
* Plugin aliases: `androidApplication`, `androidMultiplatformLibrary`, `composeMultiplatform`, `composeCompiler`, `kotlinMultiplatform`; new plugins use the same camelCase style (e.g. `kotlinSerialization`, `ksp`, `room`).
* `shared/build.gradle.kts`: Android KMP library plugin (`kotlin { android { … androidResources { enable = true } } }` — never the old top-level `android { }`); `iosArm64()` + `iosSimulatorArm64()` with `binaries.framework { baseName = "Shared"; isStatic = true }`. Swift imports it as `import Shared`. Do not rename it.
* Compose resources: `compose.resources { publicResClass = false; packageOfResClass = "com.tech_hunt.tailorpass.resources"; generateResClass = always }`.
* Add each dependency to the narrowest source set. Before adding one to `commonMain`, confirm it publishes Android, `iosArm64` and `iosSimulatorArm64` artifacts (e.g. on klibs.io).
* Latest stable versions only; ask before adding alpha/beta/RC versions or any library not named in `plan.md` or this file.
* Never add Hilt/Dagger, networking, analytics, crash-reporting, or ads SDKs.

### Entry points

| Where | Content |
| --- | --- |
| `shared/commonMain/.../App.kt` | `@Composable fun App()` wrapping `MaapTheme { }` — the only UI root |
| `androidApp/.../MainActivity.kt` | `setContent { App() }`; no UI code in `androidApp` |
| `shared/iosMain/.../MainViewController.kt` (Step 11) | `fun MainViewController() = ComposeUIViewController { App() }` |
| `iosApp/iosApp/ContentView.swift` (Step 11) | `UIViewControllerRepresentable` returning `MainViewControllerKt.MainViewController()`, shown with `.ignoresSafeArea()` |

## 3. Kotlin Multiplatform rules

* Write code in `commonMain` by default. Use `androidMain`/`iosMain` only for platform APIs with no multiplatform alternative. Never duplicate logic across platforms.
* **IMPORTANT — `commonMain` stays platform-free:** no `android.*`, `java.*`, `javax.*`, `platform.*`, `R.*`, `LocalContext`, Android-only `androidx` artifacts, `java.time`, `String.format`, `System.currentTimeMillis()`, `synchronized`/`@Volatile`. Use `kotlin.time`/`kotlinx-datetime`, the `util/` format helpers (`toBanglaDigits()`, `formatInches()`), `Res.*`, coroutines/`Mutex`.
* **Platform abstractions** — use the ones `plan.md` names:
  * `expect fun setAppLanguage(tag: String)` + `LocalAppLocale` (language switch);
  * `expect @Composable fun CameraPreview(…)` (camera);
  * `interface BodyMeasurer` (on-device AI measurement);
  * PDF writing + share sheet, file pick/save (backup), database/settings file path.

  `expect`/`actual` **functions** are fine. Do not create `expect`/`actual` **classes** (Beta); for a stateful platform service, use an interface in `commonMain` with platform implementations. Ask before adding an abstraction not listed here.
* **iOS is deferred.** Until Step 11, do not edit `iosMain`, `iosApp` or run iOS builds. Each new `expect` therefore lacks an iOS `actual`: list it under Step 11 in `plan.md` in the same change.
* Kotlin APIs that Swift will call or implement (Step 11) stay simple: no `suspend`, `Flow`, generics, sealed classes, or default arguments. Top-level functions are `<FileName>Kt.<fn>` in Swift; `init…` becomes `do…`.
* iOS framework is linked with `embedAndSignAppleFrameworkForXcode`. Do not add the Kotlin CocoaPods plugin. If a native iOS SDK is needed, prefer Swift Package Manager (JetBrains no longer recommends CocoaPods for new projects).

## 4. Product invariants (never break)

* **Offline:** no network code, no `INTERNET` permission in the merged Android manifest.
* **Private:** no analytics, telemetry, or logging services. Never log names, heights, or measurement values.
* **Photos are temporary:** memory or app cache only, deleted as soon as measurement finishes, is cancelled, or the screen is left. Never saved to the gallery.
* **No hard-coded UI strings.** Bengali (default) in `composeResources/values/strings.xml`, English in `values-en/strings.xml` — same keys.
* **Key rule:** model/logic code holds only *names* (string keys like `"m_chest"`, drawable names like `"ic_garment_panjabi"`), never display text. UI resolves them with `str(key)` / `drawable(name)` from `ui/util/Strings.kt`; fixed text uses `stringResource(Res.string.x)`.
* **Bengali renders correctly** (conjuncts ক্ষ, ন্ত, স্ত্র) on screen, in PDF and in PNG, using the bundled Hind Siliguri font — never the system font. Numbers use Bengali digits and ½ ("২৪½") in Bengali; `useBangla = false` in English.
* **Garment definitions** (measurement lists, order, plausible ranges `minInches`/`maxInches`, presets, size chart, `suggestSize()`) live in `model/Measurements.kt`, copied verbatim from `kmp-resources`. Do not duplicate them elsewhere or change their units without asking. The storage unit is decided in Step 10.

## 5. Architecture and state (MVI)

* Unidirectional flow: `UI → Action/Intent → ViewModel → immutable UiState → UI`.
* **Design steps (1–9):** each screen is a stateless `XxxScreen(state, onAction)` with sample data from the shared models plus `@Preview`s (light, and dark where the design has one). No ViewModels, repositories or DI yet.
* **Step 10:** one `androidx.lifecycle.ViewModel` (KMP) per flow in `commonMain`, exposing `StateFlow<UiState>`, collected with `collectAsStateWithLifecycle()`. One-off effects (navigation, share sheet, snackbar) go through a `Channel`/`SharedFlow`, not `UiState` flags.
* Composables hold no business logic and never touch repositories, databases, or platform services. Pass state and callbacks down, not ViewModels.
* Handle loading / success / empty / error explicitly; avoid catch-all `else` branches across states with different meanings.
* Measurement math, size suggestion, and report layout are pure Kotlin in `commonMain`. Platform code only supplies raw input (camera frames, landmarks) or draws output (PDF, share).
* Coroutines: `viewModelScope`; no `runBlocking`; CPU work on `Dispatchers.Default`, I/O on `Dispatchers.IO` (`import kotlinx.coroutines.IO` in common). Inject dispatchers where tests need control. Release native resources (camera, ML models) in `finally`/`close()`. Don't swallow exceptions.

### Data (Step 10)

* Room KMP (bundled SQLite driver) for data, DataStore Preferences for settings (language, theme, onboarding seen) — both options in `plan.md`. Backup/restore is JSON via kotlinx.serialization (merge vs replace).
* Repositories own data access; map entities to domain/UI models; never expose entities or DataStore keys to the UI.
* Wrap multi-row saves and restore in transactions. Every schema change needs a migration and a test that old backups still restore.
* DI: Koin, introduced in Step 10. Common modules in `commonMain/di`; one platform module per platform; Android starts Koin from the `Application` class. `single` for repositories/platform services, `viewModel` for ViewModels. No heavy work at creation — load ML models lazily, off the main thread.

## 6. Compose Multiplatform UI

* **Match the design exactly:** HTML first (sizes, colors, radius, font size, text), then PNG. Frame 360×800 (CSS px = dp). Don't leave Material defaults; apply the HTML values via `MaapTheme`/`Dimens`.
* Reuse `ui/components/` (create a component in the first step that needs it, then reuse it). Don't use a raw Material component where a project component exists.
* Sheets/dialogs with an `overlay_on` base are overlays on that screen, not routes. Dark variants are the same composable under `MaapTheme(darkTheme = true)`.
* Navigation: multiplatform Navigation Compose with type-safe `@Serializable` routes, graph from `navigation.json`. Every non-root screen has a visible back action (iOS has no back button); use `BackHandler` where back must be intercepted.
* Safe areas: `WindowInsets.safeDrawing` / `MaapScaffold`. Use `start`/`end`, never `left`/`right`.
* Keep composables small and stateless; hoist state. No expensive work in composition; avoid needless `remember`/`LaunchedEffect`/`derivedStateOf`. Stable keys in lazy lists. `rememberSaveable` only for state that must survive recreation.

## 7. Testing

* Add or update tests when behavior changes. Use `kotlin.test` + `kotlinx-coroutines-test` with fakes, not mocking frameworks.
* Most tests go in `commonTest` (formatting, size suggestion, measurement math, backup/restore, ViewModels). Use `androidHostTest` for Android-only code and `iosTest` for iOS-only code.
* Cover edge cases: range limits, skipped measurements, ½-inch rounding, Bengali vs English digits.
* Never weaken or delete a test to make code pass.

## 8. Build and verification

| Purpose | Command |
| --- | --- |
| Shared tests on Linux (commonTest + androidHostTest on the JVM) | `./gradlew :shared:testAndroidHostTest` |
| All shared tests on every available target | `./gradlew :shared:allTests` |
| Android build (must pass at the end of every step) | `./gradlew :androidApp:assembleDebug` |
| Install on device/emulator | `./gradlew :androidApp:installDebug` |
| iOS compile / link / tests (macOS, Step 11) | `./gradlew :shared:compileKotlinIosSimulatorArm64` · `:shared:linkDebugFrameworkIosSimulatorArm64` · `:shared:iosSimulatorArm64Test` |
| Release check (R8, merged manifest) | `./gradlew :androidApp:assembleRelease` |

After each change:

1. Review the diff, including for platform APIs leaking into `commonMain`.
2. Run the tests covering the changed code. This is mandatory before calling the change done.
3. Run `:androidApp:assembleDebug`.
4. UI changes: run the app or render `@Preview`s, check Bengali and English, and list the differences from each screen's PNG.
5. PDF/PNG/font changes: generate the output and check Bengali conjuncts.
6. Report iOS as "not verified (deferred to Step 11)" unless on macOS in Step 11.

Before a release build: merged manifest has no `INTERNET`; R8 keeps the ML runtime and kotlinx-serialization classes; AI measurement works in the release build.

Never claim something works without verifying it. Say plainly what could not be verified and why.

## 9. Task reporting

Every task gets two short, factual messages. Never merge them.

* **Before starting:** the step/requirement, and the concrete actions with the source sets to be touched.
* **After verifying:** restate the plan; list the files changed, logic added, tests run or added with results, Android build result, the PNG-difference list (for UI steps), and iOS status.
