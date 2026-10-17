# KMP + CMP Technical Guideline

Oct 17, 2026 · @Md Ashiqul Islam

> **When this applies:** only **after the design phase is finished and tested with sample data** — i.e. after [`plan.md`](plan.md) Steps 0–9 are done (all 52 designs built in `shared/commonMain` as stateless screens with sample data, previews matched with the PNGs, `:androidApp:assembleDebug` passing). This guideline is the technical companion to **`plan.md` Step 10 (data, logic, Android platform parts) and Step 11 (iOS)**. Do not use it during Steps 0–9.
>
> **Precedence:** `plan.md` and `CLAUDE.md` win. This guideline adds detail where they leave things open. If you find a conflict, stop and ask. Product scope (which features, garments and privacy promises v1 must keep) comes from [`PROJECT_OVERVIEW.md`](PROJECT_OVERVIEW.md) — read it at the start of every sub-step, together with `plan.md`.

The app is built with Kotlin Multiplatform + Compose Multiplatform. About 80–85% of the code lives in `commonMain`, and platform work sits behind a small, fixed set of abstractions (Section 5). The first release is Android only (Step 10), but the code stays ready for iOS (Step 11).

## 1. Core rules for the agent

1. **Common first, platform later.** Write new code in `commonMain` first. Only the implementations of the abstractions in Section 5 go into platform code. If platform code is needed beyond that, ask first.
2. **Nothing from Android or iOS is imported in `commonMain`.** `android.*`, `java.*`, `R`, `LocalContext`, Android-only `androidx` artifacts (KMP-supported artifacts are fine), `platform.*` (e.g. `platform.UIKit`) etc. are forbidden in `commonMain` (full list in `CLAUDE.md` §3).
3. **Libraries only from the list in Section 4.** Ask before adding any other library. It must be KMP-supported (Android, `iosArm64`, `iosSimulatorArm64`) and must not use the internet.
4. **No hard-coded versions.** All versions go in `gradle/libs.versions.toml`, using the latest stable version compatible with the project's Kotlin/CMP versions. Not alpha, beta or RC.
5. **No internet.** Do not add any networking library, analytics, or crash-reporting SDK. The merged Android manifest has no `INTERNET` permission.
6. **Storage unit — centimeters (`Double`).** This is the Step 10 storage-unit decision (`CLAUDE.md` §4 defers it to Step 10). Inches are converted only for display and input. The garment definitions in `model/Measurements.kt` (ranges in `minInches`/`maxInches`) stay unchanged; convert at the boundary with the `util/` helpers.
7. **Photos are never saved permanently.** Photos stay in memory or in the app's cache and are deleted as soon as measurement finishes, is cancelled, or the screen is left. Never in the gallery.
8. **All text comes from resources.** Bengali or English strings may not be written directly in the UI. Bengali (default) is in `composeResources/values/strings.xml`, English in `composeResources/values-en/strings.xml`, with the same keys. Model/logic code holds only keys (`"m_chest"`), never display text.
9. **One sub-step at a time.** Step 10 is split into sub-steps (Section 8). One sub-step = one session: build, run the tests, report, then stop.

## 2. Architecture

&#91;embedded content: Architecture · commonMain and the two platforms\]

Shared code never calls the platform directly; it only calls the abstractions in `platform/` (interfaces) or small `expect` functions. The platform part only implements them and makes no business decisions.

MVI (from `CLAUDE.md` §5): `UI → Action → ViewModel → immutable UiState → UI`. One `androidx.lifecycle.ViewModel` (KMP) per flow, `StateFlow<UiState>` collected with `collectAsStateWithLifecycle()`. One-off effects (navigation, share sheet, snackbar) go through a `Channel`/`SharedFlow`. The stateless `XxxScreen(state, onAction)` composables from Steps 1–9 are kept; Step 10 replaces their sample data with ViewModel state.

## 3. Feature split

Of the 23 features, 13 are almost entirely common. For the database and settings, only the file path differs. In the remaining 10, only the hardware or OS part goes to the platform; the logic is still common.

| Feature | commonMain | Android (androidMain) | iOS (iosMain / Swift) |
| --- | --- | --- | --- |
| All screens, sheets, dialogs (52 designs) | All (done in Steps 1–9) | — | — |
| Navigation, ViewModel, state | All | — | — |
| Profiles, saved measurements, unfinished measurements (database) | All (Room) | Database file path | Database file path |
| Settings (language, display unit, onboarding seen; theme follows the system) | All (DataStore) | File path | File path |
| Garments and measurement lists | All (`model/Measurements.kt`) | — | — |
| Tape measurement, unusual-measurement warning | All | — | — |
| Measurement math: scale, circumference, calibration, fit allowance | All | — | — |
| Accuracy indicator (high/medium/low) | All | — | — |
| Size chart and size suggestion | All (`suggestSize()` in `Measurements.kt`) | — | — |
| Creating and reading backup/restore JSON | All | — | — |
| Bengali/English, Bengali digits, inch/cm | All (`util/`) | — | — |
| Result diagram (body figure, numbers) | All (`NumberedDiagram`, Compose) | — | — |
| Camera preview, timer, overlay | All (Kamera + our overlay/timer) | CameraX inside Kamera | AVFoundation inside Kamera |
| Photo quality check (lighting, blur) | All (decode + calculation) | — | — |
| **AI pose and body outline** | Deriving measurements from landmarks | MediaPipe Tasks Vision | MediaPipe (Swift bridge, CocoaPods in `iosApp`) |
| **PDF creation** | Layout calculation (what goes where) | Drawing with `PdfDocument` + Canvas | Drawing with `UIGraphicsPDFRenderer` |
| Share as PNG image | `ImageBitmap` from Compose (`GraphicsLayer.toImageBitmap()`) | PNG encode | PNG encode |
| Share sheet | Calling it | `Intent.ACTION_SEND` + FileProvider | `UIActivityViewController` |
| Saving/opening backup file | All (FileKit) | — | — |
| Voice guide (pre-recorded audio) | Which audio plays when | `MediaPlayer` | `AVAudioPlayer` |
| Camera permission | Permission screen UI (3.2) | System permission request | System permission request + Info.plist |
| Version 2: App lock | UI | androidx.biometric | LocalAuthentication |
| Version 3: Reminders | When to send a reminder | WorkManager / AlarmManager | UNUserNotificationCenter |

## 4. Library list

The project already uses Kotlin 2.4.20, AGP 9.1.1 and CMP 1.12.1 (`plan.md` §4). Navigation, lifecycle and kotlinx.serialization are added in Step 0. For every new library, the agent verifies the latest stable version compatible with these at the time of work and writes it into `libs.versions.toml`. "Latest stable" in the version column means it was not verified here.

### commonMain

| Purpose | Library | Version | Note |
| --- | --- | --- | --- |
| UI | Compose Multiplatform (Material 3) | 1.12.1 (project) | Stable on iOS since May 2025 (1.8.0) |
| Screen text, fonts, images, audio, model files | Compose Multiplatform Resources | Same as CMP | Bengali in `values/`, English in `values-en/` |
| Navigation | `org.jetbrains.androidx.navigation:navigation-compose` (multiplatform) | From Step 0 | Type-safe routes, `@Serializable` |
| ViewModel and Lifecycle | lifecycle-viewmodel-compose / runtime-compose (KMP) | From Step 0 |  |
| Database | Room (KMP) + sqlite-bundled | 2.8.x | Same SQLite on Android and iOS; needs KSP + Room plugins |
| Settings | DataStore Preferences (KMP) | 1.2.x |  |
| DI | Koin (core + compose-viewmodel) | Latest stable | Hilt does not work on KMP; do not use it |
| JSON (backup) | kotlinx.serialization | From Step 0 |  |
| Dates | kotlinx-datetime | Latest stable | Format Bengali month names yourself |
| Coroutines | kotlinx.coroutines | Latest stable |  |
| Camera | Kamera (formerly CameraK), `io.github.kashif-mehmood-km:camerak` + analyzer plugin | 1.2.x (Aug 2026) | Must pass the gate in Section 5; otherwise own `expect` camera |
| Backup file save/open | FileKit (core + dialogs-compose) | Latest stable | Backup/restore only; sharing uses our own `FileSharer` |
| Logging | Kermit | Latest stable | Local debug logs only; never sent anywhere; never log names, heights or measurement values |
| Testing | kotlin.test, kotlinx-coroutines-test | — | Fakes, no mocking frameworks |

### androidMain

| Purpose | Library / API |
| --- | --- |
| Pose and outline | `com.google.mediapipe:tasks-vision` (Pose Landmarker + Image Segmenter) |
| PDF | Android `android.graphics.pdf.PdfDocument` + `Canvas`, Bengali text via `StaticLayout` with the bundled Hind Siliguri font |
| PNG encode | `Bitmap.compress(PNG)` |
| Share | `Intent.ACTION_SEND` + `androidx.core` FileProvider (cache folder) |
| Audio | `MediaPlayer` |
| Entry point | `MainActivity` (`ComponentActivity` + `setContent { App() }`) — already in place from Step 0 |
| Versions 2–3 | androidx.biometric, WorkManager |

### iosMain (Kotlin) and iosApp (Swift) — Step 11

| Purpose | Library / API | Where |
| --- | --- | --- |
| Pose and outline | `MediaPipeTasksVision` pod (CocoaPods only; MediaPipe has no SPM package) | Swift (`iosApp`) |
| PDF | `UIGraphicsPDFRenderer` + `NSAttributedString` | Kotlin (`iosMain`) |
| PNG encode | `UIImagePNGRepresentation` | Kotlin (`iosMain`) |
| Share | `UIActivityViewController` | Kotlin (`iosMain`) |
| Audio | `AVAudioPlayer` | Kotlin (`iosMain`) |
| Entry point | `MainViewController()` = `ComposeUIViewController { App() }` | Kotlin + Swift |
| Versions 2–3 | LocalAuthentication, UserNotifications | Kotlin (`iosMain`) |

### Do not use

- **Hilt / Dagger:** does not support KMP.
- **Ktor, Retrofit, Firebase, Crashlytics, Analytics:** internet or data collection.
- **ML Kit pose detection:** stay with MediaPipe, so both platforms get the same model and the same results.
- **Core ML / a different iOS model:** MediaPipe is used on iOS too, for the same reason.
- **Any other PDF library:** many of them break Bengali conjuncts.
- **System TTS for the voice guide:** a Bengali voice is not guaranteed on every iPhone, so pre-recorded audio will be used.
- **Kotlin CocoaPods Gradle plugin:** the framework is linked with `embedAndSignAppleFrameworkForXcode`; CocoaPods is used only inside `iosApp` for MediaPipe.

## 5. Platform abstractions

All platform work sits behind the abstractions below. Stateful services are **interfaces** in `commonMain/platform/`, and each platform's implementation is provided through a Koin platform module. Small things use **`expect` functions** (never `expect`/`actual` classes). Every `expect` and every interface also needs an iOS implementation — list it under `plan.md` Step 11 in the same change.

| Abstraction | Kind | Purpose | Status |
| --- | --- | --- | --- |
| `setAppLanguage(tag)` + `LocalAppLocale` | `expect fun` | In-app language switch | Exists since Step 0 |
| `BodyMeasurer` | interface | On-device AI: landmarks + body mask from a photo | Step 10.8 |
| `PdfRenderer` | interface | Draws the common report layout into a PDF file | Step 10.5 |
| `ImageEncoder` | interface | `ImageBitmap` → PNG file | Step 10.5 |
| `FileSharer` | interface | OS share sheet | Step 10.5 |
| `VoicePlayer` | interface | Plays pre-recorded voice clips | Step 10.7 |
| `CameraPermission` | interface | System camera permission | Step 10.7 |
| Database builder / DataStore path | `expect fun` | Platform file paths for Room and DataStore | Step 10.2 |
| `CameraPreview(…)` | common `@Composable` | Wraps Kamera; becomes `expect @Composable` only in the fallback | Step 10.7 |

Backup file pick/save needs no abstraction (FileKit is common).

```kotlin
// commonMain/platform/BodyMeasurer.kt
interface BodyMeasurer {
    /** Extracts 33 landmarks and the body mask from one photo. null if no body is found. */
    suspend fun analyze(image: CapturedImage): PoseResult?
    /** Lightweight analysis for the live guide (without the mask) */
    suspend fun analyzeFrame(image: CapturedImage): List<Landmark>?
    fun close()
}

data class CapturedImage(val width: Int, val height: Int, val rgba: ByteArray)
data class Landmark(val x: Float, val y: Float, val z: Float, val visibility: Float) // 0..1 normalized
data class PoseResult(
    val landmarks: List<Landmark>,   // 33, in MediaPipe order
    val maskWidth: Int, val maskHeight: Int,
    val personMask: FloatArray        // per-pixel probability of being a person, 0..1
)

// commonMain/platform/PdfRenderer.kt
interface PdfRenderer {
    /** Draws the layout computed in common, saves the PDF to cache, and returns the path */
    suspend fun render(page: ReportPage, fileName: String): String
}

// commonMain/platform/ImageEncoder.kt
interface ImageEncoder {
    suspend fun encodePng(bitmap: ImageBitmap, fileName: String): String // path in cache
}

// commonMain/platform/FileSharer.kt
interface FileSharer {
    fun share(path: String, mimeType: String, title: String)
}

// commonMain/platform/VoicePlayer.kt
interface VoicePlayer {
    fun play(clip: VoiceClip)   // enum: STEP_BACK, TURN_RIGHT, HOLD_STILL, PHOTO_TAKEN ...
    fun stop()
}

// commonMain/platform/CameraPermission.kt
interface CameraPermission {
    suspend fun status(): PermissionStatus
    suspend fun request(): PermissionStatus
}
```

### Where each abstraction is implemented

| Abstraction | Android (Step 10) | iOS (Step 11) |
| --- | --- | --- |
| `BodyMeasurer` | `androidMain`, Kotlin, MediaPipe Tasks Vision | **Swift** (`iosApp`), MediaPipeTasksVision pod; the Swift `MediaPipePoseBridge` implements the small `IosPoseBridge`; `IosBodyMeasurer` in `iosMain` wraps it to make a `BodyMeasurer` |
| `PdfRenderer` | `androidMain`, `PdfDocument` | `iosMain`, `UIGraphicsPDFRenderer` |
| `ImageEncoder` | `androidMain`, `Bitmap.compress(PNG)` | `iosMain`, `UIImagePNGRepresentation` |
| `FileSharer` | `androidMain`, Intent + FileProvider | `iosMain`, `UIActivityViewController` |
| `VoicePlayer` | `androidMain`, `MediaPlayer` | `iosMain`, `AVAudioPlayer` |
| `CameraPermission` | `androidMain`, Activity Result API | `iosMain`, `AVCaptureDevice.requestAccess` |
| DB / DataStore path | `androidMain`, `Context` file dirs | `iosMain`, `NSFileManager` document dir |

### Specific rules

- **BodyMeasurer — why not one common implementation:** MediaPipe is published only as an Android Maven AAR and an iOS CocoaPods pod (no KMP artifact, no SPM package), and no mature KMP pose/segmentation library exists (checked Oct 2026). So each platform implements `BodyMeasurer`, but it **only returns landmarks and the mask**. All calculations of scale, width, circumference and confidence are in `commonMain/measure/`, so results are exactly the same on both platforms. The model files (`.task`) are the same version on both platforms. Load models lazily, off the main thread, and release them in `close()`.
- **PdfRenderer:** `ReportPage` is the list of drawing elements for an A4 (595×842 pt; the 10.1/10.2 designs are the same page at 794×1123 CSS px, so scale by 0.75) page: `Text`, `Line`, `Rect`, `Circle`, `Path`. The common `ReportLayoutBuilder` decides which text goes where, following the Step 8 designs (10.1/10.2). The platform only draws and makes no decisions. The body diagram is also defined in common as a `Path`, so the same picture appears in all three places: screen, PDF and PNG.
- **Camera — user approval first:** before writing any camera code (spike included), send the user a written description — approach, library + version, files and source sets, how photos stay private and are deleted, how the gate below is tested, the fallback — and wait for an explicit go-ahead. Ask again before switching to the fallback.
- **Camera — Kamera with a verification gate:** after approval, in Step 10.7, do a short spike with Kamera 1.2 on a low-cost Android phone and confirm all of:
  1. Preview works inside our Compose screen with our `CameraOverlay` drawn on top (Step 4 designs).
  2. Capture keeps photos private: use the in-memory capture listener, or `takePictureToFile()` only if it writes into the app cache (never the gallery/`Pictures`) and the file is deleted in a `finally` block.
  3. The analyzer plugin's live frames (JPEG `ByteArray` on Android/iOS) can be decoded and passed to `analyzeFrame` fast enough for the live pose guide (target ≥ 5 analysed frames/s).
  4. Front/back lens switch works, and the merged manifest still has no `INTERNET`.

  If all pass, use Kamera inside our common `CameraPreview(…)` composable, so screens never depend on the library directly. If any fail, replace it with `expect @Composable fun CameraPreview(…)`: CameraX (`AndroidView`) on Android, AVFoundation preview in `UIKitView` on iOS — same signature, screens unchanged. Report the spike result either way. The countdown timer is ours, in common.
- **Image decoding is common:** turn Kamera's JPEG bytes into a `CapturedImage` in common (`decodeToImageBitmap()` from Compose Resources + `ImageBitmap.readPixels()`); downscale live frames before analysis.
- **Photo quality check:** calculated in common from `CapturedImage.rgba`: average brightness (lighting) and Laplacian variance (blur). Whether the whole body is in the frame is determined from the landmarks.

### Swift bridge on iOS (Step 11)

Implementing Kotlin `suspend` functions or `Flow` from Swift is cumbersome. So Swift will implement only one simple, synchronous interface, and `IosBodyMeasurer` in `iosMain` will run it on `Dispatchers.Default` to make a `BodyMeasurer`.

```kotlin
// shared/src/iosMain/kotlin/com/tech_hunt/tailorpass/platform/IosPoseBridge.kt  (Swift will implement this)
interface IosPoseBridge {
    fun load(poseModelPath: String, segmenterModelPath: String)
    /** Synchronous. null = no body found */
    fun analyze(rgba: NSData, width: Int, height: Int, withMask: Boolean): IosPoseOutput?
    fun close()
}

class IosPoseOutput(
    val landmarks: FloatArray,      // 33 × 4 values: x, y, z, visibility
    val maskWidth: Int,
    val maskHeight: Int,
    val mask: FloatArray?           // null when withMask = false
)

// shared/src/iosMain/kotlin/com/tech_hunt/tailorpass/di/KoinIos.kt
fun initKoin(poseBridge: IosPoseBridge) { /* startKoin { modules(common + iosPlatformModule(poseBridge)) } */ }
```

```swift
// iosApp/iosApp/iOSApp.swift
import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() { KoinIosKt.doInitKoin(poseBridge: MediaPipePoseBridge()) }
    var body: some Scene { WindowGroup { ContentView() } }
}
```

- Interfaces exposed to Swift have no `suspend`, `Flow`, generics, sealed classes or default arguments. Only simple types: `NSData`, `FloatArray`, `String`, `Int`, `Boolean`.
- Kotlin's `initKoin` appears in Swift as `doInitKoin` (Kotlin prefixes `do` to names starting with `init`). Top-level functions appear as `<FileName>Kt.<fn>`.
- `NSData` is created from `ByteArray` in `iosMain`, copying it in one go with `usePinned`.

## 6. Project structure

The structure is the one from `plan.md` / `CLAUDE.md` §2 (JetBrains' [chat example](https://github.com/JetBrains/compose-multiplatform/tree/master/examples/chat), Android + iOS only): `shared`, `androidApp`, `iosApp`. It is already in place after Step 0 — **do not restructure, rename or move modules**. Step 10 only adds the packages and files below.

**Package name:** root package `com.tech_hunt.tailorpass`; new code goes in sub-packages (e.g. `com.tech_hunt.tailorpass.measure`, `com.tech_hunt.tailorpass.data.db`). Keep the existing namespaces/applicationId as they are.

| Part | What | Gradle plugins | What goes here |
| --- | --- | --- | --- |
| `shared` | KMP library | `kotlinMultiplatform`, `androidMultiplatformLibrary`, `composeMultiplatform`, `composeCompiler`, `kotlinSerialization`; Step 10 adds `ksp`, `room` | All UI, logic, data, resources, abstractions and their androidMain/iosMain implementations. Static framework `Shared` for iOS (`import Shared`) |
| `androidApp` | Android app shell | `androidApplication`, `composeMultiplatform`, `composeCompiler` | `MainActivity`, `Application` class (starts Koin), `AndroidManifest.xml` (CAMERA, FileProvider), `res/xml/file_paths.xml`, launcher icon, `app_name`, R8 rules. No business logic |
| `iosApp` | Xcode project (Step 11) | — | SwiftUI shell, MediaPipe Swift bridge, Podfile, Info.plist, `InfoPlist.xcstrings`, `Config.xcconfig` |

```text
project/
├─ settings.gradle.kts                 # include(":androidApp"), include(":shared")
├─ build.gradle.kts                    # plugins apply false
├─ gradle/libs.versions.toml
├─ for-agent/                          # docs for the agent; not part of the app
│
├─ androidApp/
│  ├─ build.gradle.kts                 # implementation(project(":shared"))
│  ├─ proguard-rules.pro               # R8: keep rules for MediaPipe, kotlinx-serialization, Room
│  └─ src/main/
│     ├─ AndroidManifest.xml           # CAMERA, FileProvider; not INTERNET
│     ├─ kotlin/com/tech_hunt/tailorpass/MainActivity.kt   # setContent { App() }
│     ├─ kotlin/com/tech_hunt/tailorpass/TailorPassApp.kt   # Application: starts Koin (common + Android module)
│     └─ res/                          # launcher icon, xml/file_paths.xml, values{,-en}/strings.xml (app_name)
│
├─ shared/
│  ├─ build.gradle.kts                 # kotlin { android { … androidResources { enable = true } }; iosArm64(); iosSimulatorArm64() }
│  │                                   # framework { baseName = "Shared"; isStatic = true }
│  └─ src/
│     ├─ commonMain/kotlin/com/tech_hunt/tailorpass/
│     │  ├─ App.kt                     # @Composable fun App(): MaapTheme + navigation (from Step 0)
│     │  ├─ model/                     # Measurements.kt, Diagrams.kt (verbatim) + domain models
│     │  ├─ util/                      # toBanglaDigits(), formatInches(), cm/inch helpers
│     │  ├─ ui/theme/ ui/components/ ui/diagram/ ui/util/ ui/nav/
│     │  ├─ ui/screens/<section>/      # from Steps 1–9; Step 10 adds a ViewModel per flow
│     │  ├─ di/                        # Koin common module (Step 10)
│     │  ├─ data/db/ data/prefs/ data/backup/   # Room, DataStore, backup JSON + repositories (Step 10)
│     │  ├─ measure/                   # scale, circumference, calibration, confidence, photo quality
│     │  ├─ report/                    # ReportPage, ReportLayoutBuilder
│     │  └─ platform/                  # abstractions (Section 5)
│     ├─ commonMain/composeResources/
│     │  ├─ values/strings.xml (bn, default)  values-en/strings.xml
│     │  ├─ font/                      # Hind Siliguri
│     │  ├─ drawable/
│     │  └─ files/
│     │     ├─ audio/bn/  audio/en/   # voice guide clips
│     │     └─ models/                # pose_landmarker.task, segmenter model (same copy for both platforms)
│     ├─ commonTest/kotlin/com/tech_hunt/tailorpass/       # tests that run on both platforms (most tests go here)
│     ├─ androidHostTest/kotlin/com/tech_hunt/tailorpass/  # Android tests on the JVM, no phone needed
│     ├─ iosTest/kotlin/com/tech_hunt/tailorpass/          # tests on the iOS simulator (Step 11)
│     ├─ androidMain/kotlin/com/tech_hunt/tailorpass/
│     │  ├─ di/PlatformModule.android.kt
│     │  └─ platform/                  # Android implementations (<name>.android.kt)
│     └─ iosMain/kotlin/com/tech_hunt/tailorpass/          # Step 11 only
│        ├─ MainViewController.kt      # fun MainViewController() = ComposeUIViewController { App() }
│        ├─ di/KoinIos.kt              # fun initKoin(poseBridge: IosPoseBridge)
│        └─ platform/                  # IosBodyMeasurer (wraps the bridge), PDF, PNG, Share, Voice, Permission, paths
│
└─ iosApp/                             # Step 11 only
   ├─ Configuration/Config.xcconfig      # TEAM_ID, BUNDLE_ID, APP_NAME
   ├─ Podfile                            # only MediaPipeTasksVision, in the iosApp target
   ├─ iosApp.xcodeproj                   # Build Phase: ./gradlew :shared:embedAndSignAppleFrameworkForXcode
   └─ iosApp/
      ├─ iOSApp.swift                    # in init: KoinIosKt.doInitKoin(poseBridge: MediaPipePoseBridge())
      ├─ ContentView.swift               # UIViewControllerRepresentable → MainViewControllerKt.MainViewController(), .ignoresSafeArea()
      ├─ MediaPipePoseBridge.swift       # import Shared; implements IosPoseBridge
      ├─ InfoPlist.xcstrings             # bn/en app name + camera usage text
      └─ Info.plist                      # NSCameraUsageDescription
```

**Rules for this structure:**

- **Entry points:** `MainActivity` only calls `setContent { App() }`. `ContentView` only shows `MainViewController()`. Both run the common `App()`. No SwiftUI tabs or SwiftUI screens; the whole app is Compose.
- **Permissions and FileProvider:** `shared` is a library, so the `CAMERA` permission and the `FileProvider` declaration go in `androidApp`'s Manifest. Do not hard-code the FileProvider authority in code; provide it to Koin from `androidApp`.
- **iOS framework:** linked directly with `embedAndSignAppleFrameworkForXcode`, like the chat example. CocoaPods is used only in `iosApp`, for MediaPipe (it has no SPM package), so always open the `.xcworkspace` in Xcode.
- **Model files:** only one copy, in `composeResources/files/models/`. On Android, read the bytes and pass them to `BaseOptions.setModelAssetBuffer` (direct `ByteBuffer`). On iOS, Kotlin writes the file to cache once and gives the path to the bridge.
- **Garment definitions stay in `model/Measurements.kt`** (measurement lists, order, ranges, presets, size chart, `suggestSize()`), copied verbatim from `kmp-resources` — no `garments.json`/`sizecharts.json`. The new per-garment data for AI — calibration factors and fit allowances — is added in `model/` next to those definitions in Step 10.8; ask before changing any existing value or unit in `Measurements.kt`. Display text (how to measure, mistakes to avoid) stays as string keys.

**Where to put tests:** whatever is the same on both platforms (math, sizes, formatting, backup, report layout, ViewModels) goes in `commonTest`. Tests for Android-only parts (e.g. `AndroidPdfRenderer`) go in `androidHostTest`, and tests for iOS parts in `iosTest` (Step 11). Try Room DAO tests first in `androidHostTest` with an in-memory database; if that does not work, ask before adding Android instrumented tests.

**R8:** with minify enabled in the release build, MediaPipe classes may get stripped. Add keep rules for MediaPipe, kotlinx-serialization and Room in `proguard-rules.pro`, and run the AI measurement once on every release build to check.

## 7. Bengali, privacy and testing

### Bengali and language

- **Font bundled in the app.** Hind Siliguri is already in `composeResources/font` and used by `MaapTheme` typography (Step 0). Do not rely on the system font. Pass the same font file to the platform when creating PDFs.
- **Conjunct check.** Verify "ক্ষ ন্ত স্ত্র ঙ্গ র্ত্য পোশাকের গলার গভীরতা" on screen, in PDF and in PNG — on Android in Step 10.5, on iOS in Step 11.
- **Bengali digits.** When the language is Bengali, all numbers use the digits ০–৯; in English, 0–9. Use the `util/` helpers (`toBanglaDigits()`, `formatInches()`, `useBangla`), not ad-hoc formatting in the UI.
- **Displaying inches.** Round to the nearest ½ inch (২৪½"). Centimeters are shown to one decimal place. Pressing −/+ in the tape input (`NumberStepperField`) changes the value by ½ inch or 1 cm.
- **Switching language.** The in-app Bengali/English toggle (`setAppLanguage` + `LocalAppLocale`, from Step 0) is separate from the phone's language and is stored in DataStore. More languages (Arabic, Urdu) may be added later, so use `start/end` in layouts, not `left/right`.

### Privacy

- Android Manifest: only the `CAMERA` permission. Verify on every release that the release build's merged manifest has no `INTERNET` (adding any library — Kamera, FileKit, MediaPipe — can bring it in unnoticed; if needed, use `tools:node="remove"`).
- iOS Info.plist: `NSCameraUsageDescription` in Bengali and English (`InfoPlist.xcstrings`). App Store privacy label: no data collected.
- Photos are kept in memory as `ByteArray`; discard them immediately when processing finishes, on cancel, or when the screen is left. If they must be written to disk, use only the cache, and delete them in a `finally` block.
- Create PDFs/PNGs in the cache; delete old share files when the app starts.
- Do not write names, heights or measurement values to logs.
- "Delete all data" (12.4): all Room tables, DataStore and the cache folder are emptied.

### Testing

| What | Where | How |
| --- | --- | --- |
| Measurement math (scale, circumference, fit) | `commonTest` | Expected results from known landmarks + mask |
| Size suggestion, valid-range warning | `commonTest` | Values on both sides of the limits |
| Bengali digits, ½, inch/cm | `commonTest` | 36.25 → "৩৬½", etc. |
| cm storage ↔ inch display | `commonTest` | Round-trip keeps ½-inch values exact |
| Backup → restore | `commonTest` | All data comes back exactly (merge and replace); old backups still restore after a schema change |
| ViewModels | `commonTest` | Fake repositories/abstractions; loading/success/empty/error states |
| Room DAO | `androidHostTest` in-memory (instrumented only if needed) | Save, edit, delete, unfinished measurement |
| Report layout | `commonTest` | No text goes outside A4 |
| AI accuracy | Real phone | Compare with tape measurements of 10–20 people; target ±2 cm on circumferences |
| Speed | Low-cost Android (3–4 GB RAM) | AI processing within 10 seconds |

Commands: `./gradlew :shared:testAndroidHostTest` and `./gradlew :androidApp:assembleDebug` after every sub-step (`CLAUDE.md` §8).

## 8. Order of work (after the design phase)

**Prerequisite:** `plan.md` Steps 0–9 are finished — all 52 designs built and tested with sample data, previews matched with the PNGs, Android build passing. Step 0 already covered the project structure, theme, fonts, strings, navigation skeleton, base components and the language `expect`.

Step 10 is split into the sub-steps below. Give each sub-step to the agent as a separate task; at the end of each, `:shared:testAndroidHostTest` and `:androidApp:assembleDebug` must pass, then stop. Design section numbers refer to `SCREENS.md`.

- **10.1 Foundation:** Koin (common module, Android module, `Application` class), the `platform/` interfaces from Section 5 (Android implementations empty for now), FileProvider + `file_paths.xml`, Kermit (local only).
- **10.2 Data:** cm storage (Section 1 rule 6), Room entities (`Profile(name, gender, heightCm, fit)`, `MeasurementSet(profileId, garment, date, method AI|TAPE, unit, note, status COMPLETE|INCOMPLETE)`, `MeasurementValue(setId, key, valueCm, source)` incl. skipped measurements), database/DataStore path `expect`s, DataStore (language, display unit, onboarding seen — theme follows the system), repositories mapping entities to domain models, migrations policy, tests.
- **10.3 First launch and Home (design sections 1–2, plan Steps 1–2):** ViewModels for language, onboarding, profile, home, profile sheet, saved-measurement menu; remove their sample data.
- **10.4 Tape measurement and verification (sections 3, 7, 8; plan Steps 3, 5–7):** first ask the user for the missing Shirt/Pants/Pajama measurement lists and size charts (`plan.md` §3 "Missing garment data"); method selection, the full tape flow (incl. resume of an incomplete measurement), verify, edit sheet. After this sub-step, the app is usable without AI.
- **10.5 Result, share, size chart (sections 9–11; plan Steps 8–9):** `ReportLayoutBuilder`, Android `PdfRenderer`, `ImageEncoder`, `FileSharer`, size suggestion; Bengali conjunct check in PDF and PNG.
- **10.6 Settings (section 12; plan Step 9):** backup/restore with FileKit (merge vs replace, in a transaction), delete all data, language and theme persisted, privacy policy, about.
- **10.7 Camera and preparation (sections 4–6; plan Steps 3–4):** **start with the written camera description and wait for the user's go-ahead (Section 5)**, then the Kamera gate (Section 5) and its report, `CameraPreview`, `CameraPermission`, timer, `VoicePlayer` + audio clips, photo quality check.
- **10.8 AI measurement:** Android `BodyMeasurer` (MediaPipe), the `measure/` pipeline, confidence badges, calibration factors and fit allowances, accuracy check on real people.
- **10.9 Before the Android release:** acceptance checklist, `:androidApp:assembleRelease`, no `INTERNET` in the merged manifest, R8/ProGuard keeps MediaPipe/serialization/Room, AI measurement works in the release build.
- **Step 11 — iOS (Mac, Xcode, Apple Developer account):** `MainViewController.kt` + `ContentView.swift`, Podfile + `MediaPipePoseBridge.swift`, iOS implementations of every abstraction (`BodyMeasurer` via the bridge), `Info.plist`/`InfoPlist.xcstrings`, then re-run the full checklist and the PNG comparison on the simulator and a real iPhone.

## References

- [Kotlin Multiplatform: Jetpack library support and versions](https://developer.android.com/kotlin/multiplatform)
- [Compose Multiplatform 1.8.0: iOS stable](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released-compose-multiplatform-for-ios-is-stable-and-production-ready/embed/)
- [Kamera 1.0 announcement](https://slack-chats.kotlinlang.org/t/34099386/hi-everyone-camerak-is-now-kamera-and-it-has-finally-reached)
- [Kamera repository and releases](https://github.com/Kashif-E/Kamera/releases)
- [MediaPipe Pose Landmarker: Android](https://developers.google.com/mediapipe/solutions/vision/pose_landmarker/android)
- [MediaPipe Pose Landmarker: iOS](https://developers.google.com/edge/mediapipe/solutions/vision/pose_landmarker/ios)
- [MediaPipe Image Segmenter: iOS](https://developers.google.com/edge/mediapipe/solutions/vision/image_segmenter/ios)
