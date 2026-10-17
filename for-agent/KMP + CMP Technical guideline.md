# KMP + CMP টেকনিক্যাল গাইডলাইন

Oct 17, 2026 · @Md Ashiqul Islam

অ্যাপটি Kotlin Multiplatform + Compose Multiplatform দিয়ে বানানো হবে। কোডের প্রায় ৮০–৮৫% থাকবে `commonMain`-এ, আর প্ল্যাটফর্মের কাজ থাকবে মাত্র ছয়টি interface-এর পেছনে। প্রথম রিলিজ শুধু Android, তবে কোড শুরু থেকেই iOS-এর জন্য তৈরি থাকবে।

## ১. এজেন্টের জন্য মূল নিয়ম

1. **আগে common, পরে প্ল্যাটফর্ম।** নতুন কোড প্রথমে `commonMain`-এ লেখার চেষ্টা করো। প্ল্যাটফর্মে যাবে শুধু সেকশন ৫-এর ছয়টি interface-এর ইমপ্লিমেন্টেশন। এর বাইরে প্ল্যাটফর্ম কোড লাগলে আগে জিজ্ঞেস করো।
2. **`commonMain`-এ Android বা iOS-এর কিছু import হবে না।** `android.*`, `androidx` (শুধু KMP-সমর্থিত artifact ছাড়া), `platform.UIKit` ইত্যাদি `commonMain`-এ নিষেধ।
3. **লাইব্রেরি শুধু সেকশন ৪-এর তালিকা থেকে।** নতুন লাইব্রেরি যোগ করার আগে জিজ্ঞেস করো। সেটা যেন KMP-সমর্থিত হয় আর ইন্টারনেট ব্যবহার না করে।
4. **ভার্সন হার্ডকোড নয়।** সব ভার্সন `gradle/libs.versions.toml`-এ, সর্বশেষ stable ভার্সন। alpha বা beta নয়।
5. **ইন্টারনেট নেই।** কোনো নেটওয়ার্ক লাইব্রেরি, অ্যানালিটিক্স বা ক্র্যাশ-রিপোর্টিং SDK যোগ করবে না। Android Manifest-এ `INTERNET` পারমিশন থাকবে না।
6. **মাপ সবসময় সেন্টিমিটারে (`Double`) সংরক্ষিত।** ইঞ্চি শুধু দেখানোর সময় রূপান্তর হবে।
7. **ছবি স্থায়ীভাবে সেভ নয়।** ছবি থাকবে মেমরিতে বা অ্যাপের cache-এ, প্রসেসিং শেষ হলেই মুছে যাবে। গ্যালারিতে কখনো নয়।
8. **সব লেখা resource থেকে।** UI-তে সরাসরি বাংলা বা ইংরেজি স্ট্রিং লেখা যাবে না। সব আসবে `composeResources/values/strings.xml` (ইংরেজি) আর `values-bn/strings.xml` (বাংলা) থেকে।
9. **একবারে একটি ফিচার।** প্রতিটি ফিচার শেষে বিল্ড করো, টেস্ট চালাও, তারপর পরেরটা।

## ২. আর্কিটেকচার

&#91;embedded content: আর্কিটেকচার · commonMain আর দুই প্ল্যাটফর্ম\]

shared কোড কখনো প্ল্যাটফর্মকে সরাসরি ডাকে না, শুধু `platform/`-এর interface ডাকে। প্ল্যাটফর্ম অংশ শুধু সেই interface ইমপ্লিমেন্ট করে, কোনো ব্যবসায়িক সিদ্ধান্ত নেয় না।

## ৩. ফিচার ভাগ

২৩টি ফিচারের মধ্যে ১৩টি প্রায় পুরোপুরি common। ডেটাবেস ও সেটিংসে শুধু ফাইলের পাথ আলাদা। বাকি ১০টিতে শুধু হার্ডওয়্যার বা OS-এর অংশ প্ল্যাটফর্মে যায়, লজিক তবুও common।

| ফিচার | commonMain | Android (androidMain) | iOS (iosMain / Swift) |
| --- | --- | --- | --- |
| সব স্ক্রিন, শিট, ডায়ালগ (৫২টি ডিজাইন) | সব | — | — |
| নেভিগেশন, ViewModel, স্টেট | সব | — | — |
| প্রোফাইল, সংরক্ষিত মাপ, অসমাপ্ত মাপ (ডেটাবেস) | সব (Room) | ডেটাবেস ফাইলের পাথ | ডেটাবেস ফাইলের পাথ |
| সেটিংস (ভাষা, ইউনিট) | সব (DataStore) | ফাইলের পাথ | ফাইলের পাথ |
| ক্যাটাগরি ও মাপের তালিকা (JSON কনফিগ) | সব | — | — |
| ফিতা দিয়ে মাপ, অস্বাভাবিক মাপের সতর্কতা | সব | — | — |
| মাপের গণিত: স্কেল, ঘের, ক্যালিব্রেশন, ফিট অ্যালাউন্স | সব | — | — |
| নির্ভুলতা নির্দেশক (উচ্চ/মাঝারি/নিম্ন) | সব | — | — |
| সাইজ চার্ট ও সাইজ সাজেশন | সব | — | — |
| ব্যাকআপ/রিস্টোর JSON তৈরি ও পড়া | সব | — | — |
| বাংলা/ইংরেজি, বাংলা সংখ্যা, ইঞ্চি/সেমি | সব | — | — |
| ফলাফলের ডায়াগ্রাম (শরীরের ছবি, নম্বর) | সব (Compose Canvas) | — | — |
| ক্যামেরা প্রিভিউ, টাইমার, ওভারলে | UI ও টাইমার (Kamera) | Kamera ভেতরে CameraX | Kamera ভেতরে AVFoundation |
| ছবির মান যাচাই (আলো, ঝাপসা) | হিসাব | ছবি থেকে পিক্সেল বের করা | ছবি থেকে পিক্সেল বের করা |
| **AI পোজ ও শরীরের আউটলাইন** | ল্যান্ডমার্ক থেকে মাপ বের করা | MediaPipe Tasks Vision | MediaPipe (Swift wrapper, CocoaPods) |
| **PDF তৈরি** | লেআউট হিসাব (কোথায় কী বসবে) | `PdfDocument` + Canvas দিয়ে আঁকা | `UIGraphicsPDFRenderer` দিয়ে আঁকা |
| PNG ছবি হিসেবে শেয়ার | Compose থেকে `ImageBitmap` | PNG এনকোড | PNG এনকোড |
| শেয়ার শিট | কল করা | `Intent.ACTION_SEND` + FileProvider | `UIActivityViewController` |
| ব্যাকআপ ফাইল সেভ/খোলা | সব (FileKit) | — | — |
| ভয়েস গাইড (আগে রেকর্ড করা অডিও) | কোন অডিও কখন বাজবে | `MediaPlayer` | `AVAudioPlayer` |
| ক্যামেরা পারমিশন | পারমিশন স্ক্রিনের UI | সিস্টেম পারমিশন অনুরোধ | সিস্টেম পারমিশন অনুরোধ + Info.plist |
| ভার্সন ২: অ্যাপ লক | UI | androidx.biometric | LocalAuthentication |
| ভার্সন ৩: রিমাইন্ডার | কখন রিমাইন্ডার দেবে | WorkManager / AlarmManager | UNUserNotificationCenter |

## ৪. লাইব্রেরি তালিকা

এখানে দেওয়া ভার্সন অক্টোবর ২০২৬-এর সর্বশেষ stable; কাজ শুরুর সময় এজেন্ট নিজে সর্বশেষ stable যাচাই করে `libs.versions.toml`-এ লিখবে। ভার্সন কলামে "সর্বশেষ stable" মানে এখানে যাচাই করা হয়নি।

### commonMain

| কাজ | লাইব্রেরি | ভার্সন | নোট |
| --- | --- | --- | --- |
| UI | Compose Multiplatform (Material 3) | 1.12.x | iOS-এ stable মে ২০২৫ (1.8.0) থেকে |
| স্ক্রিনের লেখা, ফন্ট, ছবি, অডিও | Compose Multiplatform Resources | CMP-এর সাথে | `values-bn` দিয়ে বাংলা |
| নেভিগেশন | androidx Navigation Compose (KMP) | 2.10.x | type-safe route, `@Serializable` |
| ViewModel ও Lifecycle | androidx lifecycle-viewmodel-compose (KMP) | 2.11.x |  |
| ডেটাবেস | Room (KMP) + sqlite-bundled | 2.8.x | Android ও iOS-এ একই SQLite |
| সেটিংস | DataStore Preferences (KMP) | 1.2.x |  |
| DI | Koin (core + compose-viewmodel) | সর্বশেষ stable | Hilt KMP-তে চলে না, ব্যবহার করবে না |
| JSON (কনফিগ, ব্যাকআপ) | kotlinx.serialization | সর্বশেষ stable |  |
| তারিখ | kotlinx-datetime | সর্বশেষ stable | বাংলা মাসের নাম নিজে ফরম্যাট করবে |
| কোরুটিন | kotlinx.coroutines | সর্বশেষ stable |  |
| ক্যামেরা | Kamera (আগের নাম CameraK) | 1.0.x | জুন ২০২৬-এ 1.0; সমস্যা হলে সেকশন ৫-এর বিকল্প |
| ফাইল সেভ/খোলা, শেয়ার | FileKit (core + dialogs-compose) | সর্বশেষ stable | ব্যাকআপ/রিস্টোর; শেয়ার কাজ না করলে নিজের `FileSharer` |
| লগ | Kermit | সর্বশেষ stable | শুধু লোকাল লগ, কোথাও পাঠাবে না |
| টেস্ট | kotlin.test, kotlinx-coroutines-test | — |  |

### androidMain

| কাজ | লাইব্রেরি / API |
| --- | --- |
| পোজ ও আউটলাইন | `com.google.mediapipe:tasks-vision` (Pose Landmarker + Image Segmenter) |
| PDF | Android `android.graphics.pdf.PdfDocument` + `Canvas`, `StaticLayout` দিয়ে বাংলা লেখা |
| শেয়ার | `Intent.ACTION_SEND` + `androidx.core` FileProvider (cache ফোল্ডার) |
| অডিও | `MediaPlayer` |
| প্রবেশপথ | `MainActivity` (`ComponentActivity` + `setContent { MainView() }`) |
| ভার্সন ২–৩ | androidx.biometric, WorkManager |

### iosMain (Kotlin) ও iosApp (Swift)

| কাজ | লাইব্রেরি / API | কোথায় |
| --- | --- | --- |
| পোজ ও আউটলাইন | `MediaPipeTasksVision` pod (শুধু CocoaPods দিয়ে ইনস্টল হয়) | Swift (`iosApp`) |
| PDF | `UIGraphicsPDFRenderer` + `NSAttributedString` | Kotlin (`iosMain`) |
| শেয়ার | `UIActivityViewController` | Kotlin (`iosMain`) |
| অডিও | `AVAudioPlayer` | Kotlin (`iosMain`) |
| প্রবেশপথ | `MainViewController()` = `ComposeUIViewController { App() }` | Kotlin + Swift |
| ভার্সন ২–৩ | LocalAuthentication, UserNotifications | Kotlin (`iosMain`) |

### যেগুলো ব্যবহার করবে না

- **Hilt / Dagger:** KMP সাপোর্ট করে না।
- **Ktor, Retrofit, Firebase, Crashlytics, Analytics:** ইন্টারনেট বা ডেটা সংগ্রহ।
- **ML Kit-এর পোজ ডিটেকশন:** MediaPipe-এই থাকবে, যাতে দুই প্ল্যাটফর্মে একই মডেল ও একই ফলাফল আসে।
- **ভিন্ন কোনো PDF লাইব্রেরি:** অনেকগুলোতে বাংলা যুক্তাক্ষর ভেঙে যায়।
- **ভয়েস গাইডে সিস্টেম TTS:** iOS-এ বাংলা ভয়েস সব ফোনে নিশ্চিত নয়, তাই আগে রেকর্ড করা অডিও ব্যবহার হবে।

## ৫. প্ল্যাটফর্ম interface

প্ল্যাটফর্মের সব কাজ ছয়টি interface-এর পেছনে থাকবে। interface থাকবে `commonMain/platform/`-এ, আর Koin-এর `platformModule` দিয়ে প্রতিটি প্ল্যাটফর্মের ইমপ্লিমেন্টেশন দেওয়া হবে। `expect/actual` শুধু ছোট জিনিসের জন্য, যেমন ফাইলের পাথ বা Room builder।

```kotlin
// commonMain/platform/PoseAnalyzer.kt
interface PoseAnalyzer {
    /** একটি ছবি থেকে ৩৩টি ল্যান্ডমার্ক আর শরীরের মাস্ক বের করে। শরীর না পেলে null। */
    suspend fun analyze(image: CapturedImage): PoseResult?
    /** লাইভ গাইডের জন্য হালকা বিশ্লেষণ (মাস্ক ছাড়া) */
    suspend fun analyzeFrame(image: CapturedImage): List<Landmark>?
    fun close()
}

data class CapturedImage(val width: Int, val height: Int, val rgba: ByteArray)
data class Landmark(val x: Float, val y: Float, val z: Float, val visibility: Float) // 0..1 normalized
data class PoseResult(
    val landmarks: List<Landmark>,   // MediaPipe-এর ক্রমে ৩৩টি
    val maskWidth: Int, val maskHeight: Int,
    val personMask: FloatArray        // প্রতি পিক্সেলে মানুষ হওয়ার সম্ভাবনা 0..1
)

// commonMain/platform/PdfRenderer.kt
interface PdfRenderer {
    /** common-এ হিসাব করা লেআউট আঁকে, cache-এ PDF সেভ করে পাথ ফেরত দেয় */
    suspend fun render(page: ReportPage, fileName: String): String
}

// commonMain/platform/ImageEncoder.kt
interface ImageEncoder {
    suspend fun encodePng(bitmap: ImageBitmap, fileName: String): String // cache-এর পাথ
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

### প্রতিটি interface কোথায় ইমপ্লিমেন্ট হবে

| interface | Android | iOS |
| --- | --- | --- |
| `PoseAnalyzer` | `androidMain`, Kotlin, MediaPipe Tasks Vision | **Swift** (`iosApp`), MediaPipeTasksVision pod; Swift-এর MediaPipePoseBridge ছোট IosPoseBridge ইমপ্লিমেন্ট করে; iosMain-এর IosPoseAnalyzer সেটাকে মুড়ে PoseAnalyzer বানায় |
| `PdfRenderer` | `androidMain`, `PdfDocument` | `iosMain`, `UIGraphicsPDFRenderer` |
| `ImageEncoder` | `androidMain`, `Bitmap.compress(PNG)` | `iosMain`, `UIImagePNGRepresentation` |
| `FileSharer` | `androidMain`, Intent + FileProvider | `iosMain`, `UIActivityViewController` |
| `VoicePlayer` | `androidMain`, `MediaPlayer` | `iosMain`, `AVAudioPlayer` |
| `CameraPermission` | `androidMain`, Activity Result API | `iosMain`, `AVCaptureDevice.requestAccess` |

### নির্দিষ্ট নিয়ম

- **PoseAnalyzer:** শুধু ল্যান্ডমার্ক আর মাস্ক ফেরত দেবে। স্কেল, প্রস্থ, ঘের আর confidence-এর সব হিসাব `commonMain/measure/`-এ, যাতে দুই প্ল্যাটফর্মে ফলাফল হুবহু এক হয়। মডেল ফাইল (`.task`) দুই প্ল্যাটফর্মে একই ভার্সনের হবে।
- **PdfRenderer:** `ReportPage` হলো A4 (৫৯৫×৮৪২ pt) পৃষ্ঠার আঁকার উপকরণের তালিকা: `Text`, `Line`, `Rect`, `Circle`, `Path`। কোন লেখা কোথায় বসবে তা common-এর `ReportLayoutBuilder` বানাবে। প্ল্যাটফর্ম শুধু আঁকবে, কোনো সিদ্ধান নেবে না। শরীরের ডায়াগ্রামও `Path` হিসেবে common-এ সংজ্ঞায়িত, যাতে স্ক্রিন, PDF আর PNG—তিন জায়গায় একই ছবি আসে।
- **ক্যামেরা:** প্রথমে Kamera দিয়ে চেষ্টা করো। ছবি `CapturedImage` (মেমরিতে) হিসেবে নিয়ে আসবে। Kamera-তে লাইভ ফ্রেম বা টাইমারের চাহিদা পূরণ না হলে `expect fun CameraView(...)` composable বানাও: Android-এ CameraX, iOS-এ AVFoundation প্রিভিউ `UIKitView`-এ। এই সিদ্ধানের আগে জিজ্ঞেস করো।
- **ছবির মান যাচাই:** `CapturedImage.rgba` থেকে common-এ হিসাব: গড় উজ্জ্বলতা (আলো) আর Laplacian variance (ঝাপসা)। পুরো শরীর ফ্রেমে আছে কিনা, তা ল্যান্ডমার্ক থেকে।

### iOS-এ Swift ব্রিজ

Swift থেকে Kotlin-এর `suspend` ফাংশন বা `Flow` ইমপ্লিমেন্ট করা ঝামেলার। তাই Swift শুধু একটি সরল, সিঙ্ক্রোনাস interface ইমপ্লিমেন্ট করবে, আর `iosMain`-এর `IosPoseAnalyzer` সেটাকে `Dispatchers.Default`-এ চালিয়ে `PoseAnalyzer` বানাবে।

```kotlin
// shared/src/iosMain/kotlin/com/tech_hunt/tailorpass/platform/IosPoseBridge.kt  (Swift এটা ইমপ্লিমেন্ট করবে)
interface IosPoseBridge {
    fun load(poseModelPath: String, segmenterModelPath: String)
    /** সিঙ্ক্রোনাস। null = শরীর পাওয়া যায়নি */
    fun analyze(rgba: NSData, width: Int, height: Int, withMask: Boolean): IosPoseOutput?
    fun close()
}

class IosPoseOutput(
    val landmarks: FloatArray,      // ৩৩ × ৪ মান: x, y, z, visibility
    val maskWidth: Int,
    val maskHeight: Int,
    val mask: FloatArray?           // withMask = false হলে null
)

// shared/src/iosMain/kotlin/com/tech_hunt/tailorpass/di/KoinIos.kt
fun initKoin(poseBridge: IosPoseBridge) { /* startKoin { modules(common + iosPlatformModule(poseBridge)) } */ }
```

```swift
// iosApp/iosApp/iOSApp.swift
import SwiftUI
import shared

@main
struct iOSApp: App {
    init() { KoinIosKt.doInitKoin(poseBridge: MediaPipePoseBridge()) }
    var body: some Scene { WindowGroup { ContentView() } }
}
```

- Swift-এর দিকে যাওয়া interface-এ `suspend`, `Flow`, generic বা sealed class থাকবে না। শুধু সাধারণ টাইপ, `NSData`, `FloatArray`, `String`, `Int`।
- Kotlin-এর `initKoin` Swift-এ `doInitKoin` নামে দেখায় (Kotlin `init` দিয়ে শুরু নামের আগে `do` বসায়)।
- `ByteArray` থেকে `NSData` বানানো হবে `iosMain`-এ, `usePinned` দিয়ে একবারে কপি করে।

## ৬. প্রজেক্ট কাঠামো

প্রজেক্ট JetBrains-এর [chat উদাহরণের](https://github.com/JetBrains/compose-multiplatform/tree/master/examples/chat) কাঠামো মেনে চলবে। তিনটি অংশ থাকবে: `shared`, `androidApp` আর `iosApp`। `desktopApp` আর `jsApp` থাকবে না, `shared`-এ শুধু Android ও iOS টার্গেট। Android Studio-র KMP প্লাগইন দিয়ে বানানো প্রজেক্ট এই কাঠামোয় না থাকলে, অন্য কাজের আগে মডিউলগুলো এই নাম ও ভাগে সাজাও।

**প্যাকেজ নাম:** সব কোডের মূল প্যাকেজ `com.tech_hunt.tailorpass`। সাব-প্যাকেজ এর নিচে বসবে, যেমন `com.tech_hunt.tailorpass.measure`, `com.tech_hunt.tailorpass.ui.screens.home`। `shared`-এর Android namespace `com.tech_hunt.tailorpass.shared`, আর `androidApp`-এর namespace ও applicationId `com.tech_hunt.tailorpass`। প্লাগইন অন্য নাম দিয়ে থাকলে প্রজেক্টের বিদ্যমান নামই রাখো আর জিজ্ঞেস করো।

| অংশ | কী | Gradle প্লাগইন | এখানে যা থাকবে |
| --- | --- | --- | --- |
| `shared` | KMP লাইব্রেরি | `kotlinMultiplatform`, `androidMultiplatformLibrary` (`com.android.kotlin.multiplatform.library`), `composeMultiplatform`, `composeCompiler`; সাথে Room, KSP, kotlinx-serialization | সব UI, লজিক, ডেটা, resource, interface আর তাদের androidMain/iosMain ইমপ্লিমেন্টেশন। iOS-এর জন্য static framework `shared` |
| `androidApp` | Android অ্যাপের খোলস | `androidApplication`, `composeCompiler`, `composeMultiplatform` | `MainActivity`, `AndroidManifest.xml` (পারমিশন, FileProvider), লঞ্চার আইকন, applicationId, সাইনিং। কোনো ব্যবসায়িক লজিক নয় |
| `iosApp` | Xcode প্রজেক্ট | — | SwiftUI প্রবেশপথ, MediaPipe Swift wrapper, Podfile, Info.plist, `Config.xcconfig` |

```text
project/
├─ settings.gradle.kts                 # include(":androidApp", ":shared")
├─ build.gradle.kts                    # প্লাগইন apply false
├─ gradle.properties
├─ gradle/libs.versions.toml
├─ for-agent/                          # এজেন্টের জন্য ডক (গাইডলাইন, স্পেসিফিকেশন, ডিজাইন); অ্যাপের অংশ নয়
│
├─ androidApp/
│  ├─ build.gradle.kts                 # implementation(project(":shared"))
│  ├─ proguard-rules.pro               # R8: MediaPipe, kotlinx-serialization, Room-এর keep rule
│  └─ src/main/
│     ├─ AndroidManifest.xml           # CAMERA, FileProvider; INTERNET নয়
│     ├─ kotlin/com/tech_hunt/tailorpass/MainActivity.kt   # setContent { MainView() }
│     ├─ kotlin/com/tech_hunt/tailorpass/TailorPassApp.kt   # Application: shared-এর Koin চালু করে
│     └─ res/                          # লঞ্চার আইকন, xml/file_paths.xml, app_name
│
├─ shared/
│  ├─ build.gradle.kts                 # kotlin { android { namespace, minSdk } ; iosArm64(); iosSimulatorArm64() }
│  │                                   # framework { baseName = "shared"; isStatic = true }
│  └─ src/
│     ├─ commonMain/kotlin/com/tech_hunt/tailorpass/
│     │  ├─ App.kt                     # @Composable fun App(): থিম + নেভিগেশন
│     │  ├─ di/                        # Koin common মডিউল
│     │  ├─ ui/theme/ ui/components/ ui/diagram/
│     │  ├─ ui/screens/                # ডিজাইনের ১২ সেকশন:
│     │  │     onboarding/ home/ start/ prep/ camera/ check/
│     │  │     tape/ verify/ result/ share/ sizechart/ settings/
│     │  ├─ domain/                    # মডেল + use case
│     │  ├─ data/db/ data/prefs/ data/config/ data/backup/
│     │  ├─ measure/                   # স্কেল, ঘের, ক্যালিব্রেশন, confidence, ছবির মান
│     │  ├─ sizechart/  report/  format/
│     │  └─ platform/                  # ছয়টি interface (সেকশন ৫)
│     ├─ commonMain/composeResources/
│     │  ├─ values/strings.xml  values-bn/strings.xml
│     │  ├─ font/                      # Hind Siliguri
│     │  ├─ drawable/
│     │  └─ files/
│     │     ├─ config/garments.json  config/sizecharts.json
│     │     ├─ audio/bn/  audio/en/   # ভয়েস গাইড ক্লিপ
│     │     └─ models/                # pose_landmarker.task, segmenter মডেল (দুই প্ল্যাটফর্মে একই কপি)
│     ├─ commonTest/kotlin/com/tech_hunt/tailorpass/       # দুই প্ল্যাটফর্মেই চলে এমন টেস্ট (বেশিরভাগ টেস্ট এখানে)
│     ├─ androidHostTest/kotlin/com/tech_hunt/tailorpass/  # কম্পিউটারে (JVM) চলা Android টেস্ট, ফোন লাগে না
│     ├─ iosTest/kotlin/com/tech_hunt/tailorpass/          # iOS সিমুলেটরে চলা টেস্ট
│     ├─ androidMain/kotlin/com/tech_hunt/tailorpass/
│     │  ├─ main.android.kt            # @Composable fun MainView() = App()
│     │  ├─ di/PlatformModule.android.kt
│     │  └─ platform/                  # ছয়টি interface-এর Android ইমপ্লিমেন্টেশন
│     └─ iosMain/kotlin/com/tech_hunt/tailorpass/
│        ├─ main.ios.kt                # fun MainViewController(): UIViewController = ComposeUIViewController { App() }
│        ├─ di/KoinIos.kt              # fun initKoin(poseBridge: IosPoseBridge)
│        └─ platform/                  # IosPoseAnalyzer (bridge মোড়ানো), PDF, PNG, Share, Voice, Permission
│
└─ iosApp/
   ├─ Configuration/Config.xcconfig      # TEAM_ID, BUNDLE_ID, APP_NAME
   ├─ Podfile                            # শুধু MediaPipeTasksVision, iosApp টার্গেটে
   ├─ iosApp.xcodeproj                   # Build Phase: ./gradlew :shared:embedAndSignAppleFrameworkForXcode
   └─ iosApp/
      ├─ iOSApp.swift                    # init-এ KoinIosKt.doInitKoin(poseBridge: MediaPipePoseBridge())
      ├─ ContentView.swift               # ComposeViewControllerToSwiftUI().ignoresSafeArea()
      ├─ ComposeViewControllerToSwiftUI.swift  # UIViewControllerRepresentable → Main_iosKt.MainViewController()
      ├─ KotlinToSwiftHelper.swift       # Swift-এর জন্য Kotlin কলের মোড়ক (chat উদাহরণের মতো)
      ├─ MediaPipePoseBridge.swift       # import shared; IosPoseBridge ইমপ্লিমেন্ট করে
      └─ Info.plist                      # NSCameraUsageDescription
```

**এই কাঠামোর নিয়ম:**

- **প্রবেশপথ:** `androidApp`-এর `MainActivity` শুধু `MainView()` ডাকে। `iosApp`-এর `ContentView` শুধু `MainViewController()` দেখায়। দুটোই শেষ পর্যন্ত common-এর `App()` চালায়। chat উদাহরণের মতো SwiftUI ট্যাব বা নিজস্ব SwiftUI স্ক্রিন বানাবে না; পুরো অ্যাপ Compose।
- **পারমিশন ও FileProvider:** `shared` এখন লাইব্রেরি, তাই `CAMERA` পারমিশন আর `FileProvider`-এর ঘোষণা `androidApp`-এর Manifest-এ। FileProvider-এর authority কোডে হার্ডকোড না করে `androidApp` থেকে Koin-এ দাও।
- **iOS framework:** chat উদাহরণের মতোই `embedAndSignAppleFrameworkForXcode` দিয়ে সরাসরি যুক্ত হবে। Kotlin-এর CocoaPods প্লাগইন ব্যবহার করবে না। CocoaPods লাগবে শুধু `iosApp`-এ MediaPipe-এর জন্য, তাই Xcode-এ সবসময় `.xcworkspace` খুলবে।
- **মডেল ফাইল:** একটিই কপি, `composeResources/files/models/`-এ। Android-এ bytes পড়ে `BaseOptions.setModelAssetBuffer` (direct `ByteBuffer`)। iOS-এ Kotlin একবার cache-এ ফাইল লিখে পাথ bridge-কে দেয়।
- **`garments.json`:** প্রতিটি পোশাকের মাপের তালিকা, ফলাফল ও ফিতায় মাপার ক্রম, শরীর বা পছন্দের মাপ, কীভাবে মাপবেন, ভুল এড়াতে, বৈধ সীমা, ক্যালিব্রেশন গুণক আর ফিট অ্যালাউন্স এখানে। নতুন পোশাক যোগ করতে কোডে হাত দিতে হবে না (নতুন ডায়াগ্রাম ছাড়া)।

**টেস্ট কোথায় রাখবে:** যা দুই প্ল্যাটফর্মে একরকম (গণিত, সাইজ, ফরম্যাট, ব্যাকআপ, রিপোর্ট লেআউট), তা `commonTest`-এ। শুধু Android অংশের টেস্ট (যেমন `AndroidPdfRenderer`) `androidHostTest`-এ, iOS অংশের টেস্ট `iosTest`-এ। Room DAO-র টেস্ট প্রথমে `androidHostTest`-এ in-memory ডেটাবেস দিয়ে চেষ্টা করো; না চললে Android instrumented টেস্টে নাও।

**R8:** রিলিজ বিল্ডে minify চালু থাকলে MediaPipe-এর ক্লাস মুছে যেতে পারে। `proguard-rules.pro`-তে MediaPipe আর kotlinx-serialization-এর keep rule দাও, আর প্রতিটি রিলিজ বিল্ডে AI মাপ একবার চালিয়ে দেখো।

## ৭. বাংলা, গোপনীয়তা ও টেস্টিং

### বাংলা ও ভাষা

- **ফন্ট অ্যাপের ভেতরে।** Hind Siliguri `composeResources/font`-এ রাখো আর থিমের সব টাইপোগ্রাফিতে ব্যবহার করো। সিস্টেম ফন্টের উপর ভরসা নয়। PDF বানানোর সময়ও একই ফন্ট ফাইল প্ল্যাটফর্মকে দাও।
- **প্রথম দিনেই যুক্তাক্ষর পরীক্ষা।** একটি ডিবাগ স্ক্রিনে "ক্ষ ন্ত স্ত্র ঙ্গ র্ত্য পোশাকের গলার গভীরতা" দেখিয়ে Android ও iOS দুটোতেই স্ক্রিন, PDF আর PNG-তে যাচাই করো।
- **বাংলা সংখ্যা।** ভাষা বাংলা হলে সব সংখ্যা ০–৯ অঙ্কে, ইংরেজিতে 0–9। `format/` প্যাকেজের একটি ফাংশন দিয়ে করো, UI-তে সরাসরি নয়।
- **ইঞ্চি দেখানো।** নিকটতম ½ ইঞ্চিতে গোল করো (২৪½")। সেমি দেখানো হবে এক দশমিক পর্যন্ত। ফিতার ইনপুটে −/+ চাপলে ½ ইঞ্চি বা ১ সেমি করে বদলাবে।
- **ভাষা বদলানো।** অ্যাপের ভেতরে বাংলা/ইংরেজি টগল ফোনের ভাষা থেকে আলাদা, DataStore-এ সংরক্ষিত। পরে আরও ভাষা (আরবি, উর্দু) যোগ হতে পারে, তাই লেআউটে `start/end` ব্যবহার করো, `left/right` নয়।

### গোপনীয়তা

- Android Manifest: শুধু `CAMERA` পারমিশন। রিলিজ বিল্ডের merged manifest-এ `INTERNET` নেই, তা প্রতি রিলিজে যাচাই করো (কোনো লাইব্রেরি যোগ করলে এটা অজান্তে চলে আসতে পারে; দরকার হলে `tools:node="remove"`)।
- iOS Info.plist: `NSCameraUsageDescription` বাংলা ও ইংরেজিতে। App Store প্রাইভেসি লেবেল: কোনো ডেটা সংগ্রহ করা হয় না।
- ছবি `ByteArray` হিসেবে মেমরিতে; প্রসেসিং শেষে, বাতিল করলে, বা স্ক্রিন ছাড়লে সাথে সাথে ফেলে দাও। ডিস্কে লিখতে হলে শুধু cache, আর `finally` ব্লকে মুছে ফেলো।
- PDF/PNG cache-এ বানাও; অ্যাপ শুরুর সময় পুরনো শেয়ার ফাইল মুছে ফেলো।
- লগে নাম, উচ্চতা বা মাপের মান লিখবে না।
- "সব ডেটা মুছুন": Room-এর সব টেবিল, DataStore আর cache ফোল্ডার খালি।

### টেস্টিং

| কী | কোথায় | কীভাবে |
| --- | --- | --- |
| মাপের গণিত (স্কেল, ঘের, ফিট) | `commonTest` | জানা ল্যান্ডমার্ক + মাস্ক দিয়ে প্রত্যাশিত ফলাফল |
| সাইজ সাজেশন, বৈধ সীমার সতর্কতা | `commonTest` | সীমার দুই পাশের মান |
| বাংলা সংখ্যা, ½, ইঞ্চি/সেমি | `commonTest` | ৩৬.২৫ → "৩৬½" ইত্যাদি |
| ব্যাকআপ → রিস্টোর | `commonTest` | সব ডেটা হুবহু ফিরে আসে |
| Room DAO | Android instrumented / in-memory | সেভ, এডিট, মুছে ফেলা, অসমাপ্ত মাপ |
| রিপোর্ট লেআউট | `commonTest` | কোনো লেখা A4-এর বাইরে যায় না |
| AI নির্ভুলতা | আসল ফোন | ১০–২০ জনের ফিতার মাপের সাথে তুলনা, লক্ষ্য ঘেরে ±২ সেমি |
| গতি | কম দামি Android (৩–৪ GB RAM) | AI প্রসেসিং ১০ সেকেন্ডের মধ্যে |

## ৮. কাজের ক্রম

প্রথম ৯টি ধাপে Android রিলিজ, ১০ নম্বর ধাপে iOS। প্রতিটি ধাপ এজেন্টকে আলাদা কাজ হিসেবে দিন, আর প্রতিটি ধাপ শেষে Android বিল্ড আর `commonTest` পাস করতে হবে।

1. **প্রজেক্ট সেটআপ:** chat উদাহরণের কাঠামো (সেকশন ৬: shared, androidApp, iosApp; প্লাগইনের প্রজেক্ট আলাদা হলে আগে সেভাবে সাজাও), `libs.versions.toml`, Koin, থিম (ডিজাইনের রং ও ফন্ট, লাইট/ডার্ক), নেভিগেশনের কঙ্কাল, ছয়টি interface (Android-এ আপাতত খালি ইমপ্লিমেন্টেশন), বাংলা যুক্তাক্ষর টেস্ট স্ক্রিন। **iOS সিমুলেটরেও একবার চালিয়ে দেখো** যে কঙ্কালটা আর বাংলা লেখা সেখানে চলে।
2. **ডেটা ও কনফিগ:** Room এনটিটি (Profile, MeasurementSession, MeasurementValue + অসমাপ্ত অবস্থা, বাদ থাকা মাপ), DataStore, `garments.json` আর `sizecharts.json` পড়া, `format/`।
3. **প্রথমবার খোলা ও হোম (ডিজাইন সেকশন ১–২):** ভাষা, অনবোর্ডিং, প্রোফাইল, হোম, প্রোফাইল শিট, সংরক্ষিত মাপের মেনু।
4. **ফিতা দিয়ে মাপ ও যাচাই (সেকশন ৩, ৭, ৮):** পদ্ধতি বাছাই, ফিতার পুরো ফ্লো, মাপ যাচাই, এডিট শিট। এই ধাপের পর AI ছাড়াই অ্যাপ কাজের।
5. **ফলাফল, শেয়ার, সাইজ চার্ট (সেকশন ৯–১১):** ডায়াগ্রাম, `ReportLayoutBuilder`, Android `PdfRenderer`, `ImageEncoder`, `FileSharer`, সাইজ সাজেশন।
6. **সেটিংস (সেকশন ১২):** ব্যাকআপ/রিস্টোর (FileKit), সব ডেটা মুছুন, গোপনীয়তা নীতি, অ্যাপ সম্পর্কে।
7. **ক্যামেরা ও প্রস্তুতি (সেকশন ৪–৬):** চেকলিস্ট, পোজ গাইড, পারমিশন, Kamera, টাইমার, `VoicePlayer`, ছবির মান যাচাই।
8. **AI পরিমাপ:** Android `PoseAnalyzer`, `measure/` পাইপলাইন, confidence, আসল মানুষের উপর নির্ভুলতা যাচাই ও ক্যালিব্রেশন গুণক ঠিক করা।
9. **Android রিলিজের আগে:** অ্যাক্সেপ্ট্যান্স চেকলিস্ট (স্পেসিফিকেশন ডকের সেকশন ১০), merged manifest-এ INTERNET নেই, R8/ProGuard-এ MediaPipe ঠিকমতো চলছে।
10. **iOS:** Podfile + `MediaPipePoseBridge.swift`, iosMain-এ ছয়টি interface (PoseAnalyzer ব্রিজের মাধ্যমে), Info.plist, আসল iPhone-এ পুরো চেকলিস্ট আবার চালানো। এই ধাপে Mac, Xcode আর Apple Developer অ্যাকাউন্ট লাগবে।

## সূত্র

- [Kotlin Multiplatform: Jetpack লাইব্রেরি সাপোর্ট ও ভার্সন](https://developer.android.com/kotlin/multiplatform)
- [Compose Multiplatform 1.8.0: iOS stable](https://blog.jetbrains.com/kotlin/2025/05/compose-multiplatform-1-8-0-released-compose-multiplatform-for-ios-is-stable-and-production-ready/embed/)
- [Kamera 1.0 ঘোষণা](https://slack-chats.kotlinlang.org/t/34099386/hi-everyone-camerak-is-now-kamera-and-it-has-finally-reached)
- [MediaPipe Pose Landmarker: iOS](https://developers.google.com/edge/mediapipe/solutions/vision/pose_landmarker/ios)
- [MediaPipe Image Segmenter: iOS](https://developers.google.com/edge/mediapipe/solutions/vision/image_segmenter/ios)
