# মাপ — Kotlin Multiplatform (KMP) রিসোর্স

ধরে নেওয়া হয়েছে: **shared মডিউলে শুধু লজিক** (Kotlin), **UI নেটিভ** — Android-এ Jetpack Compose, iOS-এ SwiftUI।
তাই রিসোর্স তিন ভাগে:

```
shared/src/commonMain/kotlin/com/tech_hunt/tailorpass/   ← দুই প্ল্যাটফর্মের এক কোড (কোনো Compose/R/Res নেই)
├── model/Measurements.kt   মাপের তালিকা, ক্রম, পছন্দের মাপ, স্বাভাবিক সীমা, পোশাক, সাইজ চার্ট
├── model/Diagrams.kt       নম্বরসহ ডায়াগ্রামের ডেটা (ছবির নাম + নম্বরের স্থানাঙ্ক)
└── util/BanglaDigits.kt    বাংলা অঙ্ক, ২৪½ ফরম্যাট, ইঞ্চি↔সেমি
shared/build.gradle.kts.snippet   framework-এর নাম "Shared"

androidApp/src/main/
├── res/drawable/           63টি VectorDrawable (আইকন, লোগো, শরীর, ডায়াগ্রাম, ইলাস্ট্রেশন)
├── res/font/               Hind Siliguri — FONTS.md দেখে ৪টি .ttf রাখুন
├── res/values/strings.xml  বাংলা (ডিফল্ট) — 339টি লেখা
├── res/values-en/strings.xml  ইংরেজি — একই key
└── java/com/tech_hunt/tailorpass/
    ├── ui/theme/           Color, Type (R.font), Dimens, Theme → MaapTheme { }
    ├── ui/diagram/NumberedDiagram.kt
    └── util/Strings.kt     shared-এর key → R.string / R.drawable

iosApp/
├── Resources/Assets.xcassets/
│   ├── Icons/              63টি SVG imageset (একই নাম; আইকনগুলো template — tint করা যায়)
│   └── Colors/             37টি colorset, প্রতিটিতে লাইট + ডার্ক মান
├── Resources/Localizable.xcstrings   বাংলা + ইংরেজি, key = Android-এর নাম
├── Resources/InfoPlist.xcstrings     অ্যাপের নাম "মাপ", ক্যামেরা অনুমতির লেখা
├── Theme/MaapColors.swift  Color.Maap.primary …
├── Theme/MaapFonts.swift   MaapFont.bodyMedium … (Dynamic Type সহ)
├── Theme/MaapDimens.swift  স্পেসিং, উচ্চতা, কোণ + প্রধান/বর্ডার বোতামের স্টাইল
├── Theme/L10n.swift        L10n.t("m_chest")
├── Theme/NumberedDiagram.swift
└── Info.plist.snippet.xml  UIAppFonts, bn/en, ক্যামেরা, শুধু portrait

design-tokens.json          সব রং/টাইপ/স্পেসিং এক জায়গায় (যাচাই বা অন্য টুলের জন্য)
```

## মূল ধারণা: এক key, দুই প্ল্যাটফর্ম

shared কোডে লেখা বা ছবি থাকে না, থাকে শুধু **নাম**। একই নাম দুই জায়গায় আছে:

| shared-এ | Android | iOS |
|---|---|---|
| `MeasurementKey.Chest.labelKey` = `"m_chest"` | `str("m_chest")` → `R.string.m_chest` | `L10n.t("m_chest")` |
| `Garment.Panjabi.iconName` = `"ic_garment_panjabi"` | `painterResource(ctx.drawableId(…))` | `Image("ic_garment_panjabi")` |
| `Diagrams.panjabi` | `NumberedDiagram(Diagrams.panjabi)` | `NumberedDiagram(spec: Diagrams.shared.panjabi)` |
| `formatInches(24.5f)` → `"২৪½"` | সরাসরি | `BanglaDigitsKt.formatInches(value: 24.5, useBangla: true)` |

UI-র নিজের লেখা (বোতাম, শিরোনাম) সরাসরি ব্যবহার করুন: Android-এ `stringResource(R.string.action_share)`, iOS-এ `Text("action_share")` বা `L10n.t("action_share")`।

## বসানোর ধাপ

**shared**
1. `shared/src/commonMain/...` ফাইলগুলো আপনার shared মডিউলে কপি করুন; প্যাকেজ `com.tech_hunt.tailorpass` (আগেই বদলানো)।
2. `build.gradle.kts`-এ framework `baseName = "Shared"` (অন্য নাম দিলে Swift-এর `import Shared` বদলান)।

**Android**
1. `res/` এর সব কিছু `androidApp/src/main/res/`-এ, `java/...` ফাইলগুলো আপনার প্যাকেজে কপি করুন।
2. ফন্ট নামিয়ে `res/font/`-এ রাখুন, নাম ছোট হাতে: `hind_siliguri_regular.ttf` ইত্যাদি (FONTS.md)।
3. অ্যাপ মোড়ান: `MaapTheme { App() }`।
4. release build-এ resource shrinking চালু থাকলে `getIdentifier` দিয়ে খোঁজা রিসোর্স মুছে যেতে পারে — তাই `res/raw/keep.xml` দেওয়া আছে:
   `<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="@string/m_*,@string/garment_*,@string/accuracy_*,@string/badge_*,@string/length_*,@string/depth_*,@string/fit_*,@string/gender_*,@string/manual_*,@drawable/*" />`

**iOS** (Xcode 15+, iOS 16+)
1. `Resources/` এর `Assets.xcassets`, `Localizable.xcstrings`, `InfoPlist.xcstrings` iosApp টার্গেটে টেনে আনুন (Copy items ✓)।
   আপনার আগের Assets.xcassets থাকলে `Icons/` ও `Colors/` ফোল্ডার দুটো তার ভেতরে রাখুন।
2. `Theme/*.swift` টার্গেটে যোগ করুন।
3. ৪টি ফন্ট .ttf টার্গেটে যোগ করুন (Target Membership ✓) এবং `Info.plist.snippet.xml`-এর অংশ Info.plist-এ দিন।
4. Project → Info → Localizations-এ Bangla (bn) যোগ করে Development Language বাংলা করুন।

## ব্যবহারের উদাহরণ

```kotlin
// Android (Compose)
Icon(painterResource(R.drawable.ic_share), stringResource(R.string.action_share), tint = MaterialTheme.colorScheme.primary)
Text(formatInches(24.5f) + "\"", style = MaapTheme.numbers.listValue)          // ২৪½"
Text(str(MeasurementKey.Chest.labelKey))                                       // বুক
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

## নিয়ম যা ডিজাইনে মানা হয়েছে

- মূল লেখা ≥১৬, চাপা যায় এমন জিনিস ≥৪৮, প্রধান বোতাম ৫৬ উঁচু পুরো প্রস্থ।
- প্রতিটি স্ক্রিনে একটিই প্রধান বোতাম (ভরাট); দ্বিতীয়টি বর্ডার দেওয়া বা লিংক।
- রং একা অর্থ বহন করে না — ব্যাজে চিহ্নও আছে (● উচ্চ, ◐ মাঝারি, ▲ নিম্ন)।
- সংখ্যা বাংলা অঙ্কে; ইংরেজি ভাষায় `useBangla = false`।
- ডার্ক মোড: Android-এ `MaapTheme(darkTheme = …)`, iOS-এ colorset নিজেই বদলায়।
- ক্যামেরা স্ক্রিন দুই মোডেই গাঢ় (`camera*` রং)।
- "স্ক্রল হয় কিন্তু বোতাম সবসময় দেখা যায়":
  Android — `Scaffold(bottomBar = { বোতাম })` + `LazyColumn`;
  iOS — `ScrollView { … }.safeAreaInset(edge: .bottom) { বোতাম }`।

## যা এখনো ঠিক করতে হবে

- `about_contact_email` — আসল ইমেইল।
- `SampleMenShirtChart`, মাপের `minInches/maxInches`, ঝুল/গলার গভীরতার নমুনা মান — আসল ডেটা দিয়ে।
- গোপনীয়তা নীতির লেখা — অ্যাপ সত্যিই এভাবে কাজ করে কিনা মিলিয়ে।
- নীল/সবুজ বিকল্প: iOS-এ `PrimaryGreen`, `PrimaryGreenContainer` রং দেওয়া আছে; সবুজ বেছে নিলে `Color.Maap.primary`-কে সেগুলোতে বদলান।
