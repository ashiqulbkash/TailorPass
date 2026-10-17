# TailorPass — বাস্তবায়ন পরিকল্পনা (Compose Multiplatform · Android + iOS)

> কোডিং এজেন্ট: **কাজ শুরুর আগে এই ফাইলটি পুরো পড়ুন।** এটি আগের `AGENT_GUIDE.md`-এর জায়গা নিয়েছে — সেই ফাইলের সব নির্দেশনা এখানে আছে (অংশ ২–৬)।
> Coding agent: read this whole file first. It replaces `AGENT_GUIDE.md`; every rule from that guide is kept below.

---

## ১. অ্যাপের ওভারভিউ

**TailorPass** — বাংলাদেশের মানুষের জন্য সম্পূর্ণ অফলাইন পোশাকের মাপ নেওয়ার অ্যাপ।

> **অ্যাপের নাম:** দুই ভাষাতেই **"TailorPass"** (`app_name`, iOS `CFBundleDisplayName`, `onb_welcome` = "TailorPass-এ স্বাগতম" / "Welcome to TailorPass" — `kmp-resources`-এ আগেই বদলানো)। ডিজাইনের HTML/PNG-তে পুরোনো নাম **"মাপ" / "Maap"** আছে — সেখানে নামটা বাদে বাকি সব হুবহু মেলাবেন; নামের পার্থক্য তুলনা-তালিকায় লিখতে হবে না। কোডের ভেতরের পুরোনো নাম (`MaapTheme`, `Dimens` ইত্যাদি) যেমন আছে তেমনই থাকবে।

- **কী করে:** একটি প্রোফাইলের (নাম, লিঙ্গ, উচ্চতা, ফিট পছন্দ) জন্য পোশাকের মাপ নেয় — দুই উপায়ে:
  1. **AI ছবি দিয়ে:** সামনে ও পাশ থেকে দুটি ছবি → ফোনেই (on-device) মাপ বের হয়, প্রতিটি মাপে নির্ভুলতার ব্যাজ (● উচ্চ / ◐ মাঝারি / ▲ নিম্ন)।
  2. **ফিতা দিয়ে নিজে:** ছবি/ডায়াগ্রাম দেখে একটি একটি করে মাপ লেখা; পছন্দের মাপ (ঝুল, গলার গভীরতা) ছবি-কার্ড থেকে বাছাই।
- **তারপর:** মাপ যাচাই ও এডিট → ফলাফল (নম্বরসহ ডায়াগ্রাম + সাজেস্ট করা রেডিমেড সাইজ) → দর্জিকে **PDF (A4)** বা **ছবি (PNG)** হিসেবে শেয়ার।
- **পোশাক:** শার্ট, প্যান্ট, পাঞ্জাবি, পায়জামা (ছেলেদের), সালওয়ার কামিজ (মেয়েদের), গেঞ্জি (শুধু সাইজ সাজেশন)।
- **অন্যান্য:** একাধিক প্রোফাইল, সংরক্ষিত মাপের তালিকা, অসমাপ্ত মাপ পরে চালিয়ে যাওয়া, সাইজ চার্ট, ব্যাকআপ/রিস্টোর (JSON ফাইল), সব ডেটা মুছে ফেলা, বাংলা/ইংরেজি, লাইট/ডার্ক মোড।
- **গোপনীয়তা:** ইন্টারনেট পারমিশন নেই; ছবি মাপ বের হলেই মুছে যায়; সব ডেটা শুধু ফোনে।
- **ভাষা:** ডিফল্ট বাংলা, দ্বিতীয় ইংরেজি। সংখ্যা বাংলা অঙ্কে।

### প্ল্যাটফর্ম ও কৌশল — **Compose Multiplatform (CMP)**
পুরো UI **একবারই** Compose-এ লেখা হবে (`commonMain`), এবং সেই একই UI Android ও iOS দুই জায়গায় চলবে। **SwiftUI-তে আলাদা UI বানানো হবে না।**

- `shared` (Kotlin Multiplatform + Compose Multiplatform):
  - `commonMain/kotlin/com/tech_hunt/tailorpass/`
    - `model/`, `util/`, পরে `data/` — মডেল, বিজনেস লজিক, ডেটা, view-model/state।
    - `ui/` — **সব স্ক্রিন, কম্পোনেন্ট, থিম, নেভিগেশন** (`App.kt`, `ui/theme/`, `ui/components/`, `ui/screens/<section>/`, `ui/nav/`)।
  - `commonMain/composeResources/` — `values/strings.xml` (bn, ডিফল্ট), `values-en/strings.xml`, `drawable/` (63টি VectorDrawable XML), `font/` (Hind Siliguri)। কোডে `Res.string.*`, `Res.drawable.*`, `Res.font.*`।
  - `androidMain` / `iosMain` — শুধু প্ল্যাটফর্ম-নির্ভর অংশ (`expect`/`actual`): ভাষা বদল, ক্যামেরা, শেয়ার/PDF, ফাইল পিকার, স্টোরেজ পাথ।
  - `iosMain/.../MainViewController.kt` — `fun MainViewController() = ComposeUIViewController { App() }`।
- `androidApp` — পাতলা শেল: `MainActivity` → `setContent { App() }`, manifest, launcher আইকন।
- `iosApp` — পাতলা শেল: `ContentView` একটি `UIViewControllerRepresentable` যা `MainViewControllerKt.MainViewController()` দেখায় (`.ignoresSafeArea()`), `Info.plist`, `InfoPlist.xcstrings`।
- **আমাদের ক্রম — এখন শুধু Android:** সেকশন ধরে ধরে `commonMain`-এ ডিজাইন দাঁড় করানো (স্যাম্পল ডেটা দিয়ে), Android-এ চালিয়ে/preview দিয়ে PNG-এর সাথে মেলানো। কোড common-এ লেখা হয় বলে পরে একই UI iOS-এ চলবে, কিন্তু **iOS-এর কোনো কাজ (iosMain, iosApp, iOS বিল্ড/টেস্ট) এখন করা হবে না** — সেটা আলাদাভাবে, Mac-এ, Step 11-এ। ডিজাইন শেষ হলে ডেটা/লজিক ও Android-এর প্ল্যাটফর্ম অংশ (ক্যামেরা, শেয়ার)।
- **রেফারেন্স প্রজেক্ট:** JetBrains-এর [`examples/chat`](https://github.com/JetBrains/compose-multiplatform/tree/master/examples/chat) — একই কাঠামো: `shared` (`kotlinMultiplatform` + `composeCompiler` + `androidMultiplatformLibrary` + `composeMultiplatform`, `android { androidResources { enable = true } }`), UI ও `composeResources/` `shared/commonMain`-এ, `androidMain`-এ `api(activity-compose, appcompat, core-ktx)`, `androidApp`-এ শুধু `MainActivity` → `setContent { … }`, iOS-এ `ComposeUIViewController { … }` + Swift-এ `UIViewControllerRepresentable`। সেটআপে সন্দেহ হলে এটা দেখুন।
- ডিজাইন ফ্রেম **360×800** (CSS px = dp = iOS pt) — পিক্সেল-নির্ভুলভাবে মেলাতে হবে।

---

## ২. সোর্স অফ ট্রুথ (অগ্রাধিকার অনুযায়ী)

1. [`html/NN_*.html`](html/) — প্রতিটি স্ক্রিনের হুবহু স্ট্যাটিক HTML + inline CSS: স্পেসিং, সাইজ, রং, radius, ফন্ট সাইজ, লেখা।
2. [`screens/NN_*.png`](screens/) — রেন্ডার করা ছবি (2×), মিলিয়ে দেখার জন্য।
3. [`SCREENS.md`](SCREENS.md) / [`screens.json`](screens.json) — প্রতিটি স্ক্রিনের উদ্দেশ্য, overlay base, নেভিগেশন, সব দৃশ্যমান লেখা।
4. [`navigation.json`](navigation.json) — স্ক্রিন → কোন স্ক্রিনে যায় (nav graph এখান থেকে)।
5. [`design-tokens.json`](design-tokens.json) — রং, টাইপ স্কেল, সাইজ।
6. [`kmp-resources/`](kmp-resources/) — তৈরি রিসোর্স। **নতুন কিছু বানানোর বদলে এগুলোই ব্যবহার করুন** (CMP-তে নিতে নিচের রূপান্তর মেনে)। আগে পড়ুন [`kmp-resources/README.md`](kmp-resources/README.md) ও [`kmp-resources/FONTS.md`](kmp-resources/FONTS.md)। (README-র SwiftUI অংশ আর প্রযোজ্য নয়।)

অন্যান্য: [`README.md`](README.md) (প্যাকেজ সূচি), [`maap-design-screens.pdf`](maap-design-screens.pdf) (৫২টি স্ক্রিন একসাথে, ২১ পৃষ্ঠা)।

### `kmp-resources/`-এ যা আছে ও CMP-তে কোথায় যাবে
| উৎস (`kmp-resources/…`) | গন্তব্য (`shared/src/commonMain/…`) | রূপান্তর |
|---|---|---|
| `shared/.../model/Measurements.kt`, `model/Diagrams.kt`, `util/BanglaDigits.kt` | `kotlin/com/tech_hunt/tailorpass/model/`, `util/` | হুবহু কপি। `MeasurementKey`, `Garment`, `GarmentMeasurements`, `LengthPreset`, `NeckDepthPreset`, `Fit`, `Gender`, `SourceBadge`, সাইজ চার্ট + `suggestSize()`; `Diagrams.*`; `toBanglaDigits()`, `formatInches()` (২৪½), `inchesToCmText()`, `feetInchesToCm()` |
| `androidApp/src/main/res/values/strings.xml`, `values-en/strings.xml` (339 key) | `composeResources/values/strings.xml`, `composeResources/values-en/strings.xml` | হুবহু কপি (CMP একই XML ফরম্যাট ও `%1$s` আর্গুমেন্ট বোঝে)। `\"`/`\'` escape ঠিক আছে কিনা বিল্ডে যাচাই। |
| `androidApp/src/main/res/drawable/*.xml` (63টি) | `composeResources/drawable/` | হুবহু কপি (CMP Android VectorDrawable XML পড়তে পারে)। |
| Hind Siliguri ৪টি TTF ([FONTS.md](kmp-resources/FONTS.md)) | `composeResources/font/hind_siliguri_{regular,medium,semibold,bold}.ttf` | — |
| `androidApp/.../ui/theme/{Color,Type,Dimens,Theme}.kt` | `kotlin/com/tech_hunt/tailorpass/ui/theme/` | `R.font.x` → `Font(Res.font.x, …)` (composable — `FontFamily` টি `@Composable` ফাংশনে বানান)। Android-only import থাকলে সরান। `MaapTheme { }`, `MaapTheme.colors`, `MaapTheme.numbers`, `Dimens`, shapes (নীল/সবুজ ব্র্যান্ড) অপরিবর্তিত। |
| `androidApp/.../ui/diagram/NumberedDiagram.kt` | `kotlin/.../ui/diagram/` | `painterResource(context.drawableId(name))` → `painterResource(drawable(name))`; `LocalContext` সরান। |
| `androidApp/.../util/Strings.kt` | `kotlin/.../ui/util/Strings.kt` (নতুন করে লেখা) | `getIdentifier` বাদ। shared key → রিসোর্স: `Res.allStringResources[key]` ও `Res.allDrawableResources[name]` দিয়ে `fun stringRes(key)`, `fun drawable(name)`, `@Composable fun str(key, vararg args)` (না পেলে `error("Missing …")`)। |
| `androidApp/src/main/res/raw/keep.xml` | — | **দরকার নেই** (CMP রিসোর্স shrink হয় না)। |
| `iosApp/Resources/InfoPlist.xcstrings`, `iosApp/Info.plist.snippet.xml` | `iosApp/iosApp/` | রাখুন — অ্যাপের নাম, ক্যামেরা usage লেখা, bn/en localization, শুধু portrait। `UIAppFonts` অংশ **বাদ** (ফন্ট composeResources থেকে আসে)। |
| `iosApp/Resources/Assets.xcassets`, `Localizable.xcstrings`, `iosApp/Theme/*.swift` | — | **ব্যবহার হবে না** (UI SwiftUI-তে নয়)। শুধু রেফারেন্স হিসেবে থাকল। |
| `design-tokens.json` | — | টোকেনের কপি (যাচাইয়ের জন্য) |

**মূল নিয়ম (Key rule):** model/logic কোডে শুধু **নাম** থাকে (string key যেমন `"m_chest"`, ছবির নাম যেমন `"ic_garment_panjabi"`)। UI সেগুলো `str(key)` / `drawable(name)` দিয়ে `composeResources` থেকে রিজলভ করে। **model/logic-এ কখনো display text রাখবেন না।** স্থির লেখা সরাসরি `stringResource(Res.string.x)`।

---

## ৩. ডিজাইনের নিয়ম (অবশ্যই মানতে হবে)

- মূল লেখা ≥ 16; চাপা যায় এমন সবকিছু ≥ 48; প্রতি স্ক্রিনে **একটিই** ভরাট প্রধান বোতাম; পুরো প্রস্থের 56 উঁচু বোতাম। দ্বিতীয় বোতাম বর্ডার দেওয়া বা লিংক।
- দর্জির শব্দ আগে, ইংরেজি বন্ধনীতে: "পুট (Shoulder)"। সংখ্যা বাংলা অঙ্কে (`toBanglaDigits()`), আধা ইঞ্চি "২৪½"। ইংরেজি ভাষায় `useBangla = false`।
- রং একা কখনো অর্থ বহন করে না — ব্যাজে ● ◐ ▲ চিহ্ন ও লেখা থাকে।
- লম্বা তালিকা স্ক্রল হয়, কিন্তু প্রধান অ্যাকশন নিচে স্থির থাকে।
- Bottom nav শুধু ট্যাব স্ক্রিনে (হোম, সাইজ চার্ট, সেটিংস) ও ফলাফলে; ক্যামেরা ও ধাপের ফ্লো-তে লুকানো।
- ক্যামেরা স্ক্রিন সবসময় গাঢ় (`camera*` রং)। ডার্ক মোডে primary `#A9C4F0`, প্রধান বোতামে গাঢ় লেখা।
- অফলাইন: নেটওয়ার্ক পারমিশন নেই; মাপ নেওয়ার পর ছবি মুছে ফেলা হয়।
- দুই প্ল্যাটফর্মে একই Compose UI — PNG-এর মতোই দেখাবে। Material-এর ডিফল্ট স্টাইলে ছেড়ে দেবেন না; HTML-এর মাপ/রং বসান। Safe area/insets: `Modifier.windowInsetsPadding(WindowInsets.safeDrawing)` / `Scaffold` insets — iOS notch ও home indicator-এ ঠিক দেখাতে হবে।
- কোনো hard-coded string নয় — সব লেখা `composeResources/values/strings.xml` key থেকে।
- **`commonMain`-এ কোনো Android/iOS-only API নয়** (`android.*`, `R`, `LocalContext`, `platform.UIKit.*`)। দরকার হলে `expect`/`actual`।

### API চিটশিট (সব `commonMain`)
| ডিজাইন | Compose Multiplatform |
|---|---|
| তালিকা স্ক্রল হয়, নিচে বোতাম স্থির (যেমন 8.4) | `Scaffold(bottomBar = …)` + `LazyColumn` |
| Bottom nav (৩ ট্যাব) | `NavigationBar` (কাস্টম স্টাইল) |
| ব্যাকসহ টপ বার | কাস্টম `TopBar` |
| লেখা | `stringResource(Res.string.x)` / `str(key)` |
| আইকন | `Icon(painterResource(Res.drawable.ic_x), …, tint = …)` / `drawable(name)` |
| রং | `MaterialTheme.colorScheme.*`, `MaapTheme.colors.*` |
| টাইপ | `MaterialTheme.typography.*`, `MaapTheme.numbers.*` |
| ডার্ক মোড | `MaapTheme(darkTheme = …)` |
| ভাষা বদল (1.1, 12.x) | `expect`/`actual`: Android `AppCompatDelegate.setApplicationLocales`; iOS `NSUserDefaults` `AppleLanguages` + UI রিফ্রেশ। Compose-এ `LocalAppLocale` (CMP ডকের "custom locale" প্যাটার্ন) দিয়ে সাথে সাথে রিকম্পোজ। |
| Format args | `stringResource(Res.string.x, arg1, …)` (`%1$s`) |
| Sheet / dialog (`overlay_on`) | `ModalBottomSheet` / `AlertDialog` / কাস্টম `Dialog` |
| নেভিগেশন | `org.jetbrains.androidx.navigation:navigation-compose` (multiplatform), type-safe `@Serializable` route |
| ব্যাক | `NavController` + CMP `BackHandler` (Android back / iOS edge-swipe) |
| Preview | `commonMain`-এ `@Preview` (`androidx.compose.ui.tooling.preview` / `org.jetbrains.compose.ui:ui-tooling-preview`) |
| ক্যামেরা preview (Step 10) | `expect @Composable fun CameraPreview(…)` → Android `AndroidView` + CameraX; iOS `UIKitView` + AVFoundation |
| PDF/PNG শেয়ার (Step 8/10) | PNG: `rememberGraphicsLayer()` → `toImageBitmap()` (common); PDF ও share sheet: `expect`/`actual` — Android `PdfDocument` + share `Intent`, iOS `UIGraphicsPDFRenderer` + `UIActivityViewController` |

### ডিজাইনে ব্যবহৃত স্যাম্পল ডেটা
- প্রোফাইল: **রাহাত** (ছেলে, ৫′৭″, রেগুলার, ৩টি সংরক্ষিত), **ফারহানা** (মেয়ে, ৫′৩″, রেগুলার, ২টি), **আব্বু** (ছেলে, কোনো মাপ নেই)।
- পাঞ্জাবি (ইঞ্চি): ঝুল 42, বুক 40, পেট 36, হিপ 41, পুট 18, হাতা 24½, হাতার মুখ 10, গলা 15½ → সাইজ **L**।
- সালওয়ার কামিজ: 8.4 / 9.3 দেখুন (কামিজ ১০টি + সালওয়ার ৪টি মাপ, সাইজ M)।

### যা পরে আসল তথ্য দিয়ে বদলাতে হবে (Placeholders)
যোগাযোগের ইমেইল (`about_contact_email`), আসল সাইজ চার্ট (`SampleMenShirtChart`), outlier সতর্কতার স্বাভাবিক সীমা (`minInches/maxInches`), ঝুল/গলার গভীরতার নমুনা মান, গোপনীয়তা নীতির লেখা (আসল আচরণের সাথে মিলিয়ে), নীল/সবুজ ব্র্যান্ড বাছাই, অ্যাপ আইকন/লোগো (`app_logo` এখনো পুরোনো "মাপ" ডিজাইনের হলে)।

---

## ৪. প্রজেক্টের বর্তমান অবস্থা (যা জানা দরকার)

- প্রজেক্ট: `TailorPass` (`settings.gradle.kts`), প্যাকেজ **`com.tech_hunt.tailorpass`**, iOS `PRODUCT_NAME=TailorPass`। `kmp-resources/`-এর সব Kotlin ফাইল আগেই এই প্যাকেজে (`com/tech_hunt/tailorpass/...`)।
- **এখনকার সেটআপ আগের (native UI) পরিকল্পনার — Step 0-এ উল্টাতে হবে:**
  - `shared`-এ এখন Compose নেই (শুধু `Greeting`/`Platform` টেমপ্লেট কোড)। → Compose Multiplatform plugin, compose compiler, resources আবার যোগ করতে হবে।
  - `androidApp/.../App.kt` একটি অস্থায়ী root স্ক্রিন; `androidApp`-এ Compose সরাসরি যোগ করা। → `App()` যাবে `shared/commonMain/.../App.kt`-এ, `androidApp` থেকে UI কোড সরবে।
  - `androidApp/src/main/res/values/strings.xml`-এ শুধু `app_name = TailorPass` (Android launcher label এখান থেকে — এটা রাখুন, সাথে `values-en`-এও একই)।
  - iOS: `iosApp/iosApp/ContentView.swift` এখন সাধারণ SwiftUI `Text(Greeting().greet())`। → Step 11-এ `MainViewController()` দেখাবে (এখন হাত দেবেন না)।
- `gradle/libs.versions.toml`-এ আগেই আছে: `composeMultiplatform = 1.12.1`, `material3`, `compose-components-resources`, `compose-uiToolingPreview`, lifecycle-viewmodel/runtime-compose, plugin `composeMultiplatform`, `composeCompiler`।
- AGP 9.1.1, Kotlin 2.4.20, compileSdk 37, minSdk 24। `shared` `com.android.kotlin.multiplatform.library` plugin ব্যবহার করে।

---

## ৫. কাজের নিয়ম (প্রতিটি স্টেপে)

1. **এক স্টেপ = এক সেশন।** স্টেপের সব স্ক্রিন শেষ হলে, বিল্ড পাস করলে এবং PNG-এর সাথে তুলনা লিস্ট দিলে **কাজ বন্ধ করুন** — পরের স্টেপ নিজে থেকে শুরু করবেন না। ব্যবহারকারী রিভিউ করে পরের স্টেপ বলবেন।
2. **ডিজাইন আগে:** স্ক্রিনগুলো `shared/commonMain`-এ Compose দিয়ে বানানো হবে স্যাম্পল ডেটা দিয়ে। state ও স্যাম্পল ডেটা shared মডেল (`Measurements.kt` ইত্যাদি) ব্যবহার করবে। প্রতিটি স্ক্রিন stateless composable (`XxxScreen(state, onAction)`) + `@Preview` (লাইট, এবং যেখানে ডিজাইন আছে সেখানে ডার্ক)।
3. প্রতিটি স্ক্রিনের জন্য: আগে HTML পড়ুন (মাপ, রং, লেখা), তারপর PNG দেখুন, তারপর বানান। শেষে **PNG-এর সাথে তুলনা করে পার্থক্যের তালিকা** দিন।
4. পুনরায় ব্যবহারযোগ্য কম্পোনেন্ট প্রথম যে স্টেপে লাগে সেখানেই বানান (`shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/ui/components/`), পরের স্টেপে নতুন না বানিয়ে সেগুলোই ব্যবহার করুন।
5. Sheet/dialog (যেগুলোর `Overlay` আছে) আলাদা route নয় — base স্ক্রিনের ওপর overlay।
6. `Dark` ভ্যারিয়েন্ট (2.2, 5.2, 8.2, 9.2) আলাদা স্ক্রিন নয় — একই composable `MaapTheme(darkTheme = true)`-তে; সেগুলোর জন্য ডার্ক `@Preview` বানিয়ে PNG-এর সাথে মেলান।
7. নেভিগেশন [`navigation.json`](navigation.json) থেকে (multiplatform Navigation Compose); স্টেপের ভেতরের স্ক্রিনগুলোর মধ্যে নেভিগেশন কাজ করবে, বাকি গন্তব্য placeholder রাখা যাবে।
8. প্রতিটি স্টেপের শেষে `./gradlew :androidApp:assembleDebug` পাস করতে হবে। iOS বিল্ড/রান এই স্টেপগুলোর অংশ নয় (আলাদাভাবে টেস্ট হবে) — তবে `commonMain`-এ প্ল্যাটফর্ম-API না রাখার নিয়ম মানতেই হবে, যাতে পরে iOS-এ চলে।

**প্রতি স্টেপে এজেন্টকে দেওয়ার প্রম্পট:**
> "for-agent/plan.md পড়ুন। Step <N> বাস্তবায়ন করুন — Compose Multiplatform (`shared/commonMain`), এখন শুধু Android-এ চালিয়ে যাচাই, html/ ও screens/ হুবহু মিলিয়ে। state/মডেল shared থেকে, composeResources-এর theme, ছবি ও string key ব্যবহার করুন। কোনো hard-coded string নয়, commonMain-এ প্ল্যাটফর্ম-API নয়। প্রতিটি স্ক্রিন শেষে PNG-এর সাথে তুলনা করে পার্থক্য লিখুন। স্টেপ শেষ হলে থামুন।"

---

## ৬. ধাপে ধাপে পরিকল্পনা

সারসংক্ষেপ:

| Step | বিষয় | স্ক্রিন | সংখ্যা |
|---|---|---|---|
| 0 | CMP সেটআপ ও ফাউন্ডেশন | — | — |
| 1 | প্রথমবার খোলা | 1.1–1.5 | 5 |
| 2 | হোম | 2.1–2.5 | 5 |
| 3 | মাপ শুরু + ছবি তোলার প্রস্তুতি | 3.1–3.2, 4.1–4.4 | 6 |
| 4 | ক্যামেরা + ছবি যাচাই | 5.1–5.4, 6.1–6.3 | 7 |
| 5 | ফিতা দিয়ে মাপ — ক | 7.1–7.5, 7.10 | 6 |
| 6 | ফিতা দিয়ে মাপ — খ | 7.6–7.9 | 4 |
| 7 | মাপ যাচাই ও এডিট | 8.1–8.5 | 5 |
| 8 | ফলাফল + শেয়ার ফাইল | 9.1–9.4, 10.1–10.3 | 7 |
| 9 | সাইজ চার্ট + সেটিংস | 11.1, 12.1–12.6 | 7 |
| 10 | ডেটা ও লজিক + প্ল্যাটফর্ম অংশ (ক্যামেরা, AI, শেয়ার) | — | — |
| 11 | iOS (আলাদাভাবে, Mac-এ): শেল, প্ল্যাটফর্ম অংশ, চালানো ও যাচাই | সব | 52 |

---

### Step 0 — Compose Multiplatform সেটআপ ও ফাউন্ডেশন

**লক্ষ্য:** common Compose UI Android-এ চলছে; রিসোর্স, ফন্ট, থিম ও string লোড হচ্ছে তা একটি টেস্ট স্ক্রিনে প্রমাণ করা।

**Gradle**
- [ ] `shared/build.gradle.kts` (chat example-এর মতো): plugin `composeMultiplatform` + `composeCompiler` যোগ; `androidMain.dependencies { api(activity-compose); api(appcompat); api(core-ktx) }`; `commonMain.dependencies`: `compose.runtime/foundation/material3/ui`, `compose.components.resources`, `compose.uiToolingPreview`, lifecycle viewmodel/runtime-compose, multiplatform navigation-compose, `kotlinx-serialization-json` (route-এর জন্য, + serialization plugin)। iOS target ও framework (`baseName = "Shared"`, `isStatic = true`) যেমন আছে রাখুন — শুধু Android বিল্ড করা হবে।
- [ ] `shared`-এর android target-এ Android resources চালু করুন (`androidResources { enable = true }` — AGP 9 KMP library), নাহলে Android-এ composeResources পাওয়া যাবে না।
- [ ] `compose.resources { publicResClass = false; packageOfResClass = "com.tech_hunt.tailorpass.resources"; generateResClass = always }`।
- [ ] `shared`-এর ফাইল-হেডার কমেন্ট ("No UI, no Compose…") বদলে নতুন নিয়ম লিখুন।
- [ ] `androidApp/build.gradle.kts`: plugin `composeMultiplatform` + `composeCompiler`; dependency `project(":shared")`, `activity-compose`, `ui-tooling` (debug) — বাকি Compose `shared`-এর মাধ্যমে আসবে।
- [ ] নতুন লাইব্রেরি `gradle/libs.versions.toml`-এ (CMP 1.12.1-এর সাথে মানানসই ভার্সন)।

**রিসোর্স ও কোড স্থানান্তর** (অংশ ২-এর টেবিল মেনে)
- [ ] `model/`, `util/` → `shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/`।
- [ ] strings (bn/en), 63টি drawable, ৪টি Hind Siliguri TTF → `shared/src/commonMain/composeResources/`।
- [ ] theme (`Color/Type/Dimens/Theme.kt`), `NumberedDiagram.kt`, নতুন `ui/util/Strings.kt` → `shared/.../ui/`।
- [ ] `androidApp/.../App.kt` মুছে `shared/commonMain/.../App.kt` (`MaapTheme { … }`); `MainActivity` সেটিই দেখাবে।
- [ ] টেমপ্লেট `Greeting`/`Platform`/`GreetingUtil` ও তাদের টেস্ট মুছুন (বা নতুন টেস্টে বদলান)।

**প্ল্যাটফর্ম কনফিগ**
- [ ] Android manifest: কোনো INTERNET পারমিশন নয়; portrait only; `android:label="@string/app_name"` (= TailorPass); `androidApp/res/values-en/strings.xml`-এও `app_name`।

**টেস্ট স্ক্রিন ও বেস কম্পোনেন্ট**
- [ ] `App()` → টেস্ট স্ক্রিন: বাংলা লেখা, একটি আইকন, primary বোতাম, `formatInches(24.5f)`, `str("m_chest")` (ডাইনামিক key)।
- [ ] বেস কম্পোনেন্ট (প্রায় সব স্টেপে লাগবে): `TopBar` (back / title / subtitle / trailing chip/icon), `PrimaryButton` (56 pill), `OutlinedPillButton`, `MaapScaffold` (নিচে স্থির বোতাম + স্ক্রল কনটেন্ট, safe-area সহ)।
- [ ] ভাষা: `expect fun setAppLanguage(tag: String)` + `LocalAppLocale` কাঠামো (UI Step 1-এ)।
- [ ] `./gradlew :androidApp:assembleDebug` পাস; অ্যাপ চালিয়ে টেস্ট স্ক্রিন দেখা।

**⏹ থামুন।**

---

### Step 1 — প্রথমবার খোলা (First launch) · ৫টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 1.1 | ভাষা বাছাই `Language` | [html](html/01_1-1_Language.html) | [png](screens/01_1-1_Language.png) |
| 1.2 | অনবোর্ডিং — কীভাবে কাজ করে `Onboarding1` | [html](html/02_1-2_Onboarding1.html) | [png](screens/02_1-2_Onboarding1.png) |
| 1.3 | অনবোর্ডিং — গোপনীয়তা `Onboarding2` | [html](html/03_1-3_Onboarding2.html) | [png](screens/03_1-3_Onboarding2.png) |
| 1.4 | অনবোর্ডিং — দর্জিকে শেয়ার `Onboarding3` | [html](html/04_1-4_Onboarding3.html) | [png](screens/04_1-4_Onboarding3.png) |
| 1.5 | প্রোফাইল তৈরি `ProfileCreate` | [html](html/05_1-5_ProfileCreate.html) | [png](screens/05_1-5_ProfileCreate.png) |

**নতুন কম্পোনেন্ট:** `OnboardingPage` (ইলাস্ট্রেশন + টাইটেল + বুলেট + Skip/Next + ডট ইন্ডিকেটর — ১.২–১.৪ একই কাঠামো, `HorizontalPager`), `SegmentedToggle` (ছেলে/মেয়ে, ফুট-ইঞ্চি/সেমি), `FitCard` (স্লিম/রেগুলার/লুজ), টেক্সট ফিল্ড স্টাইল।
**নোট:** উচ্চতা রূপান্তর `feetInchesToCm()`; ভাষা বাছাই Step 0-এর `setAppLanguage` + `LocalAppLocale`।

**⏹ থামুন।**

---

### Step 2 — হোম (Home) · ৫টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 2.1 | হোম `Main` | [html](html/06_2-1_Main.html) | [png](screens/06_2-1_Main.png) |
| 2.2 | হোম — ডার্ক `MainDark` (ডার্ক preview) | [html](html/07_2-2_MainDark.html) | [png](screens/07_2-2_MainDark.png) |
| 2.3 | হোম — খালি অবস্থা (আব্বু) `HomeEmpty` | [html](html/08_2-3_HomeEmpty.html) | [png](screens/08_2-3_HomeEmpty.png) |
| 2.4 | প্রোফাইল বদলানো শিট `ProfileSheet` (overlay on Main) | [html](html/09_2-4_ProfileSheet.html) | [png](screens/09_2-4_ProfileSheet.png) |
| 2.5 | সংরক্ষিত মাপের মেনু `SavedMenuSheet` (overlay on Main) | [html](html/10_2-5_SavedMenuSheet.html) | [png](screens/10_2-5_SavedMenuSheet.png) |

**নতুন কম্পোনেন্ট:** `BottomNav` (৩ ট্যাব, pill indicator — হোম/সাইজ চার্ট/সেটিংস; সাইজ চার্ট ও সেটিংস এখন placeholder), `ProfileChip`/অ্যাভাটার, `GarmentCard` (৬টি গ্রিড), `SavedRow` (ট্যাপ, শেয়ার আইকন, লং-প্রেস), `MaapBottomSheet` (`ModalBottomSheet` স্টাইল), empty state।
**নোট:** 7.9 `HomeIncomplete` Step 6-এ এই স্ক্রিনেই একটি কার্ড যোগ করবে — হোম স্ক্রিন সেভাবে state-চালিত রাখুন।

**⏹ থামুন।**

---

### Step 3 — মাপ শুরু ও ছবি তোলার প্রস্তুতি · ৬টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 3.1 | পদ্ধতি বাছাই `Method` | [html](html/11_3-1_Method.html) | [png](screens/11_3-1_Method.png) |
| 3.2 | ক্যামেরা পারমিশন `CameraPermission` | [html](html/12_3-2_CameraPermission.html) | [png](screens/12_3-2_CameraPermission.png) |
| 4.1 | সতর্কতা চেকলিস্ট — পাঞ্জাবি `Checklist` | [html](html/13_4-1_Checklist.html) | [png](screens/13_4-1_Checklist.png) |
| 4.2 | সতর্কতা চেকলিস্ট — সালওয়ার কামিজ `ChecklistFemale` | [html](html/14_4-2_ChecklistFemale.html) | [png](screens/14_4-2_ChecklistFemale.png) |
| 4.3 | পোজ গাইড — ছেলে `PoseGuide` | [html](html/15_4-3_PoseGuide.html) | [png](screens/15_4-3_PoseGuide.png) |
| 4.4 | পোজ গাইড — মেয়ে `PoseGuideFemale` | [html](html/16_4-4_PoseGuideFemale.html) | [png](screens/16_4-4_PoseGuideFemale.png) |

**নতুন কম্পোনেন্ট:** `MethodCard` (হাইলাইট/সাধারণ), `InfoBanner` (tint / orange / green), `ChecklistItem` (চেকবক্স + দুই লাইন), টিপ কার্ড।
**নোট:** 4.1/4.2 এবং 4.3/4.4 একই composable, শুধু লিঙ্গ/পোশাক অনুযায়ী ডেটা আলাদা। চেকলিস্টে সব টিক না দিলে "এগিয়ে যান" disabled (interactive state)। ক্যামেরা পারমিশন আসল OS প্রম্পট এখনই নয় — শুধু UI।

**⏹ থামুন।**

---

### Step 4 — ক্যামেরা ও ছবি যাচাই · ৭টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 5.1 | সামনের ছবি ১/২ — ছেলে `Camera` | [html](html/17_5-1_Camera.html) | [png](screens/17_5-1_Camera.png) |
| 5.2 | সামনের ছবি ১/২ — ডার্ক `CameraDark` (ডার্ক preview) | [html](html/18_5-2_CameraDark.html) | [png](screens/18_5-2_CameraDark.png) |
| 5.3 | সামনের ছবি ১/২ — মেয়ে `CameraFemale` | [html](html/19_5-3_CameraFemale.html) | [png](screens/19_5-3_CameraFemale.png) |
| 5.4 | পাশের ছবি ২/২ `CameraSide` | [html](html/20_5-4_CameraSide.html) | [png](screens/20_5-4_CameraSide.png) |
| 6.1 | ছবি যাচাই — সফল ও প্রসেসিং `PhotoCheckSuccess` | [html](html/21_6-1_PhotoCheckSuccess.html) | [png](screens/21_6-1_PhotoCheckSuccess.png) |
| 6.2 | ছবি যাচাই — সমস্যা `PhotoCheckProblem` | [html](html/22_6-2_PhotoCheckProblem.html) | [png](screens/22_6-2_PhotoCheckProblem.png) |
| 6.3 | মাপ বের করা যায়নি `AIFailed` | [html](html/23_6-3_AIFailed.html) | [png](screens/23_6-3_AIFailed.png) |

**নতুন কম্পোনেন্ট:** `CameraOverlay` (ড্যাশ বডি আউটলাইন — সামনে ছেলে/মেয়ে, পাশ), স্টেপ pill (১/২), ভয়েস-গাইড টগল, টাইমার চিপ, `StepProgressBar`, প্রসেসিং স্টেপ লিস্ট, কারণ কার্ড।
**নোট:** ক্যামেরা স্ক্রিন সবসময় গাঢ় (`camera*` রং), bottom nav নেই। এখন ক্যামেরা preview-এর জায়গায় placeholder (গাঢ় ব্যাকগ্রাউন্ড) — আসল `expect CameraPreview` ও AI Step 10-এ, `interface BodyMeasurer`-এর পেছনে। UI শুধু state দেখাবে: গাইডেন্স ব্যানার, ধাপ ১/২ → ২/২, প্রসেসিং প্রোগ্রেস, সফল, সমস্যা(কারণ), ব্যর্থ। 5.1/5.3/5.4 একই `CameraScreen` বিভিন্ন state-এ।

**⏹ থামুন।**

---

### Step 5 — ফিতা দিয়ে মাপ (ক) · ৬টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 7.1 | প্রস্তুতি `ManualPrep` | [html](html/24_7-1_ManualPrep.html) | [png](screens/24_7-1_ManualPrep.png) |
| 7.2 | মাপের তালিকা `ManualList` | [html](html/25_7-2_ManualList.html) | [png](screens/25_7-2_ManualList.png) |
| 7.3 | শরীরের মাপ — বুক `ManualEntry` | [html](html/26_7-3_ManualEntry.html) | [png](screens/26_7-3_ManualEntry.png) |
| 7.4 | শরীরের মাপ — হাতা (সাহায্যকারী লাগবে) `ManualSleeve` | [html](html/27_7-4_ManualSleeve.html) | [png](screens/27_7-4_ManualSleeve.png) |
| 7.5 | অস্বাভাবিক মাপের সতর্কতা `ManualWarnSheet` (overlay on ManualEntry) | [html](html/28_7-5_ManualWarnSheet.html) | [png](screens/28_7-5_ManualWarnSheet.png) |
| 7.10 | পছন্দের মাপ — গলার গভীরতা (মেয়ে) `ManualNeckDepth` | [html](html/33_7-10_ManualNeckDepth.html) | [png](screens/33_7-10_ManualNeckDepth.png) |

**নতুন কম্পোনেন্ট:** `NumberStepperField` (−/+ 0.5″, বড় সংখ্যা), `ManualStepRow` (done/now/pending + "সাহায্যকারী লাগবে" ব্যাজ), কলাপসিবল "ভুল এড়াতে", হাইলাইট করা ডায়াগ্রাম, `PresetPictureCard` (৩টি ছবি-কার্ড — 7.10 ও পরে 7.6 এ লাগবে)।
**নোট:** 7.3/7.4/7.10 একই `ManualEntryScreen` — মাপের key অনুযায়ী ডায়াগ্রাম, নির্দেশনা, সাহায্যকারী ব্যানার, preset কার্ড বদলায়। outlier সীমা `MeasurementKey`-এর `minInches/maxInches` থেকে। সংখ্যা কিবোর্ড: `KeyboardOptions(keyboardType = KeyboardType.Decimal)` — iOS-এ "Done" দিয়ে কিবোর্ড বন্ধ হয় কিনা খেয়াল রাখুন।

**⏹ থামুন।**

---

### Step 6 — ফিতা দিয়ে মাপ (খ) · ৪টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 7.6 | পছন্দের মাপ — ঝুল `ManualLength` | [html](html/29_7-6_ManualLength.html) | [png](screens/29_7-6_ManualLength.png) |
| 7.7 | সারসংক্ষেপ `ManualSummary` | [html](html/30_7-7_ManualSummary.html) | [png](screens/30_7-7_ManualSummary.png) |
| 7.8 | মাঝপথে বের হওয়া `ManualExitDialog` (overlay on ManualEntry) | [html](html/31_7-8_ManualExitDialog.html) | [png](screens/31_7-8_ManualExitDialog.png) |
| 7.9 | হোম — অসমাপ্ত মাপ `HomeIncomplete` | [html](html/32_7-9_HomeIncomplete.html) | [png](screens/32_7-9_HomeIncomplete.png) |

**নতুন কম্পোনেন্ট:** `ConfirmDialog`, সারসংক্ষেপ সারি (এডিট আইকন, অনুপস্থিত হলে কমলা সারি + "এখন মাপুন"), `IncompleteCard` (কমলা বাম বর্ডার, ৪/৮ প্রোগ্রেস)।
**নোট:** 7.6 Step 5-এর `PresetPictureCard` ব্যবহার করবে (`LengthPreset`, উচ্চতা থেকে মান)। 7.9 নতুন স্ক্রিন নয় — Step 2-এর হোমে incomplete-কার্ড state। 7.8 ব্যাক (Android back / iOS swipe) ধরতে `BackHandler`।

**⏹ থামুন।**

---

### Step 7 — মাপ যাচাই ও এডিট (Review & edit) · ৫টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 8.1 | মাপ যাচাই — পাঞ্জাবি `Verify` | [html](html/34_8-1_Verify.html) | [png](screens/34_8-1_Verify.png) |
| 8.2 | মাপ যাচাই — ডার্ক `VerifyDark` (ডার্ক preview) | [html](html/35_8-2_VerifyDark.html) | [png](screens/35_8-2_VerifyDark.png) |
| 8.3 | মাপ এডিট শিট `EditSheet` (overlay on Verify) | [html](html/36_8-3_EditSheet.html) | [png](screens/36_8-3_EditSheet.png) |
| 8.4 | মাপ যাচাই — সালওয়ার কামিজ `VerifySK` | [html](html/37_8-4_VerifySK.html) | [png](screens/37_8-4_VerifySK.png) |
| 8.5 | মাপ যাচাই — ফিতায় মাপা `VerifyManual` | [html](html/38_8-5_VerifyManual.html) | [png](screens/38_8-5_VerifyManual.png) |

**নতুন কম্পোনেন্ট:** `MeasurementRow` + `SourceBadge` ব্যাজ (● উচ্চ / ◐ মাঝারি / ▲ নিম্ন / ✎ আপনার পছন্দ / ফিতায় মাপা / বাদ আছে), সেকশন হেডার (কামিজ ১০টি / সালওয়ার ৪টি), দর্জির নোট ফিল্ড।
**নোট:** পাঁচটিই একই `VerifyScreen` — শুধু ডেটা ও ব্যাজের উৎস আলাদা। 8.4-এ তালিকা স্ক্রল, "ফলাফল দেখুন" নিচে স্থির (`Scaffold(bottomBar)` + `LazyColumn`)। এডিট শিট `NumberStepperField` (Step 5) পুনঃব্যবহার করবে।

**⏹ থামুন।**

---

### Step 8 — ফলাফল ও শেয়ার ফাইল · ৭টি ডিজাইন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 9.1 | ফলাফল — পাঞ্জাবি `Result` | [html](html/39_9-1_Result.html) | [png](screens/39_9-1_Result.png) |
| 9.2 | ফলাফল — ডার্ক `ResultDark` (ডার্ক preview) | [html](html/40_9-2_ResultDark.html) | [png](screens/40_9-2_ResultDark.png) |
| 9.3 | ফলাফল — সালওয়ার কামিজ `ResultSK` | [html](html/41_9-3_ResultSK.html) | [png](screens/41_9-3_ResultSK.png) |
| 9.4 | গেঞ্জির সাইজ `GenjiResult` | [html](html/42_9-4_GenjiResult.html) | [png](screens/42_9-4_GenjiResult.png) |
| 10.1 | PDF — পাঞ্জাবি, A4 `TailorPDF` (794×1123) | [html](html/43_10-1_TailorPDF.html) | [png](screens/43_10-1_TailorPDF.png) |
| 10.2 | PDF — সালওয়ার কামিজ, A4 `TailorPDFSK` (794×1123) | [html](html/44_10-2_TailorPDFSK.html) | [png](screens/44_10-2_TailorPDFSK.png) |
| 10.3 | ছবি হিসেবে শেয়ার, PNG `SharePNG` (1080×2600) | [html](html/45_10-3_SharePNG.html) | [png](screens/45_10-3_SharePNG.png) |

**নতুন কম্পোনেন্ট:** `NumberedDiagram` (দেওয়া আছে) ব্যবহার, সাজেস্টেড সাইজ কার্ড (`suggestSize()`), নম্বরসহ মাপের তালিকা, ইঞ্চি/সেমি `SegmentedToggle` (`inchesToCmText()`), কামিজ/সালওয়ার ট্যাব, সাইজ তুলনা (M|L|XL), "সেভ হয়েছে" চিপ।
**নোট:** 9.1/9.2/9.3 একই `ResultScreen` (bottom nav সহ)। 10.x অ্যাপের স্ক্রিন নয় — শেয়ার করার ফাইল। ফাইলের লেআউট common Compose composable (`TailorSheetPdfPage`, `TailorSharePng`) হিসেবে বানান + preview দিয়ে PNG-এর সাথে মেলান; PNG রেন্ডার common (`GraphicsLayer.toImageBitmap()`, 1080 px চওড়া)। PDF লেখা ও share sheet `expect`/`actual` (Android `PdfDocument` + `Intent`, iOS `UIGraphicsPDFRenderer` + `UIActivityViewController`) — এখানে বা Step 10-এ।

**⏹ থামুন।**

---

### Step 9 — সাইজ চার্ট ও সেটিংস · ৭টি স্ক্রিন

| # | স্ক্রিন | HTML | PNG |
|---|---|---|---|
| 11.1 | সাইজ চার্ট `SizeChart` | [html](html/46_11-1_SizeChart.html) | [png](screens/46_11-1_SizeChart.png) |
| 12.1 | সেটিংস `Settings` | [html](html/47_12-1_Settings.html) | [png](screens/47_12-1_Settings.png) |
| 12.2 | ব্যাকআপ সফল `BackupSnackbar` (overlay on Settings) | [html](html/48_12-2_BackupSnackbar.html) | [png](screens/48_12-2_BackupSnackbar.png) |
| 12.3 | রিস্টোর নিশ্চিতকরণ `RestoreDialog` (overlay on Settings) | [html](html/49_12-3_RestoreDialog.html) | [png](screens/49_12-3_RestoreDialog.png) |
| 12.4 | সব ডেটা মুছুন — নিশ্চিতকরণ `DeleteDialog` (overlay on Settings) | [html](html/50_12-4_DeleteDialog.html) | [png](screens/50_12-4_DeleteDialog.png) |
| 12.5 | গোপনীয়তা নীতি `PrivacyPolicy` | [html](html/51_12-5_PrivacyPolicy.html) | [png](screens/51_12-5_PrivacyPolicy.png) |
| 12.6 | অ্যাপ সম্পর্কে `About` | [html](html/52_12-6_About.html) | [png](screens/52_12-6_About.png) |

**নতুন কম্পোনেন্ট:** সাইজ টেবিল (ব্যবহারকারীর সারি হাইলাইট "আপনার"), গার্মেন্ট চিপ, `SettingsRow`/সেকশন হেডার, সবুজ snackbar, রেডিও অপশন কার্ড (merge/replace, replace-এ সতর্কতা), টাইপ-করে-নিশ্চিত ("মুছুন") লাল বোতাম, নম্বরসহ পলিসি সেকশন।
**নোট:** Step 2-এর `BottomNav`-এর placeholder ট্যাবগুলো এখানে আসল স্ক্রিনে বদলাবে। ভাষা বদল `setAppLanguage` + `LocalAppLocale`। About-এ অ্যাপের নাম `app_name` (TailorPass)। `about_contact_email` placeholder।

**⏹ থামুন। — এখানে ডিজাইন পর্ব শেষ (৫২টি ডিজাইন, দুই প্ল্যাটফর্মে একই কোড)।**

---

### Step 10 — ডেটা ও লজিক + প্ল্যাটফর্ম অংশ

(ডিজাইন পর্ব শেষে; প্রয়োজনে একাধিক সেশনে ভাগ করা হবে)

- [ ] **ডেটা (শুধু লোকাল — SQLDelight বা Room KMP):** `Profile(name, gender, heightCm, fit)`, `MeasurementSet(profileId, garment, date, method AI|TAPE, unit, note, values map, sources map, status COMPLETE|INCOMPLETE)`। ড্রাইভার/ডেটাবেস পাথ `expect`/`actual`।
- [ ] রিপোজিটরি + প্রতি ফ্লো-তে একটি `ViewModel` (`commonMain`, multiplatform lifecycle-viewmodel, `StateFlow` → `collectAsStateWithLifecycle()`)। UI এক জায়গায় থাকায় iOS-এ SKIE/NativeCoroutines লাগবে না।
- [ ] সেটিংস (ভাষা, থিম, অনবোর্ডিং দেখা হয়েছে কিনা): DataStore KMP বা multiplatform-settings।
- [ ] ব্যাকআপ/রিস্টোর = JSON ফাইল (kotlinx.serialization); merge বনাম replace; ফাইল বাছাই/সেভ `expect`/`actual` (Android SAF, iOS `UIDocumentPickerViewController`)।
- [ ] স্যাম্পল ডেটা সরিয়ে স্ক্রিনগুলো আসল ViewModel-এ যুক্ত করা।
- [ ] **ক্যামেরা + AI মাপ:** `commonMain`-এ `interface BodyMeasurer` ও `expect @Composable fun CameraPreview(...)`; Android `actual`: CameraX + on-device মডেল (iOS `actual` Step 11-এ)। ছবি মাপ শেষে মুছে ফেলা। OS ক্যামেরা পারমিশন।
- [ ] শেয়ার: PDF (A4) ও PNG — Step 8-এর common লেআউট থেকে, share sheet `expect`/`actual` (এখন Android `actual`)।
- [ ] নোট: `expect` ঘোষণা থাকলে iOS-এ পরে `actual` লাগবে — Step 11-এর তালিকায় যোগ করুন।

**⏹ থামুন।**

---

### Step 11 — iOS (আলাদাভাবে, Mac প্রয়োজন)

UI আগেই common — এখানে নতুন UI লেখা হবে না; iOS শেল, iOS-এর `actual` অংশ, তারপর চালিয়ে ঠিক করা।

- [ ] `shared/src/iosMain/.../MainViewController.kt`: `fun MainViewController() = ComposeUIViewController { App() }`; `iosApp/iosApp/ContentView.swift` → `UIViewControllerRepresentable` (chat example-এর `ComposeViewControllerToSwiftUI.swift`-এর মতো) + `.ignoresSafeArea()`।
- [ ] সব `expect`-এর iOS `actual`: ভাষা বদল (`NSUserDefaults` `AppleLanguages`), ক্যামেরা (AVFoundation `UIKitView` + Core ML), শেয়ার (`UIGraphicsPDFRenderer` + `UIActivityViewController`), ফাইল পিকার, ডেটাবেস পাথ।
- [ ] Xcode 15+ / iOS 16+; `iosApp` বিল্ড ফেজে `./gradlew :shared:embedAndSignAppleFrameworkForXcode` ঠিক আছে কিনা; সিমুলেটর ও আসল ডিভাইসে চালানো।
- [ ] `Info.plist` / `InfoPlist.xcstrings`: `CFBundleDisplayName` = TailorPass, bn/en localization (Development Language বাংলা), ক্যামেরা usage, শুধু portrait, `CADisableMinimumFrameDurationOnPhone = true` (`UIAppFonts` লাগবে না)।
- [ ] প্রতিটি স্ক্রিন (Step 1 → 9 একই ভাগে) iOS-এ PNG ও Android-এর সাথে মিলিয়ে পার্থক্যের তালিকা: safe area/notch, কিবোর্ড insets, বাংলা ফন্ট রেন্ডারিং, sheet/dialog আচরণ, ব্যাক-সোয়াইপ, ভাষা বদল।
- [ ] iOS `actual` অংশগুলো আসল ডিভাইসে পরীক্ষা।

---

## ৭. ফাইল সূচি (`for-agent/`, এই ফাইলের পাশে)

| ফাইল/ফোল্ডার | কী আছে |
|---|---|
| [`README.md`](README.md) | প্যাকেজ সূচি |
| [`SCREENS.md`](SCREENS.md) | ৫২টি স্ক্রিনের উদ্দেশ্য, নেভিগেশন, সব লেখা |
| [`screens.json`](screens.json) | একই তথ্য, মেশিন-পাঠযোগ্য |
| [`navigation.json`](navigation.json) | নেভিগেশন গ্রাফ |
| [`design-tokens.json`](design-tokens.json) | রং (লাইট/ডার্ক/বিকল্প ব্র্যান্ড), টাইপ, সাইজ |
| [`maap-design-screens.pdf`](maap-design-screens.pdf) | সব স্ক্রিন একসাথে (A4 আড়াআড়ি, ২১ পৃষ্ঠা) |
| [`html/`](html/) | প্রতিটি স্ক্রিনের হুবহু HTML/CSS — `01_1-1_Language` … `52_12-6_About` |
| [`screens/`](screens/) | প্রতিটি স্ক্রিনের PNG (2×), একই নাম |
| [`kmp-resources/README.md`](kmp-resources/README.md) | রিসোর্সের বিবরণ (SwiftUI অংশ আর প্রযোজ্য নয়) |
| [`kmp-resources/FONTS.md`](kmp-resources/FONTS.md) | Hind Siliguri ফন্ট নামানো ও নামকরণ |
| [`kmp-resources/shared/`](kmp-resources/shared/) | মডেল, ডায়াগ্রাম, বাংলা অঙ্ক, gradle snippet |
| [`kmp-resources/androidApp/`](kmp-resources/androidApp/) | drawable, strings (bn/en), Compose থিম, ডায়াগ্রাম — Step 0-এ `shared/commonMain`-এ যাবে |
| [`kmp-resources/iosApp/`](kmp-resources/iosApp/) | InfoPlist.xcstrings ও Info.plist snippet ব্যবহার হবে; Assets/Localizable/Theme শুধু রেফারেন্স |
