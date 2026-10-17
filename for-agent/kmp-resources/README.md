# Maap (মাপ) — Kotlin Multiplatform (KMP) Resources

> ⚠ **Outdated — this README was written for the native-UI plan (Android Compose + iOS SwiftUI).** The project is now **Compose Multiplatform**: all UI and resources live in `shared/src/commonMain` (`composeResources/`, `Res.*`). The file list below and the "one key" idea are still correct; but `R.*`/`getIdentifier`/`res/raw/keep.xml`, copying into `res/`, `shared/build.gradle.kts.snippet`, and all SwiftUI/`Assets.xcassets`/`Localizable.xcstrings`/`UIAppFonts` steps **no longer apply**. The app name is now "TailorPass" (already changed in `strings.xml` and `InfoPlist.xcstrings`). For which file goes where and how, follow only the table in [`../plan.md`](../plan.md) §2.

Assumed: **the shared module holds only logic** (Kotlin), **the UI is native** — Jetpack Compose on Android, SwiftUI on iOS.
So the resources come in three parts:

```
shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/   ← one codebase for both platforms (no Compose/R/Res)
├── model/Measurements.kt   measurement lists, order, preference measurements, normal ranges, garments, size chart
├── model/Diagrams.kt       numbered-diagram data (image name + number coordinates)
└── util/BanglaDigits.kt    Bengali digits, ২৪½ formatting, inch↔cm
shared/build.gradle.kts.snippet   framework name "Shared"

androidApp/src/main/
├── res/drawable/           63 VectorDrawables (icons, logo, body, diagrams, illustrations)
├── res/font/               Hind Siliguri — put the 4 .ttf files here, see FONTS.md
├── res/values/strings.xml  Bengali (default) — 339 strings
├── res/values-en/strings.xml  English — same keys
└── java/com/tech_hunt/tailorpass/
    ├── ui/theme/           Color, Type (R.font), Dimens, Theme → MaapTheme { }
    ├── ui/diagram/NumberedDiagram.kt
    └── util/Strings.kt     shared key → R.string / R.drawable

iosApp/
├── Resources/Assets.xcassets/
│   ├── Icons/              63 SVG imagesets (same names; the icons are templates — they can be tinted)
│   └── Colors/             37 colorsets, each with a light + dark value
├── Resources/Localizable.xcstrings   Bengali + English, key = the Android name
├── Resources/InfoPlist.xcstrings     app name "মাপ", camera permission text
├── Theme/MaapColors.swift  Color.Maap.primary …
├── Theme/MaapFonts.swift   MaapFont.bodyMedium … (with Dynamic Type)
├── Theme/MaapDimens.swift  spacing, heights, corner radii + primary/outlined button styles
├── Theme/L10n.swift        L10n.t("m_chest")
├── Theme/NumberedDiagram.swift
└── Info.plist.snippet.xml  UIAppFonts, bn/en, camera, portrait only

design-tokens.json          all colors/type/spacing in one place (for verification or other tools)
```

## Core idea: one key, two platforms

The shared code contains no text or images, only **names**. The same name exists in both places:

| In shared | Android | iOS |
|---|---|---|
| `MeasurementKey.Chest.labelKey` = `"m_chest"` | `str("m_chest")` → `R.string.m_chest` | `L10n.t("m_chest")` |
| `Garment.Panjabi.iconName` = `"ic_garment_panjabi"` | `painterResource(ctx.drawableId(…))` | `Image("ic_garment_panjabi")` |
| `Diagrams.panjabi` | `NumberedDiagram(Diagrams.panjabi)` | `NumberedDiagram(spec: Diagrams.shared.panjabi)` |
| `formatInches(24.5f)` → `"২৪½"` | directly | `BanglaDigitsKt.formatInches(value: 24.5, useBangla: true)` |

Use the UI's own text (buttons, titles) directly: on Android `stringResource(R.string.action_share)`, on iOS `Text("action_share")` or `L10n.t("action_share")`.

## Setup steps

**shared**
1. Copy the `shared/src/commonMain/...` files into your shared module; the package is `com.tech_hunt.tailorpass` (already changed).
2. In `build.gradle.kts`, set the framework `baseName = "Shared"` (if you use a different name, change Swift's `import Shared`).

**Android**
1. Copy everything in `res/` into `androidApp/src/main/res/`, and the `java/...` files into your package.
2. Download the fonts and put them in `res/font/`, with lowercase names: `hind_siliguri_regular.ttf` etc. (FONTS.md).
3. Wrap the app: `MaapTheme { App() }`.
4. If resource shrinking is on in the release build, resources looked up with `getIdentifier` may be removed — that is why `res/raw/keep.xml` is provided:
   `<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="@string/m_*,@string/garment_*,@string/accuracy_*,@string/badge_*,@string/length_*,@string/depth_*,@string/fit_*,@string/gender_*,@string/manual_*,@drawable/*" />`

**iOS** (Xcode 15+, iOS 16+)
1. Drag `Assets.xcassets`, `Localizable.xcstrings` and `InfoPlist.xcstrings` from `Resources/` into the iosApp target (Copy items ✓).
   If you already have an Assets.xcassets, put the two folders `Icons/` and `Colors/` inside it.
2. Add `Theme/*.swift` to the target.
3. Add the 4 font .ttf files to the target (Target Membership ✓) and put the contents of `Info.plist.snippet.xml` into Info.plist.
4. In Project → Info → Localizations, add Bangla (bn) and make Bengali the Development Language.

## Usage examples

```kotlin
// Android (Compose)
Icon(painterResource(R.drawable.ic_share), stringResource(R.string.action_share), tint = MaterialTheme.colorScheme.primary)
Text(formatInches(24.5f) + "\"", style = MaapTheme.numbers.listValue)          // ২৪½"
Text(str(MeasurementKey.Chest.labelKey))                                       // বুক (Chest)
NumberedDiagram(Diagrams.panjabi, Modifier.size(134.dp, 168.dp))
```

```swift
// iOS (SwiftUI)
Image("ic_share").foregroundStyle(Color.Maap.primary)
Text(BanglaDigitsKt.formatInches(value: 24.5, useBangla: true) + "\"").font(MaapFont.numberList)
Text(L10n.t(MeasurementKey.chest.labelKey)).font(MaapFont.titleSmall)
NumberedDiagram(spec: Diagrams.shared.panjabi).frame(width: 134)
Button(L10n.t("action_save")) { … }.buttonStyle(MaapPrimaryButtonStyle())
```

## Rules followed in the design

- Body text ≥ 16, anything tappable ≥ 48, primary button 56 high and full width.
- Only one primary (filled) button per screen; the second one is outlined or a link.
- Color never carries meaning alone — badges also have symbols (● high, ◐ medium, ▲ low).
- Numbers in Bengali digits; in English, `useBangla = false`.
- Dark mode: on Android `MaapTheme(darkTheme = …)`; on iOS the colorsets switch by themselves.
- Camera screens are dark in both modes (`camera*` colors).
- "Scrolls, but the button is always visible":
  Android — `Scaffold(bottomBar = { button })` + `LazyColumn`;
  iOS — `ScrollView { … }.safeAreaInset(edge: .bottom) { button }`.

## Still to be decided

- `about_contact_email` — the real email.
- `SampleMenShirtChart`, the measurements' `minInches/maxInches`, sample values for length/neck depth — with real data.
- Privacy policy text — checked against how the app really works.
- Blue/green option: on iOS the `PrimaryGreen` and `PrimaryGreenContainer` colors are provided; if green is chosen, switch `Color.Maap.primary` to them.
