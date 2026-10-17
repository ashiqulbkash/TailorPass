# TailorPass — Claude Code Guidelines (Kotlin Multiplatform)

TailorPass is an offline **Kotlin Multiplatform + Compose Multiplatform** app (Android + iOS). Users take clothing measurements from photos (on-device AI) or with a tape, then share them with a tailor as PDF or PNG. The whole UI is written once in Compose in `shared/commonMain`; there is no SwiftUI UI.

* **What the product must do:** `for-agent/PROJECT_OVERVIEW.md` (imported at the end of this section, so it is loaded into every session) — v1 features (11 areas), garments and their measurements, privacy promise, roadmap (v2/v3) and open decisions. **Read it at the start of every session that changes code or docs**, together with `plan.md`. It is the product-scope reference: nothing you build may break, drop or contradict a v1 feature or promise in it.
* **What to build:** `for-agent/plan.md` (steps 0–11, source-of-truth order, design rules) plus the design export in `for-agent/` (`html/`, `screens/`, `SCREENS.md`, `navigation.json`, `design-tokens.json`, `kmp-resources/`).
* **How to build it:** this file, plus — **from Step 10 on only** (after the design phase, Steps 0–9, is finished and tested with sample data) — `for-agent/KMP + CMP Technical guideline.en.md` (libraries, platform abstractions, Step 10 sub-steps 10.1–10.9, Step 11 iOS details). Do not use it during Steps 0–9.
* If any of these conflict, stop and ask. Do not resolve it silently. Roles: the Overview decides **which features exist** (scope); `plan.md` decides **order and what each step builds**; the HTML/PNGs decide **how it looks**; this file and the guideline decide **how it is coded**. A v1 feature in the Overview that no step covers is a gap — report it, do not drop it or build it unasked. Do not build v2/v3 roadmap features, but do not block them (e.g. keep `start`/`end`, garment lists data-driven).

Product overview (auto-imported):
@for-agent/PROJECT_OVERVIEW.md

## 1. Working rules

* Read the Overview, `plan.md` and the relevant `for-agent/` files before a planned step. Implement only what the current step asks for. Before starting, note which Overview features the step touches; when done, check none of them (or any other v1 feature or privacy promise) is broken or left out.
* **One step = one session** (in Step 10: one sub-step 10.x = one session). When the step is done, built, and compared with the PNGs, stop. Never start the next step on your own.
* Read existing code first; reuse existing components and patterns; keep changes small and scoped to the step. Do not reformat or refactor unrelated code.
* When something is ambiguous, check the code, `plan.md` and `for-agent/` first; ask only if you still cannot proceed.
* **Camera needs approval first.** Before writing any camera code (Kamera spike, `CameraPreview`, capture, frame analysis — Step 10.7), send the user a written description (approach, library + version, files and source sets, photo privacy/deletion, how the guideline's gate is tested, fallback) and wait for an explicit go-ahead. Ask again before switching to the `expect` fallback.
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
* Allowed additions beyond the chat format: an `Application` class in `androidApp` (when DI arrives), FileProvider `res/xml/file_paths.xml` + manifest entries, `androidApp/res/values-en/strings.xml`, `iosApp/iosApp/InfoPlist.xcstrings`, Swift bridge files for native SDKs and `iosApp/Podfile` for MediaPipe (Step 11), the test source sets, and the Step 10 sub-packages and files listed in the guideline §6 (`di/`, `data/`, `measure/`, `report/`, `platform/`, `composeResources/files/{audio,models}/`). Anything else needs approval.
* Root package `com.tech_hunt.tailorpass`; shared code uses sub-packages.
* Platform file suffixes: `<name>.android.kt` / `<name>.ios.kt` for `actual`s.
* Keep the existing design-system names from `kmp-resources` (`MaapTheme`, `MaapTheme.colors`, `MaapTheme.numbers`, `Dimens`). Do not rename them to "TailorPass".

### Gradle

* Versions live in `gradle/libs.versions.toml` (this project's convention; there is no `gradle.properties` version block). No inline versions in build files.
* Plugin aliases: `androidApplication`, `androidMultiplatformLibrary`, `composeMultiplatform`, `composeCompiler`, `kotlinMultiplatform`; new plugins use the same camelCase style (e.g. `kotlinSerialization`, `ksp`, `room`).
* `shared/build.gradle.kts`: Android KMP library plugin (`kotlin { android { … androidResources { enable = true } } }` — never the old top-level `android { }`); `iosArm64()` + `iosSimulatorArm64()` with `binaries.framework { baseName = "Shared"; isStatic = true }`. Swift imports it as `import Shared`. Do not rename it.
* Compose resources: `compose.resources { publicResClass = false; packageOfResClass = "com.tech_hunt.tailorpass.resources"; generateResClass = always }`.
* Add each dependency to the narrowest source set. Before adding one to `commonMain`, confirm it publishes Android, `iosArm64` and `iosSimulatorArm64` artifacts (e.g. on klibs.io).
* Latest stable versions only; ask before adding alpha/beta/RC versions or any library not named in `plan.md`, this file, or (from Step 10) the guideline §4.
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
* **Platform abstractions** — use the ones `plan.md` and the guideline §5 name:
  * `expect fun setAppLanguage(tag: String)` + `LocalAppLocale` (language switch, Step 0) — `LocalAppLocale` is a plain common `CompositionLocal`, not the CMP docs' `expect object`;
  * `CameraPreview(…)` — a common composable wrapping Kamera; it becomes `expect @Composable fun CameraPreview(…)` (CameraX / AVFoundation) only if Kamera fails the guideline's verification gate (Step 10.7);
  * `interface BodyMeasurer` (on-device AI: landmarks + body mask only; MediaPipe on both platforms);
  * `interface PdfRenderer`, `ImageEncoder`, `FileSharer`, `VoicePlayer`, `CameraPermission`;
  * `expect` functions for the database/DataStore file path.

  Backup file pick/save uses FileKit (common), so it needs no abstraction. `expect`/`actual` **functions** are fine. Do not create `expect`/`actual` **classes** (Beta); for a stateful platform service, use an interface in `commonMain` with platform implementations. Ask before adding an abstraction not listed here.
* **iOS is deferred.** Until Step 11, do not edit `iosMain`, `iosApp` or run iOS builds. Only exception: the template removal in Step 0, exactly as listed in `plan.md` Step 0. Each new `expect` therefore lacks an iOS `actual`: list it under Step 11 in `plan.md` in the same change.
* Kotlin APIs that Swift will call or implement (Step 11) stay simple: no `suspend`, `Flow`, generics, sealed classes, or default arguments. Top-level functions are `<FileName>Kt.<fn>` in Swift; `init…` becomes `do…`.
* iOS framework is linked with `embedAndSignAppleFrameworkForXcode`. Do not add the Kotlin CocoaPods plugin. If a native iOS SDK is needed, prefer Swift Package Manager (JetBrains no longer recommends CocoaPods for new projects). Exception: MediaPipe ships only as a CocoaPods pod, so `MediaPipeTasksVision` goes in `iosApp/Podfile` (open the `.xcworkspace`), used from Swift via the bridge in the guideline §5.

## 4. Product invariants (never break)

* **Offline:** no network code, no `INTERNET` permission in the merged Android manifest.
* **Private:** no analytics, telemetry, or logging services (Kermit for local-only debug logs is allowed from Step 10). Never log names, heights, or measurement values.
* **Photos are temporary:** memory or app cache only, deleted as soon as measurement finishes, is cancelled, or the screen is left. Never saved to the gallery.
* **No hard-coded UI strings.** Bengali (default) in `composeResources/values/strings.xml`, English in `values-en/strings.xml` — same keys.
* **Key rule:** model/logic code holds only *names* (string keys like `"m_chest"`, drawable names like `"ic_garment_panjabi"`), never display text. UI resolves them with `str(key)` / `drawable(name)` from `ui/util/Strings.kt`; fixed text uses `stringResource(Res.string.x)`.
* **Bengali renders correctly** (conjuncts ক্ষ, ন্ত, স্ত্র) on screen, in PDF and in PNG, using the bundled Hind Siliguri font — never the system font. Numbers use Bengali digits and ½ ("২৪½") in Bengali; `useBangla = false` in English.
* **Garment definitions** (measurement lists, order, plausible ranges `minInches`/`maxInches`, presets, size chart, `suggestSize()`) live in `model/Measurements.kt`, copied verbatim from `kmp-resources`. Do not duplicate them elsewhere or change their units without asking. Shirt/Pants/Pajama lists and the genji/women's charts are missing — ask the user for them (`plan.md` §3), never invent them. Storage unit (decided for Step 10 in the guideline): centimeters (`Double`); `Measurements.kt` stays in inches and values are converted at the boundary with `util/` helpers.

## 5. Architecture and state (MVI)

* Unidirectional flow: `UI → Action/Intent → ViewModel → immutable UiState → UI`.
* **Design steps (1–9):** each screen is a stateless `XxxScreen(state, onAction)` with sample data from the shared models plus `@Preview`s (light, and dark where the design has one). No ViewModels, repositories or DI yet.
* **Step 10:** one `androidx.lifecycle.ViewModel` (KMP) per flow in `commonMain`, exposing `StateFlow<UiState>`, collected with `collectAsStateWithLifecycle()`. One-off effects (navigation, share sheet, snackbar) go through a `Channel`/`SharedFlow`, not `UiState` flags.
* Composables hold no business logic and never touch repositories, databases, or platform services. Pass state and callbacks down, not ViewModels.
* Handle loading / success / empty / error explicitly; avoid catch-all `else` branches across states with different meanings.
* Measurement math, size suggestion, and report layout are pure Kotlin in `commonMain`. Platform code only supplies raw input (camera frames, landmarks) or draws output (PDF, share).
* Coroutines: `viewModelScope`; no `runBlocking`; CPU work on `Dispatchers.Default`, I/O on `Dispatchers.IO` (`import kotlinx.coroutines.IO` in common). Inject dispatchers where tests need control. Release native resources (camera, ML models) in `finally`/`close()`. Don't swallow exceptions.

### Data (Step 10)

* Details, entities and sub-step order: guideline §8 (10.2).
* Room KMP (bundled SQLite driver) for data, DataStore Preferences for settings (language, display unit, onboarding seen; theme follows the system — there is no theme setting) — both options in `plan.md`. Backup/restore is JSON via kotlinx.serialization (merge vs replace).
* Repositories own data access; map entities to domain/UI models; never expose entities or DataStore keys to the UI.
* Wrap multi-row saves and restore in transactions. Every schema change needs a migration and a test that old backups still restore.
* DI: Koin, introduced in Step 10. Common modules in `commonMain/di`; one platform module per platform; Android starts Koin from the `Application` class. `single` for repositories/platform services, `viewModel` for ViewModels. No heavy work at creation — load ML models lazily, off the main thread.

## 6. Compose Multiplatform UI

* **Match the design exactly:** HTML first (sizes, colors, radius, font size, text), then PNG. Frame 360×800 (CSS px = dp). Don't leave Material defaults; apply the HTML values via `MaapTheme`/`Dimens`.
* Reuse `ui/components/` (create a component in the first step that needs it, then reuse it). Don't use a raw Material component where a project component exists.
* **Reuse check before any new UI piece** (card, button, row, badge, tile, field, frame …): run `python3 for-agent/tools/ui_reuse_check.py --screen <id>` for the screen (or query the piece's CSS, e.g. `"height:56px" "border-radius:28px"`). If the piece appears on another screen, or twice on this one, build it as a component in `ui/components/` now (generic: colors/sizes as parameters, not screen text) and use it in this step; check `ui/components/` first for one to extend. Keep a piece private to its screen only when the checker finds it nowhere else. Never copy a component's styling (border/background/shape/selection) into a screen; repeated helpers (digits, semantics) go in `ui/util/` or the components package. List the checker results in the "before starting" report.
* Sheets/dialogs with an `overlay_on` base are overlays on that screen, not routes. Dark variants are the same composable under `MaapTheme(darkTheme = true)`.
* Navigation: multiplatform Navigation Compose with type-safe `@Serializable` routes, graph from `navigation.json`. Every non-root screen has a visible back action (iOS has no back button); use `BackHandler` where back must be intercepted.
* Safe areas: `WindowInsets.safeDrawing` / `MaapScaffold`. Use `start`/`end`, never `left`/`right`.
* Keep composables small and stateless; hoist state. No expensive work in composition; avoid needless `remember`/`LaunchedEffect`/`derivedStateOf`. Stable keys in lazy lists. `rememberSaveable` only for state that must survive recreation.

## 7. Testing

* Add or update tests when behavior changes. Use `kotlin.test` + `kotlinx-coroutines-test` with fakes, not mocking frameworks.
* Most tests go in `commonTest` (formatting, size suggestion, measurement math, backup/restore, ViewModels). Use `androidHostTest` for Android-only code and `iosTest` for iOS-only code.
* Cover edge cases: range limits, skipped measurements, ½-inch rounding, Bengali vs English digits.
* Never weaken or delete a test to make code pass. (The KMP template tests removed in Step 0 are replaced with real tests, not just deleted.)

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
* **Before starting** also names the Overview features (§3 areas / §4 garments) the step touches.
* **After verifying:** restate the plan; list the files changed, logic added, tests run or added with results, Android build result, the PNG-difference list (for UI steps), an **Overview check** (features touched still work as described; any Overview item found missing or in conflict), and iOS status.
