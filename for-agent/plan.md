# TailorPass — Implementation Plan (Compose Multiplatform · Android + iOS)

> Coding agent: **read this whole file and [`PROJECT_OVERVIEW.md`](PROJECT_OVERVIEW.md) before starting work.** The Overview defines the product scope (v1 features, garments, privacy promise); this file defines the order and what each step builds. It replaces the old `AGENT_GUIDE.md` — every instruction from that file is kept here (sections 2–6).
---

## 1. App overview

**TailorPass** — a fully offline clothing-measurement app for people in Bangladesh.

> **App name:** **"TailorPass"** in both languages (`app_name`, iOS `CFBundleDisplayName`, `onb_welcome` = "TailorPass-এ স্বাগতম" / "Welcome to TailorPass" — already changed in `kmp-resources`). The design HTML/PNGs still show the old name **"মাপ" / "Maap"** — match everything else in them exactly except the name; you do not need to list the name difference in the comparison list. Old names inside the code (`MaapTheme`, `Dimens`, etc.) stay as they are.

- **What it does:** takes garment measurements for a profile (name, gender, height, fit preference) — in two ways:
  1. **With AI from photos:** two photos, front and side → measurements are computed on the phone (on-device), and each measurement gets an accuracy badge (● high / ◐ medium / ▲ low).
  2. **By hand with a tape:** enter measurements one at a time while looking at a picture/diagram; preference measurements (length, neck depth) are picked from picture cards.
- **Then:** verify and edit the measurements → result (numbered diagram + suggested ready-made size) → share with the tailor as a **PDF (A4)** or an **image (PNG)**.
- **Garments:** shirt, pants, panjabi, pajama (men), salwar kameez (women), genji/undershirt (size suggestion only).
- **Other:** multiple profiles, list of saved measurements, resume an incomplete measurement later, size chart, backup/restore (JSON file), delete all data, Bengali/English, light/dark mode.
- **Privacy:** no internet permission; photos are deleted as soon as measurements are extracted; all data stays on the phone only.
- **Language:** Bengali by default, English second. Numbers in Bengali digits.

### Platform and approach — **Compose Multiplatform (CMP)**
The whole UI is written **once** in Compose (`commonMain`), and that same UI runs on both Android and iOS. **No separate UI will be built in SwiftUI.**

- `shared` (Kotlin Multiplatform + Compose Multiplatform):
  - `commonMain/kotlin/com/tech_hunt/tailorpass/`
    - `model/`, `util/`, later `data/` — models, business logic, data, view-model/state.
    - `ui/` — **all screens, components, theme, navigation** (`App.kt`, `ui/theme/`, `ui/components/`, `ui/screens/<section>/`, `ui/nav/`).
  - `commonMain/composeResources/` — `values/strings.xml` (bn, default), `values-en/strings.xml`, `drawable/` (63 VectorDrawable XMLs), `font/` (Hind Siliguri). In code: `Res.string.*`, `Res.drawable.*`, `Res.font.*`.
  - `androidMain` / `iosMain` — only platform-dependent parts (`expect`/`actual` functions or interface implementations): language switching, AI pose, share/PDF/PNG, voice, camera permission, storage path (full list in the [technical guideline](KMP%20+%20CMP%20Technical%20guideline.en.md) §5, used from Step 10).
  - `iosMain/.../MainViewController.kt` — `fun MainViewController() = ComposeUIViewController { App() }`.
- `androidApp` — thin shell: `MainActivity` → `setContent { App() }`, manifest, launcher icon.
- `iosApp` — thin shell: `ContentView` is a `UIViewControllerRepresentable` that shows `MainViewControllerKt.MainViewController()` (`.ignoresSafeArea()`), `Info.plist`, `InfoPlist.xcstrings`.
- **Our order — Android only for now:** build the design section by section in `commonMain` (with sample data), run it on Android / check it in previews, and match it against the PNGs. Because the code is written in common, the same UI will later run on iOS, but **no iOS work (iosMain, iosApp, iOS build/test) is done now** — that happens separately, on a Mac, in Step 11. After the design is finished: data/logic and the Android platform parts (camera, share). The first public release is Android (version 1); Step 11 makes the iOS app work, and its store release is planned for version 2 (Project Overview §6).
- **Reference project:** JetBrains' [`examples/chat`](https://github.com/JetBrains/compose-multiplatform/tree/master/examples/chat) — same structure: `shared` (`kotlinMultiplatform` + `composeCompiler` + `androidMultiplatformLibrary` + `composeMultiplatform`, `android { androidResources { enable = true } }`), UI and `composeResources/` in `shared/commonMain`, `api(activity-compose, appcompat, core-ktx)` in `androidMain`, only `MainActivity` → `setContent { … }` in `androidApp`, `ComposeUIViewController { … }` on iOS + `UIViewControllerRepresentable` in Swift. When in doubt about the setup, look at it.
- Design frame **360×800** (CSS px = dp = iOS pt) — must be matched pixel-accurately.

---

## 2. Sources of truth (in priority order)

**Product scope** comes from [`PROJECT_OVERVIEW.md`](PROJECT_OVERVIEW.md): which features, garments, measurements and privacy promises v1 must have (§3–§5), what is v2/v3 (§6, not built now) and what is still open (§7). No step may break or drop a v1 item from it. If the Overview and a design or this plan disagree on *what* exists, stop and ask. The list below decides *how* it looks and behaves:

1. [`html/NN_*.html`](html/) — exact static HTML + inline CSS for every screen: spacing, sizes, colors, radius, font sizes, text.
2. [`screens/NN_*.png`](screens/) — rendered images (2×), for comparison. **Font note (checked in Step 2):** the PNGs were rendered with a narrower fallback Bengali font, not Hind Siliguri (Hind Siliguri text at the HTML sizes is ~28–35% wider and ~10% taller than in the PNGs; no Hind Siliguri weight/size reproduces them). Decision: keep Hind Siliguri at the HTML's sizes. Compare PNGs for layout, spacing, colours, icons and text content — not for text width/height, and do not list that difference per screen.
3. [`SCREENS.md`](SCREENS.md) / [`screens.json`](screens.json) — each screen's purpose, overlay base, navigation, and all visible text.
4. [`navigation.json`](navigation.json) — screen → which screens it goes to (build the nav graph from this **plus the missing links in §5 rule 7** — the file was extracted from the HTML links and has gaps).
5. [`design-tokens.json`](design-tokens.json) — colors, type scale, sizes.
6. [`kmp-resources/`](kmp-resources/) — ready-made resources. **Use these instead of building anything new** (following the CMP conversion below). Read [`kmp-resources/README.md`](kmp-resources/README.md) and [`kmp-resources/FONTS.md`](kmp-resources/FONTS.md) first. The README was written for the old native-UI plan: only its file list and "one key, everywhere" idea still apply — its Android `R`/`getIdentifier`/`keep.xml` steps and all SwiftUI parts do not. The table below is the only conversion guide. `kmp-resources/shared/build.gradle.kts.snippet` is obsolete (old `androidTarget()`/`iosX64()` setup) — follow `CLAUDE.md` §2 Gradle instead.

Other: [`README.md`](README.md) (package index), [`maap-design-screens.pdf`](maap-design-screens.pdf) (all 52 screens together, 21 pages).

### What is in `kmp-resources/` and where it goes in CMP
| Source (`kmp-resources/…`) | Destination (`shared/src/commonMain/…`) | Conversion |
|---|---|---|
| `shared/.../model/Measurements.kt`, `model/Diagrams.kt`, `util/BanglaDigits.kt` | `kotlin/com/tech_hunt/tailorpass/model/`, `util/` | Copy verbatim. `MeasurementKey`, `Garment`, `GarmentMeasurements`, `LengthPreset`, `NeckDepthPreset`, `Fit`, `Gender`, `SourceBadge`, size chart + `suggestSize()`; `Diagrams.*`; `toBanglaDigits()`, `formatInches()` (২৪½), `inchesToCmText()`, `feetInchesToCm()` |
| `androidApp/src/main/res/values/strings.xml`, `values-en/strings.xml` (339 keys) | `composeResources/values/strings.xml`, `composeResources/values-en/strings.xml` | Copy verbatim (CMP understands the same XML format and `%1$s` arguments). Verify in the build that the `\"`/`\'` escapes are correct. |
| `androidApp/src/main/res/drawable/*.xml` (63) | `composeResources/drawable/` | Copy verbatim (CMP can read Android VectorDrawable XML). |
| 4 Hind Siliguri TTFs ([FONTS.md](kmp-resources/FONTS.md)) — **not in the package** (`font/` has only `.gitkeep`) | `composeResources/font/hind_siliguri_{regular,medium,semibold,bold}.ttf` | In Step 0 the agent downloads them from the official Google Fonts source (`github.com/google/fonts`, `ofl/hindsiliguri/`, SIL OFL), renames them as in FONTS.md and keeps `OFL.txt` next to them. Build-time download only — the app itself stays offline. |
| `androidApp/.../ui/theme/{Color,Type,Dimens,Theme}.kt` | `kotlin/com/tech_hunt/tailorpass/ui/theme/` | `R.font.x` → `Font(Res.font.x, …)` (composable — build the `FontFamily` inside a `@Composable` function). Remove any Android-only imports. `MaapTheme { }`, `MaapTheme.colors`, `MaapTheme.numbers`, `Dimens`, shapes (blue/green brand) unchanged. |
| `androidApp/.../ui/diagram/NumberedDiagram.kt` | `kotlin/.../ui/diagram/` | `painterResource(context.drawableId(name))` → `painterResource(drawable(name))`; remove `LocalContext`. |
| `androidApp/.../util/Strings.kt` | `kotlin/.../ui/util/Strings.kt` (rewritten) | Drop `getIdentifier`. Shared key → resource: using `Res.allStringResources[key]` and `Res.allDrawableResources[name]`, write `fun stringRes(key)`, `fun drawable(name)`, `@Composable fun str(key, vararg args)` (if not found, `error("Missing …")`). |
| `androidApp/src/main/res/raw/keep.xml` | — | **Not needed** (CMP resources are not shrunk). |
| `iosApp/Resources/InfoPlist.xcstrings`, `iosApp/Info.plist.snippet.xml` | `iosApp/iosApp/` | Keep — app name, camera usage text, bn/en localization, portrait only. **Drop** the `UIAppFonts` part (fonts come from composeResources). |
| `iosApp/Resources/Assets.xcassets`, `Localizable.xcstrings`, `iosApp/Theme/*.swift` | — | **Will not be used** (UI is not in SwiftUI). Kept only as reference. |
| `design-tokens.json` | — | Copy of the tokens (for verification) |

**Key rule:** model/logic code contains only **names** (string keys such as `"m_chest"`, image names such as `"ic_garment_panjabi"`). The UI resolves them from `composeResources` with `str(key)` / `drawable(name)`. **Never put display text in model/logic.** Fixed text uses `stringResource(Res.string.x)` directly.

---

## 3. Design rules (must be followed)

- Body text ≥ 16; everything tappable ≥ 48; **only one** filled primary button per screen; full-width 56-high buttons. Secondary buttons are outlined or links.
- Tailor's word first, English in brackets: "পুট (Shoulder)". Numbers in Bengali digits (`toBanglaDigits()`), half inch as "২৪½". In English, `useBangla = false`.
- Color never carries meaning alone — badges include the ● ◐ ▲ symbols and text.
- Long lists scroll, but the primary action stays fixed at the bottom.
- Bottom nav only on the tab screens (Home, Size chart, Settings) and on Result; hidden in the camera and step flows.
- Camera screens are always dark (`camera*` colors). In dark mode primary is `#A9C4F0`, with dark text on the primary button.
- Offline: no network permission; photos are deleted after measuring.
- Same Compose UI on both platforms — it must look like the PNGs. Do not leave things in Material's default style; apply the HTML's sizes/colors. Safe area/insets: `Modifier.windowInsetsPadding(WindowInsets.safeDrawing)` / `Scaffold` insets — must look right with the iOS notch and home indicator.
- No hard-coded strings — all text comes from `composeResources/values/strings.xml` keys.
- **No Android/iOS-only APIs in `commonMain`** (`android.*`, `R`, `LocalContext`, `platform.UIKit.*`). Use `expect`/`actual` when needed.

### API cheat sheet (all `commonMain`)
| Design | Compose Multiplatform |
|---|---|
| List scrolls, button fixed at the bottom (e.g. 8.4) | `Scaffold(bottomBar = …)` + `LazyColumn` |
| Bottom nav (3 tabs) | `NavigationBar` (custom style) |
| Top bar with back | Custom `TopBar` |
| Text | `stringResource(Res.string.x)` / `str(key)` |
| Icon | `Icon(painterResource(Res.drawable.ic_x), …, tint = …)` / `drawable(name)` |
| Color | `MaterialTheme.colorScheme.*`, `MaapTheme.colors.*` |
| Type | `MaterialTheme.typography.*`, `MaapTheme.numbers.*` |
| Dark mode | `MaapTheme(darkTheme = …)` |
| Language switch (1.1, 12.x) | `expect fun setAppLanguage(tag: String)`: Android `AppCompatDelegate.setApplicationLocales`; iOS `NSUserDefaults` `AppleLanguages` + UI refresh. In Compose, recompose immediately via `LocalAppLocale` — a plain **common** `staticCompositionLocalOf<String>` provided in `App()`, plus small `expect fun`s if the platform locale must be applied. The CMP docs' "custom locale" sample uses `expect object LocalAppLocale`; **do not copy that** (`expect`/`actual` classes/objects are forbidden, `CLAUDE.md` §3). |
| Format args | `stringResource(Res.string.x, arg1, …)` (`%1$s`) |
| Sheet / dialog (`overlay_on`) | `ModalBottomSheet` / `AlertDialog` / custom `Dialog` |
| Navigation | `org.jetbrains.androidx.navigation:navigation-compose` (multiplatform), type-safe `@Serializable` routes |
| Back | `NavController` + CMP `BackHandler` (Android back / iOS edge-swipe) |
| Preview | `@Preview` in `commonMain` (`androidx.compose.ui.tooling.preview` / `org.jetbrains.compose.ui:ui-tooling-preview`) |
| Camera preview (Step 10) | Common `CameraPreview(…)` wrapping Kamera (CameraX / AVFoundation inside); if Kamera fails the guideline's gate → `expect @Composable fun CameraPreview(…)` (Android `AndroidView` + CameraX; iOS `UIKitView` + AVFoundation) |
| PDF/PNG share | Step 8: only the common layouts + previews. Step 10.5: PNG via `rememberGraphicsLayer()` → `toImageBitmap()` (common) + `ImageEncoder`; PDF via `PdfRenderer`; share via `FileSharer` — interfaces with Android `PdfDocument` / `Bitmap.compress` / share `Intent` (iOS `UIGraphicsPDFRenderer` / `UIActivityViewController` in Step 11) |

### Sample data used in the design
- Profiles: **Rahat / রাহাত** (male, 5′7″, regular, 3 saved), **Farhana / ফারহানা** (female, 5′3″, regular, 2), **Abbu / আব্বু** (male, no measurements).
- Panjabi (inches): length 42, chest 40, belly 36, hip 41, shoulder 18, sleeve 24½, cuff 10, neck 15½ → size **L**.
- Salwar kameez: see 8.4 / 9.3 (10 kameez + 4 salwar measurements, size M).

### To be replaced later with real information (Placeholders)
Contact email (`about_contact_email`), the real size chart (`SampleMenShirtChart`), the normal ranges for the outlier warning (`minInches/maxInches`), sample values for length/neck depth, privacy policy text (matched to the app's real behavior), blue/green brand choice, app icon/logo (if `app_logo` is still the old "মাপ" design).

### Garment data sources (added 2026-10-18 at the user's request, from web sources)
The user asked the agent to find the missing garment data on the web and use it. Now in `Measurements.kt`; the agent still never changes these values on its own — new values come from the user.
- **Shirt / Pants / Pajama lists** (`shirt`, `pants`, `pajama` + `…ManualOrder`): the measurement names are Project Overview §4. Where and how each is measured follows tailoring guides (shirt: [Proper Cloth](https://propercloth.com/reference/dress-shirt-body-measurements/), [Luxire](https://luxire.com/pages/shirt-body-measurement-guide); trousers: [Luxire pant guide](https://mtm.luxire.com/pages/pant-measurement-guide)). Shirt waist = belly (`WaistBelly`, like panjabi); shirt length = `ShirtLength` (preference, number entry, no knee-length cards). New keys: `Biceps`, `ShirtLength`, `Thigh`, `Knee`, `Crotch` (rise), `Inseam`, `FullLength`. Their plausible ranges are rough starting values like the existing ones.
- **Bangla terms:** রান (Thigh), হাঁটু (Knee), হাই (Crotch) and মুহুরি/মোহরি (Bottom) as in Bangla tailoring guides ([textilegoln](https://textilegoln.com/%E0%A6%AE%E0%A6%BE%E0%A6%AA-%E0%A6%A8%E0%A7%87%E0%A6%AF%E0%A6%BC%E0%A6%BE%E0%A6%B0-%E0%A6%AA%E0%A6%A6%E0%A7%8D%E0%A6%A7%E0%A6%A4%E0%A6%BF/), [sattacademy](https://sattacademy.com/academy/%E0%A6%AA%E0%A7%8B%E0%A6%B6%E0%A6%BE%E0%A6%95-%E0%A6%A4%E0%A7%88%E0%A6%B0%E0%A6%BF)). হাই is the tailors' full crotch: navel, between the legs, to the back waist. ⚠ Not found in any source: মাসল (Biceps), ভেতরের লম্বা (Inseam), মোট লম্বা (Full length) — confirm with a tailor (Overview §7).
- **Plausible ranges** of the new keys contain the [ANSUR II](https://ergoassessment.cam/ansur-2-data-explorer/) men's 5th–95th percentiles (US Army body data) with room below for smaller builds; values in the `MeasurementKey` comment. Crotch (হাই) 18–32" (ANSUR 21.6–27.6"), knee 12–22".
- **Genji / T-shirt chart** (`TShirtChart`, S–XXL): shoulder and length from the [Urturms T-shirt chart](https://urturms.com/pages/artax-size-chart) (South Asian brand); body chest = the brand's chest less 0–2" ease, which reproduces the 9.4 design (M 38–40, L 40–42, XL 42–44; Rahat 40/18/28 → L). Size = the chest's size, one up while the shirt's shoulder is narrower than the person's (`suggestTShirtSize`). Bangladeshi brand charts ([Ecstasy](https://ecstasybd.com/?page=sizeGuide), [Easy](https://easyfashion.com.bd/size-guide/)) list chest/length only; a [Bangladeshi study](https://zenodo.org/records/13827292) gives size L as 26.8" long and 17.3" across the shoulder — slightly shorter/narrower than the chart.
- **Women's chart** (`WomenChart`, XS–XXXL, body bust/waist/hip): [Lord & Taylor women's size guide](https://lordandtaylor.com/pages/womens-size-guide). No Bangladeshi kameez chart with hip/waist was found ([Ecstasy](https://ecstasybd.com/?page=sizeGuide) gives kameez bust 36–44 for S–XXL only). Salwar kameez sizes now come from the bust (Farhana 36" → M, as 9.3 shows).
- **Men's chart** (`MenChart`, XS–XXXL, body chest/waist/hip; was `SampleMenShirtChart`): chest from the [ASOS men's shirt chart](https://www.asos.com/discover/size-charts/men/) (XS 34–36 … XXXL 46–48) and waist from the ASOS men's chart (XS 28–30 … XXXL 40–42) — the sample chart already matched both; hip from the [James Perse men's body chart](https://www.jamesperse.com/collections/men) (low hip XS 34 … XXL 44, ±1"), XXXL continued by its 2" step (45–47). ⚠ Western brand charts.
- **Pants size** = the waist number, as ASOS sizes trousers (W26–W44, even; `suggestTrouserSize`: waist 33 → 34). **Pajama size** = the men's chart size from the waist (`suggestSizeByWaist`).
- **Diagrams** (`Diagrams.shirt / pants / pajama`, `diagram_{shirt,pants,pajama}_marks.xml`): drawn by the agent on the existing outlines — shirt = panjabi marks with a shorter length arrow and a biceps arrow; pants / pajama on `body_male_front`, cropped to the lower body like salwar. Not from the design export; replace if the designer draws them.
- Design-phase samples: shirt / pants / pajama reuse Rahat's panjabi values and 2.1's pants waist (34"); the other measurements are "the tailor measures it" — no values are invented.

---

## 4. Current project state (what you need to know)

- Project: `TailorPass` (`settings.gradle.kts`), package **`com.tech_hunt.tailorpass`**, iOS `PRODUCT_NAME=TailorPass`. All Kotlin files in `kmp-resources/` are already in this package (`com/tech_hunt/tailorpass/...`).
- **The current setup is from the earlier (native UI) plan — it must be reversed in Step 0:**
  - `shared` has no Compose now (only the `Greeting`/`Platform` template code). → Add back the Compose Multiplatform plugin, compose compiler and resources.
  - `androidApp/.../App.kt` is a temporary root screen; Compose is added directly to `androidApp`. → `App()` moves to `shared/commonMain/.../App.kt`, and UI code is removed from `androidApp`.
  - `androidApp/src/main/res/values/strings.xml` has only `app_name = TailorPass` (the Android launcher label comes from here — keep it, and add the same to `values-en` too).
  - iOS: `iosApp/iosApp/ContentView.swift` is currently a plain SwiftUI `Text(Greeting().greet())`. → In Step 11 it will show `MainViewController()` (do not touch it now).
  - The KMP template `Greeting`/`GreetingUtil`/`Platform` (`expect fun getPlatform()`) is used by `iosMain/Platform.ios.kt`, `iosTest/SharedLogicIOSTest.kt` and `iosApp/iosApp/ContentView.swift` — see the Step 0 exception below.
- `gradle/libs.versions.toml` already has: `composeMultiplatform = 1.12.1`, `material3` (**currently `1.12.0-alpha03` — not allowed; Step 0 replaces it with the latest stable material3 that is compatible with CMP 1.12.1**), `compose-components-resources`, `compose-uiToolingPreview`, lifecycle-viewmodel/runtime-compose, plugins `composeMultiplatform`, `composeCompiler`.
- AGP 9.1.1, Kotlin 2.4.20, compileSdk 37, minSdk 24. `shared` uses the `com.android.kotlin.multiplatform.library` plugin.

---

## 5. Working rules (every step)

0. **Overview check.** At the start of each step, read the Overview and list which of its features (§3 areas, §4 garments, §5 privacy) the step touches. At the end, confirm those still match the Overview and report anything from it that is missing or conflicts — do not silently drop or invent it.
1. **One step = one session.** When all screens of the step are done, the build passes, and the PNG comparison list has been given, **stop working** — do not start the next step on your own. The user will review and tell you the next step.
2. **Design first:** screens are built in Compose in `shared/commonMain` with sample data. State and sample data use the shared models (`Measurements.kt`, etc.). Each screen is a stateless composable (`XxxScreen(state, onAction)`) + `@Preview` (light, and dark where a design exists).
3. For each screen: first read the HTML (sizes, colors, text), then look at the PNG, then build it. At the end, compare with the PNG and give a **list of differences**.
4. Build each reusable component in the first step that needs it (`shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/ui/components/`); in later steps reuse it instead of building a new one. **Before building any new UI piece, run the reuse checker** — `python3 for-agent/tools/ui_reuse_check.py --screen <id>` (or with the piece's CSS declarations): if the same piece is on another screen (or twice on this one), make it a component now and use it in this step. Pieces the checker finds nowhere else stay private to their screen.
5. Sheets/dialogs (those with an `Overlay`) are not separate routes — they are overlays on the base screen.
6. `Dark` variants (2.2, 5.2, 8.2, 9.2) are not separate screens — the same composable under `MaapTheme(darkTheme = true)`; build a dark `@Preview` for them and match it with the PNG.
7. Navigation from [`navigation.json`](navigation.json) (multiplatform Navigation Compose); navigation between the screens inside a step must work, other destinations may stay placeholders. `navigation.json` (and the "Navigates to" lines in `SCREENS.md`) only contain the HTML links, so these links — taken from each screen's purpose and buttons — must be added too:

   | From | Action | To |
   |---|---|---|
   | `Main` / `HomeEmpty` / `HomeIncomplete` | profile chip | `ProfileSheet` (overlay) |
   | `Main` | garment card (6) | `Method` (garment = card); Genji → `GenjiResult` |
   | `Main` | saved row long-press | `SavedMenuSheet` (overlay); share icon → share PDF/PNG (Step 10.5) |
   | `HomeEmpty` / `HomeIncomplete` | Genji card | `GenjiResult` (not `Method` — genji is size-only) |
   | `Method` | AI card | `Checklist` / `ChecklistFemale` (by gender/garment) |
   | `Method` | tape card | `ManualPrep` (not `ManualEntry`) |
   | `Checklist` / `ChecklistFemale` | এগিয়ে যান (all ticked) | `CameraPermission` if not granted, else `PoseGuide` / `PoseGuideFemale` |
   | `CameraPermission` | অনুমতি দিন / ফিতা দিয়ে মাপুন | `PoseGuide` (female: `PoseGuideFemale`) / `ManualPrep` |
   | `Camera` / `CameraFemale` | photo taken | `CameraSide` |
   | `CameraSide` | photo taken | `PhotoCheckSuccess` or `PhotoCheckProblem` |
   | `PhotoCheckSuccess` | processing done | `Verify` / `VerifySK`; on AI failure → `AIFailed` |
   | `PhotoCheckProblem` | আবার তুলুন | the camera step that failed (front or side) |
   | `AIFailed` | ফিতা দিয়ে মাপুন | `ManualPrep` |
   | `Result` / `ResultSK` | PDF শেয়ার / ছবি হিসেবে শেয়ার | share file (`TailorPDF`/`TailorPDFSK`/`SharePNG` layouts; real share in Step 10.5) |
   | `Settings` | language / unit rows | in-place change (language → `setAppLanguage`) |
   | `Settings` | ব্যাকআপ নিন / রিস্টোর / সব ডেটা মুছুন | `BackupSnackbar` / `RestoreDialog` / `DeleteDialog` (overlays) |
   | `Settings` | গোপনীয়তা নীতি / অ্যাপ সম্পর্কে | `PrivacyPolicy` / `About` |
   | `About` | contact email | `mailto:` via Compose's common `LocalUriHandler` (no new abstraction) |

   `*Dark` targets in `navigation.json` (e.g. `MainDark`, `ResultDark`) are the normal routes under the dark theme, not separate routes. If a link is still unclear, ask.
8. At the end of every step `./gradlew :androidApp:assembleDebug` must pass. iOS build/run is not part of these steps (tested separately) — but the rule of keeping platform APIs out of `commonMain` must still be followed, so it runs on iOS later.

**Prompt to give the agent for each step:**
> "Read for-agent/plan.md and for-agent/PROJECT_OVERVIEW.md. Implement Step <N> — Compose Multiplatform (`shared/commonMain`), verify by running on Android only for now, matching html/ and screens/ exactly. State/models from shared; use the theme, images and string keys from composeResources. No hard-coded strings, no platform APIs in commonMain. After each screen, compare with the PNG and write down the differences. Check that no Overview feature is broken or missed. Stop when the step is finished."

---

## 6. Step-by-step plan

Summary:

| Step | Topic | Screens | Count |
|---|---|---|---|
| 0 | CMP setup and foundation | — | — |
| 1 | First launch | 1.1–1.5 | 5 |
| 2 | Home | 2.1–2.5 | 5 |
| 3 | Start measuring + photo preparation | 3.1–3.2, 4.1–4.4 | 6 |
| 4 | Camera + photo check | 5.1–5.4, 6.1–6.3 | 7 |
| 5 | Tape measurement — A | 7.1–7.5, 7.10 | 6 |
| 6 | Tape measurement — B | 7.6–7.9 | 4 |
| 7 | Review and edit measurements | 8.1–8.5 | 5 |
| 8 | Result + share files | 9.1–9.4, 10.1–10.3 | 7 |
| 9 | Size chart + Settings | 11.1, 12.1–12.6 | 7 |
| 10 | Data and logic + platform parts (camera, AI, share) | — | — |
| 11 | iOS (separately, on a Mac): shell, platform parts, run and verify | all | 52 |

---

### Step 0 — Compose Multiplatform setup and foundation

**Goal:** the common Compose UI runs on Android; prove on a test screen that resources, fonts, theme and strings load.

**Gradle**
- [ ] `shared/build.gradle.kts` (like the chat example): add plugins `composeMultiplatform` + `composeCompiler`; `androidMain.dependencies { api(activity-compose); api(appcompat); api(core-ktx) }`; `commonMain.dependencies`: `compose.runtime/foundation/material3/ui`, `compose.components.resources`, `compose.uiToolingPreview`, lifecycle viewmodel/runtime-compose, multiplatform navigation-compose, `kotlinx-serialization-json` (for routes, + serialization plugin). Keep the iOS targets and framework (`baseName = "Shared"`, `isStatic = true`) as they are — only Android will be built.
- [ ] Enable Android resources on `shared`'s android target (`androidResources { enable = true }` — AGP 9 KMP library), otherwise composeResources will not be found on Android.
- [ ] `compose.resources { publicResClass = false; packageOfResClass = "com.tech_hunt.tailorpass.resources"; generateResClass = always }`.
- [ ] Replace the file-header comment in `shared` ("No UI, no Compose…") with the new rules.
- [ ] `androidApp/build.gradle.kts`: plugins `composeMultiplatform` + `composeCompiler`; dependencies `project(":shared")`, `activity-compose`, `ui-tooling` (debug) — the rest of Compose comes through `shared`.
- [ ] New libraries go in `gradle/libs.versions.toml` (versions compatible with CMP 1.12.1).
- [ ] Replace `material3 = "1.12.0-alpha03"` with the latest **stable** material3 compatible with CMP 1.12.1 (verify on Maven Central / the CMP release notes). If no stable version works with CMP 1.12.1, stop and ask.

**Moving resources and code** (following the table in section 2)
- [ ] `model/`, `util/` → `shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/`.
- [ ] strings (bn/en), 63 drawables, 4 Hind Siliguri TTFs (downloaded — see the font row in §2) → `shared/src/commonMain/composeResources/`.
- [ ] theme (`Color/Type/Dimens/Theme.kt`), `NumberedDiagram.kt`, new `ui/util/Strings.kt` → `shared/.../ui/`.
- [ ] Delete `androidApp/.../App.kt` and create `shared/commonMain/.../App.kt` (`MaapTheme { … }`); `MainActivity` shows that.
- [ ] Delete the template `Greeting`/`Platform`/`GreetingUtil` and **replace** their tests with real ones (e.g. `formatInches`/`toBanglaDigits` tests in `commonTest`, a matching check in `androidHostTest`) — do not just delete them.
- [ ] **One-time iOS exception (Step 0 only):** to remove the template, the agent may delete `shared/src/iosMain/.../Platform.ios.kt`, replace `shared/src/iosTest/.../SharedLogicIOSTest.kt` with a trivial test that does not use the template, and change `iosApp/iosApp/ContentView.swift`'s body to an empty placeholder (`Color.clear`, with a comment pointing to Step 11). Nothing else in `iosMain`/`iosApp`; no iOS build is run. Report iOS as "not verified".

**Platform config**
- [ ] Android manifest: no INTERNET permission; portrait only; `android:label="@string/app_name"` (= TailorPass); `app_name` also in `androidApp/res/values-en/strings.xml`.

**Test screen and base components**
- [ ] `App()` → test screen: Bengali text, one icon, a primary button, `formatInches(24.5f)`, `str("m_chest")` (dynamic key).
- [ ] Base components (needed in almost every step): `TopBar` (back / title / subtitle / trailing chip/icon), `PrimaryButton` (56 pill), `OutlinedPillButton`, `MaapScaffold` (fixed button at the bottom + scrolling content, with safe area).
- [ ] Language: `expect fun setAppLanguage(tag: String)` + `LocalAppLocale` as a common `CompositionLocal` (not `expect object` — see the cheat sheet) (UI in Step 1). Add the missing iOS `actual` to Step 11's list.
- [ ] `./gradlew :androidApp:assembleDebug` passes; run the app and see the test screen.

**⏹ Stop.**

---

### Step 1 — First launch · 5 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 1.1 | Language choice `Language` | [html](html/01_1-1_Language.html) | [png](screens/01_1-1_Language.png) |
| 1.2 | Onboarding — how it works `Onboarding1` | [html](html/02_1-2_Onboarding1.html) | [png](screens/02_1-2_Onboarding1.png) |
| 1.3 | Onboarding — privacy `Onboarding2` | [html](html/03_1-3_Onboarding2.html) | [png](screens/03_1-3_Onboarding2.png) |
| 1.4 | Onboarding — share with tailor `Onboarding3` | [html](html/04_1-4_Onboarding3.html) | [png](screens/04_1-4_Onboarding3.png) |
| 1.5 | Create profile `ProfileCreate` | [html](html/05_1-5_ProfileCreate.html) | [png](screens/05_1-5_ProfileCreate.png) |

**New components:** `OnboardingPage` (illustration + title + bullets + Skip/Next + dot indicator — 1.2–1.4 share the same structure, `HorizontalPager`), `SegmentedToggle` (male/female, feet-inches/cm), `FitCard` (slim/regular/loose), text field style.
**Built in Step 1 for reuse (checker results):** `MaapCard` (16 radius, 1dp #D5DAE1 card — 34 screens), `Modifier.selectableCard` (2dp primary + tint when selected — 1.5, 2.4, 7.6, 7.10, 12.3), `CheckBadge` (22dp — 1.5, 12.3), `IconTile` (icon/number on a colored circle or rounded square — 1.1, 1.2–1.4, 6.3, 12.2, garment tiles), `TextPillButton` (Skip), `SelectCard`, `UnitNumberField`/`MaapTextField`/`LabeledField`, `PageDots`, `MaapScaffold(bottomBarPadding)`, and `localDigits()`/`useBanglaDigits()` in `ui/util/AppLanguage.kt`.
**Note:** height conversion with `feetInchesToCm()`; language choice uses Step 0's `setAppLanguage` + `LocalAppLocale`.

**⏹ Stop.**

---

### Step 2 — Home · 5 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 2.1 | Home `Main` | [html](html/06_2-1_Main.html) | [png](screens/06_2-1_Main.png) |
| 2.2 | Home — dark `MainDark` (dark preview) | [html](html/07_2-2_MainDark.html) | [png](screens/07_2-2_MainDark.png) |
| 2.3 | Home — empty state (Abbu) `HomeEmpty` | [html](html/08_2-3_HomeEmpty.html) | [png](screens/08_2-3_HomeEmpty.png) |
| 2.4 | Switch profile sheet `ProfileSheet` (overlay on Main) | [html](html/09_2-4_ProfileSheet.html) | [png](screens/09_2-4_ProfileSheet.png) |
| 2.5 | Saved measurement menu `SavedMenuSheet` (overlay on Main) | [html](html/10_2-5_SavedMenuSheet.html) | [png](screens/10_2-5_SavedMenuSheet.png) |

**New components:** `BottomNav` (3 tabs, pill indicator — Home/Size chart/Settings; Size chart and Settings are placeholders for now), `ProfileChip`/avatar, `GarmentCard` (grid of 6), `SavedRow` (tap, share icon, long-press), `MaapBottomSheet` (`ModalBottomSheet` style), empty state.
**Note:** 7.9 `HomeIncomplete` will add a card to this same screen in Step 6 — keep the home screen state-driven for that.

**⏹ Stop.**

---

### Step 3 — Start measuring and photo preparation · 6 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 3.1 | Choose method `Method` | [html](html/11_3-1_Method.html) | [png](screens/11_3-1_Method.png) |
| 3.2 | Camera permission `CameraPermission` | [html](html/12_3-2_CameraPermission.html) | [png](screens/12_3-2_CameraPermission.png) |
| 4.1 | Preparation checklist — panjabi `Checklist` | [html](html/13_4-1_Checklist.html) | [png](screens/13_4-1_Checklist.png) |
| 4.2 | Preparation checklist — salwar kameez `ChecklistFemale` | [html](html/14_4-2_ChecklistFemale.html) | [png](screens/14_4-2_ChecklistFemale.png) |
| 4.3 | Pose guide — male `PoseGuide` | [html](html/15_4-3_PoseGuide.html) | [png](screens/15_4-3_PoseGuide.png) |
| 4.4 | Pose guide — female `PoseGuideFemale` | [html](html/16_4-4_PoseGuideFemale.html) | [png](screens/16_4-4_PoseGuideFemale.png) |

**New components:** `MethodCard` (highlighted/normal), `InfoBanner` (tint / orange / green), `ChecklistItem` (checkbox + two lines), tip card.
**Note:** 4.1/4.2 and 4.3/4.4 are the same composable, only the data differs by gender/garment. If not every checklist item is ticked, "এগিয়ে যান" (Continue) is disabled (interactive state). The real OS camera permission prompt is not done yet — UI only.

**⏹ Stop.**

---

### Step 4 — Camera and photo check · 7 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 5.1 | Front photo 1/2 — male `Camera` | [html](html/17_5-1_Camera.html) | [png](screens/17_5-1_Camera.png) |
| 5.2 | Front photo 1/2 — dark `CameraDark` (dark preview) | [html](html/18_5-2_CameraDark.html) | [png](screens/18_5-2_CameraDark.png) |
| 5.3 | Front photo 1/2 — female `CameraFemale` | [html](html/19_5-3_CameraFemale.html) | [png](screens/19_5-3_CameraFemale.png) |
| 5.4 | Side photo 2/2 `CameraSide` | [html](html/20_5-4_CameraSide.html) | [png](screens/20_5-4_CameraSide.png) |
| 6.1 | Photo check — success and processing `PhotoCheckSuccess` | [html](html/21_6-1_PhotoCheckSuccess.html) | [png](screens/21_6-1_PhotoCheckSuccess.png) |
| 6.2 | Photo check — problem `PhotoCheckProblem` | [html](html/22_6-2_PhotoCheckProblem.html) | [png](screens/22_6-2_PhotoCheckProblem.png) |
| 6.3 | Measurement could not be extracted `AIFailed` | [html](html/23_6-3_AIFailed.html) | [png](screens/23_6-3_AIFailed.png) |

**New components:** `CameraOverlay` (dashed body outline — front male/female, side), step pill (1/2), voice-guide toggle, timer chip, `StepProgressBar`, processing step list, reason card.
**Note:** camera screens are always dark (`camera*` colors), no bottom nav. For now a placeholder (dark background) stands in for the camera preview — the real common `CameraPreview(…)` (Kamera; `expect` only as the fallback) comes in Step 10.7 and the AI in Step 10.8, behind `interface BodyMeasurer`. The UI only shows state: guidance banner, step 1/2 → 2/2, processing progress, success, problem (reason), failure. 5.1/5.3/5.4 are the same `CameraScreen` in different states.

**⏹ Stop.**

---

### Step 5 — Tape measurement (A) · 6 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 7.1 | Preparation `ManualPrep` | [html](html/24_7-1_ManualPrep.html) | [png](screens/24_7-1_ManualPrep.png) |
| 7.2 | Measurement list `ManualList` | [html](html/25_7-2_ManualList.html) | [png](screens/25_7-2_ManualList.png) |
| 7.3 | Body measurement — chest `ManualEntry` | [html](html/26_7-3_ManualEntry.html) | [png](screens/26_7-3_ManualEntry.png) |
| 7.4 | Body measurement — sleeve (helper needed) `ManualSleeve` | [html](html/27_7-4_ManualSleeve.html) | [png](screens/27_7-4_ManualSleeve.png) |
| 7.5 | Unusual measurement warning `ManualWarnSheet` (overlay on ManualEntry) | [html](html/28_7-5_ManualWarnSheet.html) | [png](screens/28_7-5_ManualWarnSheet.png) |
| 7.10 | Preference measurement — neck depth (female) `ManualNeckDepth` | [html](html/33_7-10_ManualNeckDepth.html) | [png](screens/33_7-10_ManualNeckDepth.png) |

**New components:** `NumberStepperField` (−/+ 0.5″, large number), `ManualStepRow` (done/now/pending + "সাহায্যকারী লাগবে" (helper needed) badge), collapsible "ভুল এড়াতে" (to avoid mistakes), highlighted diagram, `PresetPictureCard` (3 picture cards — needed in 7.10 and later in 7.6).
**Note:** 7.3/7.4/7.10 are the same `ManualEntryScreen` — the diagram, instructions, helper banner and preset cards change with the measurement key. Outlier limits come from `MeasurementKey`'s `minInches/maxInches`. Number keyboard: `KeyboardOptions(keyboardType = KeyboardType.Decimal)` — check whether "Done" closes the keyboard on iOS.

**⏹ Stop.**

---

### Step 6 — Tape measurement (B) · 4 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 7.6 | Preference measurement — length `ManualLength` | [html](html/29_7-6_ManualLength.html) | [png](screens/29_7-6_ManualLength.png) |
| 7.7 | Summary `ManualSummary` | [html](html/30_7-7_ManualSummary.html) | [png](screens/30_7-7_ManualSummary.png) |
| 7.8 | Leaving midway `ManualExitDialog` (overlay on ManualEntry) | [html](html/31_7-8_ManualExitDialog.html) | [png](screens/31_7-8_ManualExitDialog.png) |
| 7.9 | Home — incomplete measurement `HomeIncomplete` | [html](html/32_7-9_HomeIncomplete.html) | [png](screens/32_7-9_HomeIncomplete.png) |

**Carried over from Step 5 (user decisions, 2026-10-18):**
- [x] **Do in Step 6:** English `manual_next` "Next measurement" → "Next" (the long label clips the arrow in the 1.5-wide button on 7.3–7.10).
- [ ] **Later (not Step 6):** cm entry in the tape flow. The 7.1 inch/cm toggle is stored in `ManualState.unit`, but the entry screens (7.3–7.10) show and step in inches only (hint/stepper strings say "আধা ইঞ্চি", `outlier_body` uses `"`). Decide whether entry follows the chosen unit (needs cm strings and a cm step) or the unit only affects display (Result/Summary).
- [ ] **Later:** tape how-to text, diagrams and "ভুল এড়াতে" tips for chest (7.3 shows the panel; no chest tips exist yet) and for neck, shoulder, hip, bust, armhole and the salwar measurements. Only chest/belly/sleeve/neck depth have resources now (`ManualPlan.guide`).
- [ ] **Later:** replace Hind Siliguri with Roboto (user decision) and delete the Hind Siliguri TTFs. Reason: the bundled Hind Siliguri regular/medium/semibold draw the Bengali digit "১" without its upper stroke (bold is fine). ⚠ Before doing it, verify: Roboto has no Bengali glyphs of its own (Android falls back to the system Bengali font), but the PDF/PNG must use a bundled font and render conjuncts — a Bengali companion font (e.g. Noto Sans Bengali) may be needed alongside Roboto. Also update `CLAUDE.md` §4, `ResourceKeysTest.fontsAreBundled` and the §2 font row here.

**Found in the Step 6 device check (2026-10-18):**
- [x] **Fixed in Step 6:** a preference step without a value now starts at its suggested preset — the middle card, as the designs show (length "হাঁটু পর্যন্ত" 42″, neck depth medium 7″; `ManualPlan.suggestedPreset`). It is saved only on "next"; "পরে দেব" keeps it missing. Overview §3 "with a suggested default".
- [ ] **Decide:** confirm "middle card" as the default rule, or give a default per garment/preset (`Measurements.kt` marks none). Step 10 also computes length values from the profile's height (now the 5′7″ samples).
- [ ] **Decide (needs data):** Overview §3 lists choice cards for **cuff** (হাতার মুখ), but `Measurements.kt` has no cuff presets, so cuff is a plain number entry on 7.3-style screens. Provide the cuff options (labels, values, pictures) or confirm number entry.
- [ ] **Decide:** 7.8 `ConfirmDialog` is vertically centred (~60dp lower than the HTML's fixed `top:210px`), and its scrim is the platform dialog dim (darker grey) instead of `rgba(13,17,23,.55)`. Match the HTML exactly, or keep centring (safer on other screen heights; the same dialog is reused in 12.3/12.4).
- [ ] **Decide:** the 7.9 design is inconsistent with 2.1 — it lists only 2 saved rows under "৪টি" (the app shows all 3 + the card, scrolling) and draws "দর্জিকে পাঠানো" grey without the green ✓ that 2.1 has. Kept the 2.1 style and the full list; confirm.
- [ ] **Decide:** in English the 7.8 dialog's "Later" (`পরে করব`) and the entry screen's "Later" (`পরে দেব`) read the same; Bengali uses two words. Keep, or rename one (e.g. dialog "Finish later").
- [ ] **Note (no action):** 7.7 rows follow the HTML's `min-height:48px`; the PNG rows look ~53dp only because of the fallback font's line height (§2 font note).

**New components:** `ConfirmDialog`, summary row (edit icon; if missing, an orange row + "এখন মাপুন" (Measure now)), `IncompleteCard` (orange left border, 4/8 progress).
**Note:** 7.6 uses Step 5's `PresetPictureCard` (`LengthPreset`, values from height). 7.9 is not a new screen — it is the incomplete-card state of Step 2's home. 7.8 uses `BackHandler` to catch back (Android back / iOS swipe).

**⏹ Stop.**

---

### Step 7 — Review and edit measurements · 5 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 8.1 | Verify measurements — panjabi `Verify` | [html](html/34_8-1_Verify.html) | [png](screens/34_8-1_Verify.png) |
| 8.2 | Verify measurements — dark `VerifyDark` (dark preview) | [html](html/35_8-2_VerifyDark.html) | [png](screens/35_8-2_VerifyDark.png) |
| 8.3 | Edit measurement sheet `EditSheet` (overlay on Verify) | [html](html/36_8-3_EditSheet.html) | [png](screens/36_8-3_EditSheet.png) |
| 8.4 | Verify measurements — salwar kameez `VerifySK` | [html](html/37_8-4_VerifySK.html) | [png](screens/37_8-4_VerifySK.png) |
| 8.5 | Verify measurements — measured with tape `VerifyManual` | [html](html/38_8-5_VerifyManual.html) | [png](screens/38_8-5_VerifyManual.png) |

**Found in Step 7 (2026-10-18):**
- [ ] **Decide:** 8.1 shows panjabi length and cuff with AI accuracy badges, but `Measurements.kt` marks them as preference measurements (not AI-measurable) and 8.4 shows them as "✎ আপনার পছন্দ". The sample keeps the 8.1 badges; Step 10 should show preferences as "your choice" after photos too — confirm.
- [ ] **Decide:** a value changed on the edit sheet becomes "ফিতায় মাপা" (body) or "✎ আপনার পছন্দ" (preference); the design shows no badge after editing. Confirm.
- [ ] **Decide:** edits made on Verify are not copied back to the tape session — going back to the summary (7.7) and on to Verify again rebuilds the list from the tape values. Fine for the design phase; Step 10's ViewModel keeps one measurement.
- [ ] **Note:** the edit sheet shows a diagram/how-to only for keys with tape resources (chest, belly, sleeve, neck depth — see the Step 6 "Later" item); other keys show only the number field.

**New components:** `MeasurementRow` + `SourceBadge` badge (● high / ◐ medium / ▲ low / ✎ your preference / measured with tape / skipped), section header (kameez 10 / salwar 4), tailor's note field.
**Note:** all five are the same `VerifyScreen` — only the data and badge sources differ. In 8.4 the list scrolls and "ফলাফল দেখুন" (See result) stays fixed at the bottom (`Scaffold(bottomBar)` + `LazyColumn`). The edit sheet reuses `NumberStepperField` (Step 5).

**⏹ Stop.**

---

### Step 8 — Result and share files · 7 designs

| # | Screen | HTML | PNG |
|---|---|---|---|
| 9.1 | Result — panjabi `Result` | [html](html/39_9-1_Result.html) | [png](screens/39_9-1_Result.png) |
| 9.2 | Result — dark `ResultDark` (dark preview) | [html](html/40_9-2_ResultDark.html) | [png](screens/40_9-2_ResultDark.png) |
| 9.3 | Result — salwar kameez `ResultSK` | [html](html/41_9-3_ResultSK.html) | [png](screens/41_9-3_ResultSK.png) |
| 9.4 | Genji size `GenjiResult` | [html](html/42_9-4_GenjiResult.html) | [png](screens/42_9-4_GenjiResult.png) |
| 10.1 | PDF — panjabi, A4 `TailorPDF` (794×1123) | [html](html/43_10-1_TailorPDF.html) | [png](screens/43_10-1_TailorPDF.png) |
| 10.2 | PDF — salwar kameez, A4 `TailorPDFSK` (794×1123) | [html](html/44_10-2_TailorPDFSK.html) | [png](screens/44_10-2_TailorPDFSK.png) |
| 10.3 | Share as image, PNG `SharePNG` (1080×2600) | [html](html/45_10-3_SharePNG.html) | [png](screens/45_10-3_SharePNG.png) |

**Found in Step 8 (2026-10-18):**
- [x] **Resolved 2026-10-18:** 9.3's size now comes from the bust and the women's chart; 9.4 uses the T-shirt chart (chest + shoulder) — see §3 "Garment data sources". Genji still shows the design values (40/18/28) for every profile until Step 10.
- [ ] **Decide:** the 9.3 design has no inch/cm toggle (only 9.1 does). Salwar kameez follows the design; cm for it comes from the Settings unit row (Step 9). Add the toggle to 9.3, or keep.
- [ ] **Decide:** file markers — "*" marks "your choice" values (10.2), a low-accuracy value gets "* ফিতায় মিলিয়ে নিন" under its row on the full layout (10.1) and is named in the footer on the compact layout (10.2) and the PNG (10.3). Panjabi length/cuff carry AI badges in the sample (Step 7 item), so 10.1 stars nothing, as designed.
- [ ] **Step 10.5:** the compact PDF (10.2) fills the A4 page exactly with Hind Siliguri; a long tailor note can push the footer off. Limit the note length, shrink, or add a second page. The PNG height follows its content (the 10.3 frame is 2600; a wrapped note makes it taller).
- [ ] **Note:** the PDF/PNG logo is `app_logo` (filled blue tile), not the design's outline tape mark. The salwar diagram crop of `body_female_front` shows the hands (the 10.2 design draws legs only).
- [ ] **Note:** new results are dated with the design date (`ResultState.SampleDate`) until Step 10 stores real dates; fit is "Regular" (profiles have no stored fit yet).

**New components:** use `NumberedDiagram` (provided), suggested size card (`suggestSize()`), numbered measurement list, inch/cm `SegmentedToggle` (`inchesToCmText()`), kameez/salwar tabs, size comparison (M|L|XL), "সেভ হয়েছে" (Saved) chip.
**Note:** 9.1/9.2/9.3 are the same `ResultScreen` (with bottom nav). 10.x are not app screens — they are the files to share. Build the file layouts as common Compose composables (`TailorSheetPdfPage`, `TailorSharePng`) + match them with the PNGs via previews; PNG rendering is common (`GraphicsLayer.toImageBitmap()`, 1080 px wide). PNG/PDF file writing and the share sheet are **not** done in this step — they come in Step 10.5 (`ImageEncoder`, `PdfRenderer`, `FileSharer`); here the share buttons only call `onAction`.

**⏹ Stop.**

---

### Step 9 — Size chart and Settings · 7 screens

| # | Screen | HTML | PNG |
|---|---|---|---|
| 11.1 | Size chart `SizeChart` | [html](html/46_11-1_SizeChart.html) | [png](screens/46_11-1_SizeChart.png) |
| 12.1 | Settings `Settings` | [html](html/47_12-1_Settings.html) | [png](screens/47_12-1_Settings.png) |
| 12.2 | Backup succeeded `BackupSnackbar` (overlay on Settings) | [html](html/48_12-2_BackupSnackbar.html) | [png](screens/48_12-2_BackupSnackbar.png) |
| 12.3 | Restore confirmation `RestoreDialog` (overlay on Settings) | [html](html/49_12-3_RestoreDialog.html) | [png](screens/49_12-3_RestoreDialog.png) |
| 12.4 | Delete all data — confirmation `DeleteDialog` (overlay on Settings) | [html](html/50_12-4_DeleteDialog.html) | [png](screens/50_12-4_DeleteDialog.png) |
| 12.5 | Privacy policy `PrivacyPolicy` | [html](html/51_12-5_PrivacyPolicy.html) | [png](screens/51_12-5_PrivacyPolicy.png) |
| 12.6 | About the app `About` | [html](html/52_12-6_About.html) | [png](screens/52_12-6_About.png) |

**Found in Step 9 (2026-10-18):**
- [x] **Resolved 2026-10-18:** the women's tab shows the women's chart (bust/waist/hip) and the genji chip the T-shirt chart (chest/shoulder/length) — §3 "Garment data sources". Shirt, panjabi and pants chips still show the sample men's chart.
- [ ] **Decide:** in cm (Settings unit), the size chart rounds ranges to whole cm ("১০২–১০৭"); decimals do not fit the highlighted row. Result keeps one decimal.
- [ ] **Decide:** the language and unit rows toggle in place on tap (two options each, no picker in the design). Confirm, or design a picker sheet.
- [ ] **Decide:** 12.3 / 12.4 dialogs are centred like 7.8 (the HTML pins them at `top:120px` / `top:150px`) — same open item as Step 6.
- [ ] **Decide:** the replace option on 12.3 shows `restore_replace_warning` in an orange note (the design has the string but no drawing of it).
- [ ] **Step 10:** backup / restore only show their overlays (sample file `tailorpass-backup-2026-10-10.json`, 3 profiles, 5 measurements); "delete all" clears the sample measurements and opens profile creation with an empty back stack. Real files (FileKit), real deletion, the stored unit/language and the last-backup date come in Step 10. On Android a language change recreates the activity, so design-phase sample state resets — ViewModels fix this.
- [ ] **Note:** the size chart's "your" row uses the design chest (40") for any profile with saved measurements; Step 10 reads the profile's latest chest. App version "1.0" is a constant (`SettingsState.APP_VERSION`).

**New components:** size table (user's row highlighted "আপনার" (Yours)), garment chips, `SettingsRow`/section header, green snackbar, radio option cards (merge/replace, with a warning on replace), type-to-confirm ("মুছুন" (Delete)) red button, numbered policy sections.
**Note:** Step 2's `BottomNav` placeholder tabs are replaced with the real screens here. Language switching via `setAppLanguage` + `LocalAppLocale`. The "মাপের একক" (Unit) row switches inch/cm for the whole app. There is **no theme row** in the design: light/dark follows the system (`isSystemInDarkTheme()`). The About screen shows the app name from `app_name` (TailorPass). `about_contact_email` is a placeholder.

**⏹ Stop. — The design phase ends here (52 designs, the same code on both platforms).**

---

### Step 10 — Data and logic + platform parts

(Only after the design phase — Steps 0–9 finished and tested with sample data.)

**From here on, follow [`KMP + CMP Technical guideline.en.md`](KMP%20+%20CMP%20Technical%20guideline.en.md)** — libraries, platform abstractions, structure additions, privacy and testing rules. Step 10 runs as its sub-steps **10.1–10.9** (guideline §8), **one sub-step per session**.

Summary of the decisions it fixes:
- [ ] **Data:** Room KMP (bundled SQLite) — `Profile(name, gender, heightCm, fit)`, `MeasurementSet(profileId, garment, date, method AI|TAPE, unit, note, status COMPLETE|INCOMPLETE)` + one `MeasurementValue(setId, key, valueCm, source)` row per measurement (incl. skipped ones). Storage unit: **cm (`Double`)**; `Measurements.kt` stays unchanged (inches), conversion in `util/`. Database path via `expect` function.
- [ ] Repositories + one `ViewModel` per flow (`commonMain`, multiplatform lifecycle-viewmodel, `StateFlow` → `collectAsStateWithLifecycle()`). Because the UI lives in one place, iOS will not need SKIE/NativeCoroutines. DI: Koin.
- [ ] Settings (language, display unit inch/cm, whether onboarding was seen): DataStore Preferences KMP. Theme is not stored — it follows the system (no theme setting in the design).
- [ ] Backup/restore = JSON file (kotlinx.serialization); merge vs replace; file pick/save with **FileKit** (common).
- [ ] Remove the sample data and connect the screens to real ViewModels.
- [ ] **Camera:** Kamera 1.2 inside a common `CameraPreview(…)`, only after passing the guideline's verification gate (privacy, live-frame speed); otherwise own `expect` CameraPreview (CameraX / AVFoundation). OS camera permission via `CameraPermission`. **Approval gate:** before writing any camera code (the Kamera spike, `CameraPreview`, capture, frame analysis), the agent first sends the user a written description — approach, library + version, files and source sets, how photos stay private and get deleted, how the gate is tested, and the fallback — and waits for an explicit go-ahead. The same applies again before switching to the `expect` fallback.
- [ ] **AI measurement:** `interface BodyMeasurer` (landmarks + mask only) — Android: MediaPipe Tasks Vision; all measurement math in `commonMain/measure/`. Delete photos once measuring is done.
- [ ] Share: PDF (A4) and PNG — from Step 8's common layouts via `ReportLayoutBuilder`, `PdfRenderer`, `ImageEncoder`, `FileSharer` (Android implementations for now).
- [ ] Note: every `expect` and platform interface needs an iOS implementation later — add it to Step 11's list.

**⏹ Stop.**

---

### Step 11 — iOS (separately, requires a Mac)

The UI is already common — no new UI is written here; the iOS shell, iOS `actual` parts, then run and fix. Follow the [technical guideline](KMP%20+%20CMP%20Technical%20guideline.en.md) (§5 Swift bridge, §6 `iosApp` files, §8 Step 11).

- [ ] `shared/src/iosMain/.../MainViewController.kt`: `fun MainViewController() = ComposeUIViewController { App() }`; `iosApp/iosApp/ContentView.swift` → `UIViewControllerRepresentable` (like the chat example's `ComposeViewControllerToSwiftUI.swift`) + `.ignoresSafeArea()`.
- [ ] iOS implementations for every `expect` and platform interface: language switch (`actual fun setAppLanguage(tag)` in `iosMain/.../ui/util/AppLanguage.ios.kt` — `NSUserDefaults` `AppleLanguages` + UI refresh; added in Step 0, no iOS `actual` yet), `BodyMeasurer` (MediaPipe via `MediaPipePoseBridge.swift`, `MediaPipeTasksVision` pod — CocoaPods only in `iosApp`, open the `.xcworkspace`), camera (only if the `expect` CameraPreview fallback was taken: AVFoundation `UIKitView`), `CameraPermission`, PDF (`UIGraphicsPDFRenderer`), PNG encode, share (`UIActivityViewController`), `VoicePlayer` (`AVAudioPlayer`), database/DataStore path.
- [ ] Xcode 15+ / iOS 16+; check that `./gradlew :shared:embedAndSignAppleFrameworkForXcode` is correct in the `iosApp` build phase; run on the simulator and a real device.
- [ ] `Info.plist` / `InfoPlist.xcstrings`: `CFBundleDisplayName` = TailorPass, bn/en localization (Development Language Bengali), camera usage, portrait only, `CADisableMinimumFrameDurationOnPhone = true` (`UIAppFonts` not needed).
- [ ] For each screen (in the same groups as Steps 1 → 9), compare iOS with the PNG and Android and list the differences: safe area/notch, keyboard insets, Bengali font rendering, sheet/dialog behavior, back-swipe, language switching.
- [ ] Test the iOS `actual` parts on a real device.

---

## 7. Open items for the user (review list, 2026-10-18)

All open "missing" and "decide" items from Steps 5–9 and the garment-data work, in one place. The user reviews them later; the agent does not resolve them on its own. Tick an item (or write the answer next to it) when decided — the step notes above keep the details.

### A. Missing data / confirmation needed
- [ ] **Bangla terms not found in any source:** মাসল (Biceps), ভেতরের লম্বা (Inseam), মোট লম্বা (Full length). Confirm with a tailor. (রান, হাঁটু, হাই, মুহুরি are sourced — §3.)
- [ ] **Size charts are brand charts, not Bangladeshi:** men's (`MenChart`, ASOS + James Perse; XXXL hip 45–47 extended by the agent), women's (`WomenChart`, Lord & Taylor, US), genji (`TShirtChart`, Urturms; body chest = brand chest less 0–2" ease). Confirm or replace with a tailor / local brand chart (Overview §7).
- [ ] **Shirt / pants / pajama diagrams** were drawn by the agent on existing outlines (`diagram_{shirt,pants,pajama}_marks.xml`). Replace if the designer draws them. On the shirt, badge 7 (biceps) covers its short arrow.
- [ ] **Cuff choice cards:** Overview §3 promises picture cards for cuff, but there are no cuff presets — cuff is a number entry now. Give the options (labels, values, pictures) or confirm number entry. (Step 6)
- [ ] **Tape how-to text, diagrams and "ভুল এড়াতে" tips** exist only for chest, belly, sleeve and neck depth. Missing for neck, shoulder, hip, bust, armhole, salwar measurements and the new keys (biceps, thigh, knee, হাই, inseam, full length). (Step 5)
- [ ] **Placeholders:** contact email (`about_contact_email`), app icon/logo (`app_logo` is still the "মাপ" design?), blue/green brand choice, privacy policy text matched to the final behaviour.
- [ ] **Plausible ranges** (unusual-value warning): the new keys are checked against ANSUR II (§3); the older ones are still rough starting values. Fit allowances per garment are not defined (Overview §7).
- [ ] **Voice-guide clips** (Bangla / English) still to be recorded (Overview §7, Step 10).

### B. Decisions
- [ ] **Default preference card:** keep "middle card" (length at knee, neck depth medium) or set a default per garment. (Step 6)
- [ ] **Dialog position and scrim** (7.8, 12.3, 12.4): centred with the platform dim (now) or fixed at the HTML's `top` with `rgba(13,17,23,.55)`. (Steps 6, 9)
- [ ] **7.9 Home:** keep the full saved list and the green ✓ of 2.1, or follow the 7.9 design (2 rows, grey). (Step 6)
- [ ] **English "Later":** the 7.8 dialog (`পরে করব`) and the entry screen (`পরে দেব`) both read "Later" — keep or rename one (e.g. "Finish later"). (Step 6)
- [ ] **Preferences after photos:** show panjabi length/cuff as "✎ your choice" (like 8.4) instead of the AI badges in 8.1. (Step 7)
- [ ] **Badge after an edit** on the edit sheet: "ফিতায় মাপা" / "✎ আপনার পছন্দ" (now) or no badge. (Step 7)
- [ ] **9.3 inch/cm toggle:** add it to salwar kameez like 9.1, or keep the design (unit from Settings). (Step 8)
- [ ] **File markers** on the PDF/PNG: "*" for "your choice", "* ফিতায় মিলিয়ে নিন" for low accuracy — confirm the rules. (Step 8)
- [ ] **Size chart in cm:** ranges rounded to whole cm (now) or one decimal. (Step 9)
- [ ] **Language / unit rows:** toggle on tap (now) or a picker sheet. (Step 9)
- [ ] **12.3 replace warning:** the orange note under the options (now) — confirm the look. (Step 9)
- [ ] **cm entry in the tape flow:** entry follows the chosen unit (needs cm strings and step) or the unit only affects display. (Step 5)
- [ ] **Font:** switch Hind Siliguri → Roboto + a bundled Bengali font (e.g. Noto Sans Bengali) to fix the "১" glyph — verify PDF/PNG conjuncts first. (Step 5)
- [ ] **Long tailor note** on the compact PDF (10.2): limit the length, shrink, or add a second page. (Step 8, for 10.5)
- [ ] **Garment rules chosen by the agent:** genji size = chest size, one up while the shoulder is wider (length ignored); pants size = waist number (W26–W44), pajama = letter size from the waist; shirt length = number entry without picture cards; হাই = full crotch (navel → between the legs → back waist). Confirm. (§3)
- [ ] **Bookkeeping:** tick the Step 0–9 checklists in §6 (the work is done; boxes were never ticked).

---

## 8. File index (`for-agent/`, next to this file)

| File/folder | Contents |
|---|---|
| [`README.md`](README.md) | Package index |
| [`PROJECT_OVERVIEW.md`](PROJECT_OVERVIEW.md) | Product scope — **read every step**: users, v1 features, garments/measurements, privacy promise, roadmap, open decisions |
| [`KMP + CMP Technical guideline.en.md`](KMP%20+%20CMP%20Technical%20guideline.en.md) | Technical guideline for Steps 10–11 (after the design phase) |
| [`SCREENS.md`](SCREENS.md) | Purpose, navigation and all text of the 52 screens |
| [`screens.json`](screens.json) | The same information, machine-readable |
| [`navigation.json`](navigation.json) | Navigation graph |
| [`design-tokens.json`](design-tokens.json) | Colors (light/dark/alternative brand), type, sizes |
| [`maap-design-screens.pdf`](maap-design-screens.pdf) | All screens together (A4 landscape, 21 pages) |
| [`html/`](html/) | Exact HTML/CSS for each screen — `01_1-1_Language` … `52_12-6_About` |
| [`screens/`](screens/) | PNG (2×) for each screen, same names |
| [`kmp-resources/README.md`](kmp-resources/README.md) | Description of the resources (old native-UI plan — only the file list applies; conversion in §2) |
| [`kmp-resources/FONTS.md`](kmp-resources/FONTS.md) | Downloading and naming the Hind Siliguri fonts |
| [`kmp-resources/shared/`](kmp-resources/shared/) | Models, diagrams, Bengali digits, gradle snippet |
| [`kmp-resources/androidApp/`](kmp-resources/androidApp/) | drawables, strings (bn/en), Compose theme, diagram — go to `shared/commonMain` in Step 0 |
| [`kmp-resources/iosApp/`](kmp-resources/iosApp/) | InfoPlist.xcstrings and the Info.plist snippet will be used; Assets/Localizable/Theme are reference only |
