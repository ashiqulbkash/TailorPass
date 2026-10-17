# মাপ — ডিজাইন হ্যান্ডঅফ প্যাকেজ (Kotlin Multiplatform)

প্ল্যাটফর্ম: **KMP** — shared মডিউলে Kotlin লজিক, Android-এ Jetpack Compose, iOS-এ SwiftUI।

| ফাইল/ফোল্ডার | কী আছে |
|---|---|
| `maap-design-screens.pdf` | ৫২টি স্ক্রিন, সেকশন অনুযায়ী ক্রমে (A4 আড়াআড়ি, ২১ পৃষ্ঠা, প্রথম পাতায় সূচি) |
| [`plan.md`](plan.md) | AI কোডিং এজেন্টের জন্য নির্দেশিকা ও ধাপে ধাপে পরিকল্পনা — **প্রথমে এটি দিন** |
| `SCREENS.md` | প্রতিটি স্ক্রিনের উদ্দেশ্য, কোন স্ক্রিনে যায়, স্ক্রিনের সব লেখা |
| `screens.json`, `navigation.json` | একই তথ্য মেশিন-পাঠযোগ্য আকারে + নেভিগেশন গ্রাফ |
| `screens/` | প্রতিটি স্ক্রিনের PNG (২×), নাম `ক্রম_নম্বর_আইডি.png` |
| `html/` | প্রতিটি স্ক্রিনের হুবহু HTML/CSS (ব্রাউজারে খোলা যায়) |
| `design-tokens.json` | রং, টাইপ, মাপ-জোক |
| `kmp-resources/` | KMP রিসোর্স — `shared/` (মডেল, ডায়াগ্রাম, বাংলা অঙ্ক), `androidApp/` (drawable, strings, Compose থিম), `iosApp/` (Assets.xcassets, xcstrings, SwiftUI থিম)। বিস্তারিত `kmp-resources/README.md` |

ফাইলের নাম ক্রমানুসারে: `01_1-1_Language` … `52_12-6_About`।

এজেন্টকে কাজ দেওয়ার প্রম্পট ও স্টেপ ভাগ `plan.md`-এ আছে।
