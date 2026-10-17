# মাপ — স্ক্রিন স্পেসিফিকেশন (৫২টি স্ক্রিন)

প্রতিটি স্ক্রিনের জন্য: ছবি (`screens/`), হুবহু HTML/CSS (`html/`), উদ্দেশ্য, নেভিগেশন আর স্ক্রিনের সব লেখা।
Each entry: rendered PNG, exact static HTML (inline CSS = source of truth for spacing/colours), purpose, navigation, and all visible text.

## ১. প্রথমবার খোলা — First launch

### ১.১ ভাষা বাছাই  ·  `Language`

- **Image:** `screens/01_1-1_Language.png`  ·  **HTML:** `html/01_1-1_Language.html`  ·  **Frame:** 360x800
- **Purpose:** First screen on install. Pick Bangla/English; privacy promise at bottom.
- **Navigates to:** `Onboarding1` (বাংলা Bangla); `Onboarding1` (English ইংরেজি)
- **Visible text:**

```
ভাষা বেছে নিন
Choose your language
বাংলা
Bangla
English
ইংরেজি
আপনার ছবি বা মাপ কোথাও পাঠানো হয় না
```

### ১.২ অনবোর্ডিং — কীভাবে কাজ করে  ·  `Onboarding1`

- **Image:** `screens/02_1-2_Onboarding1.png`  ·  **HTML:** `html/02_1-2_Onboarding1.html`  ·  **Frame:** 360x800
- **Purpose:** Onboarding 1/3: how AI measuring works (3 steps). Skip + Next; dot indicator.
- **Navigates to:** `ProfileCreate` (স্কিপ); `Onboarding2` (পরের ধাপ)
- **Visible text:**

```
বুক ৪০"
উচ্চতা
মাপ-এ স্বাগতম
কীভাবে কাজ করে
১
নিজের উচ্চতা লিখুন
২
সামনে ও পাশ থেকে দুটি ছবি তুলুন
৩
অ্যাপ নিজেই মাপ বের করে দেবে
ফিতা থাকলে নিজে মেপেও লিখতে পারবেন।
স্কিপ
পরের ধাপ
```

### ১.৩ অনবোর্ডিং — গোপনীয়তা  ·  `Onboarding2`

- **Image:** `screens/03_1-3_Onboarding2.png`  ·  **HTML:** `html/03_1-3_Onboarding2.html`  ·  **Frame:** 360x800
- **Purpose:** Onboarding 2/3: privacy (offline, nothing uploaded, delete anytime).
- **Navigates to:** `ProfileCreate` (স্কিপ); `Onboarding3` (পরের ধাপ)
- **Visible text:**

```
আপনার তথ্য আপনার ফোনেই
ইন্টারনেট ছাড়াই পুরো অ্যাপ চলে
ছবি ও মাপ কোথাও আপলোড হয় না
আপনি যখন খুশি সব মুছে ফেলতে পারেন
স্কিপ
পরের ধাপ
```

### ১.৪ অনবোর্ডিং — দর্জিকে শেয়ার  ·  `Onboarding3`

- **Image:** `screens/04_1-4_Onboarding3.png`  ·  **HTML:** `html/04_1-4_Onboarding3.html`  ·  **Frame:** 360x800
- **Purpose:** Onboarding 3/3: share to tailor as PDF/image; last slide has single "Get started".
- **Navigates to:** `ProfileCreate` (শুরু করুন)
- **Visible text:**

```
PDF
দর্জি
অ্যাপে আছে
দর্জিকে পাঠান সহজে
PDF বা ছবি হিসেবে হোয়াটসঅ্যাপ, মেসেঞ্জারে পাঠান
দর্জির জন্য নোটও সাথে যায়
পাঠানোর পরেও মাপ অ্যাপে থেকে যায়
শুরু করুন
```

### ১.৫ প্রোফাইল তৈরি  ·  `ProfileCreate`

- **Image:** `screens/05_1-5_ProfileCreate.png`  ·  **HTML:** `html/05_1-5_ProfileCreate.html`  ·  **Frame:** 360x800
- **Purpose:** Create profile: name, gender toggle, height ft-in/cm toggle, fit (slim/regular/loose) cards with illustrations.
- **Navigates to:** `Onboarding3` (পেছনে যান); `Main` (প্রোফাইল সেভ করুন)
- **Visible text:**

```
প্রোফাইল তৈরি
যার মাপ নেবেন, তার তথ্য দিন
নাম
লিঙ্গ
ছেলে
মেয়ে
উচ্চতা
ফুট-ইঞ্চি
সেমি
ফুট
ইঞ্চি
= প্রায় ১৭০ সেমি · জুতা ছাড়া মাপুন
কেমন ফিট পছন্দ?
স্লিম
রেগুলার
লুজ
প্রোফাইল সেভ করুন
```

## ২. হোম — Home

### ২.১ হোম  ·  `Main`

- **Image:** `screens/06_2-1_Main.png`  ·  **HTML:** `html/06_2-1_Main.html`  ·  **Frame:** 360x800
- **Purpose:** Home (tab). Profile switcher chip opens 2.4; garment grid (6) starts a measurement; saved list (tap=open result, share icon, long-press=2.5). Bottom nav: Home / Size chart / Settings.
- **Navigates to:** `Result` (পাঞ্জাবি ১৬ অক্টোবর ২০২৬ · সাইজ L); `Result` (শার্ট ২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠান); `Result` (প্যান্ট ১৫ আগস্ট ২০২৬ · কোমর ৩৪"); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
রা
কার মাপ?
রাহাত
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
৩টি
পাঞ্জাবি
১৬ অক্টোবর ২০২৬ · সাইজ L
শার্ট
২৮ সেপ্টেম্বর ২০২৬ ·
দর্জিকে পাঠানো
প্যান্ট
১৫ আগস্ট ২০২৬ · কোমর ৩৪"
হোম
সাইজ চার্ট
সেটিংস
```

### ২.২ হোম — ডার্ক  ·  `MainDark`

- **Image:** `screens/07_2-2_MainDark.png`  ·  **HTML:** `html/07_2-2_MainDark.html`  ·  **Frame:** 360x800
- **Purpose:** Dark-mode variant of 2.1.
- **Navigates to:** `ResultDark` (পাঞ্জাবি ১৬ অক্টোবর ২০২৬ · সাইজ L); `ResultDark` (শার্ট ২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠান); `ResultDark` (প্যান্ট ১৫ আগস্ট ২০২৬ · কোমর ৩৪"); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
রা
কার মাপ?
রাহাত
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
৩টি
পাঞ্জাবি
১৬ অক্টোবর ২০২৬ · সাইজ L
শার্ট
২৮ সেপ্টেম্বর ২০২৬ ·
দর্জিকে পাঠানো
প্যান্ট
১৫ আগস্ট ২০২৬ · কোমর ৩৪"
হোম
সাইজ চার্ট
সেটিংস
```

### ২.৩ হোম — খালি অবস্থা (আব্বু)  ·  `HomeEmpty`

- **Image:** `screens/08_2-3_HomeEmpty.png`  ·  **HTML:** `html/08_2-3_HomeEmpty.html`  ·  **Frame:** 360x800
- **Purpose:** Home empty state for a profile with no saved measurements (profile "আব্বু").
- **Navigates to:** `ProfileSheet` (প্রোফাইল বদলান, এখন আব্বু); `Method` (শার্ট ছেলেদের); `Method` (প্যান্ট ছেলেদের); `Method` (পাঞ্জাবি ছেলেদের); `Method` (পায়জামা ছেলেদের); `Method` (সালওয়ার কামিজ মেয়েদের); `Method` (গেঞ্জি সাইজ সাজেশন); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
আ
কার মাপ?
আব্বু
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
এখনো কোনো মাপ নেওয়া হয়নি।
উপরের কোনো পোশাক বেছে শুরু করুন।
হোম
সাইজ চার্ট
সেটিংস
```

### ২.৪ প্রোফাইল বদলানো শিট  ·  `ProfileSheet`

- **Image:** `screens/09_2-4_ProfileSheet.png`  ·  **HTML:** `html/09_2-4_ProfileSheet.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `Main` (dimmed scrim)
- **Purpose:** Bottom sheet over Home: choose profile (radio), edit icon per profile, "+ New profile".
- **Navigates to:** `Result` (পাঞ্জাবি ১৬ অক্টোবর ২০২৬ · সাইজ L); `Result` (শার্ট ২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠান); `Result` (প্যান্ট ১৫ আগস্ট ২০২৬ · কোমর ৩৪"); `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস); `ProfileCreate` (নতুন প্রোফাইল)
- **Visible text:**

```
রা
কার মাপ?
রাহাত
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
৩টি
পাঞ্জাবি
১৬ অক্টোবর ২০২৬ · সাইজ L
শার্ট
২৮ সেপ্টেম্বর ২০২৬ ·
দর্জিকে পাঠানো
প্যান্ট
১৫ আগস্ট ২০২৬ · কোমর ৩৪"
হোম
সাইজ চার্ট
সেটিংস
কার মাপ নেবেন?
রা
রাহাত
ছেলে · ৩টি মাপ
ফা
ফারহানা
মেয়ে · ২টি মাপ
আ
আব্বু
ছেলে · এখনো মাপ নেই
নতুন প্রোফাইল
```

### ২.৫ সংরক্ষিত মাপের মেনু  ·  `SavedMenuSheet`

- **Image:** `screens/10_2-5_SavedMenuSheet.png`  ·  **HTML:** `html/10_2-5_SavedMenuSheet.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `Main` (dimmed scrim)
- **Purpose:** Bottom sheet on long-press of a saved item: view/share, edit, measure again, delete (red).
- **Navigates to:** `Result` (পাঞ্জাবি ১৬ অক্টোবর ২০২৬ · সাইজ L); `Result` (শার্ট ২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠান); `Result` (প্যান্ট ১৫ আগস্ট ২০২৬ · কোমর ৩৪"); `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস); `Result` (দেখুন ও শেয়ার করুন); `Verify` (মাপ এডিট করুন); `Method` (আবার মাপ নিন)
- **Visible text:**

```
রা
কার মাপ?
রাহাত
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
৩টি
পাঞ্জাবি
১৬ অক্টোবর ২০২৬ · সাইজ L
শার্ট
২৮ সেপ্টেম্বর ২০২৬ ·
দর্জিকে পাঠানো
প্যান্ট
১৫ আগস্ট ২০২৬ · কোমর ৩৪"
হোম
সাইজ চার্ট
সেটিংস
পাঞ্জাবি
রাহাত · ১৬ অক্টোবর ২০২৬ · সাইজ L
দেখুন ও শেয়ার করুন
মাপ এডিট করুন
আবার মাপ নিন
মুছে ফেলুন
```

## ৩. মাপ নেওয়া শুরু — Start measuring

### ৩.১ পদ্ধতি বাছাই  ·  `Method`

- **Image:** `screens/11_3-1_Method.png`  ·  **HTML:** `html/11_3-1_Method.html`  ·  **Frame:** 360x800
- **Purpose:** Choose method: AI photos (primary, highlighted) or tape. Each card navigates.
- **Navigates to:** `Main` (পেছনে যান); `Checklist` (সহজ ছবি দিয়ে মাপ (AI) সামনে ও পাশ থেকে ); `ManualEntry` (ফিতা লাগবে ফিতা দিয়ে নিজে মাপুন ছবি দেখ)
- **Visible text:**

```
কীভাবে মাপ নেবেন?
পাঞ্জাবি · রাহাত
সহজ
ছবি দিয়ে মাপ (AI)
সামনে ও পাশ থেকে দুটি ছবি তুললেই অ্যাপ মাপ বের করে দেবে।
এটি বেছে নিন
ফিতা লাগবে
ফিতা দিয়ে নিজে মাপুন
ছবি দেখে দেখে একটি একটি করে মাপ লিখে দিন।
এটি বেছে নিন
যেভাবেই মাপ নিন, পরে যেকোনো মাপ হাতে ঠিক করা যাবে।
```

### ৩.২ ক্যামেরা পারমিশন  ·  `CameraPermission`

- **Image:** `screens/12_3-2_CameraPermission.png`  ·  **HTML:** `html/12_3-2_CameraPermission.html`  ·  **Frame:** 360x800
- **Purpose:** Pre-permission explainer before the OS camera prompt; secondary link to tape flow.
- **Navigates to:** `Method` (পেছনে যান); `PoseGuide` (অনুমতি দিন); `ManualEntry` (ফিতা দিয়ে মাপুন)
- **Visible text:**

```
ক্যামেরা ব্যবহারের অনুমতি
শুধু মাপ নেওয়ার জন্য। ছবি কোথাও সেভ বা পাঠানো হয় না, মাপ বের হলেই মুছে যায়।
পরের পর্দায় ফোন অনুমতি চাইলে "অনুমতি দিন" চাপুন
অনুমতি দিন
ফিতা দিয়ে মাপুন
```

## ৪. ছবি তোলার প্রস্তুতি — Photo preparation

### ৪.১ সতর্কতা চেকলিস্ট — পাঞ্জাবি  ·  `Checklist`

- **Image:** `screens/13_4-1_Checklist.png`  ·  **HTML:** `html/13_4-1_Checklist.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Pre-photo checklist (panjabi). 5 checkboxes; Continue disabled until all ticked. Garment tip card.
- **Navigates to:** `Method` (পেছনে যান)
- **Visible text:**

```
ছবি তোলার আগে
ঠিক থাকলে প্রতিটিতে টিক দিন
টাইট বা শরীরে লাগানো পোশাক
ঢিলা জামায় মাপ বড় আসে
ভালো আলো
দিনের আলো বা উজ্জ্বল ঘর
সাধারণ পটভূমি
খালি দেয়ালের সামনে দাঁড়ান
ফোন কোমরের উচ্চতায়
টেবিল বা তাকে সোজা করে রাখুন
সোজা হয়ে দাঁড়ান
পা একটু ফাঁক, হাত শরীর থেকে দূরে
পাঞ্জাবির জন্য
হাত শরীর থেকে একটু দূরে রাখুন, যাতে হাতা ও আর্মহোল মাপা যায়
৫টির মধ্যে ৩টি ঠিক আছে
এগিয়ে যান
সবগুলোতে টিক দিলে বোতাম চালু হবে
```

### ৪.২ সতর্কতা চেকলিস্ট — সালওয়ার কামিজ  ·  `ChecklistFemale`

- **Image:** `screens/14_4-2_ChecklistFemale.png`  ·  **HTML:** `html/14_4-2_ChecklistFemale.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Same checklist for salwar kameez with female-specific item and tip.
- **Navigates to:** `Method` (পেছনে যান)
- **Visible text:**

```
ছবি তোলার আগে
সালওয়ার কামিজ · ফারহানা
শরীরে লাগানো পোশাক
ওড়না ও ঢিলা পোশাক সরিয়ে রাখুন
ভালো আলো
দিনের আলো বা উজ্জ্বল ঘর
সাধারণ পটভূমি
খালি দেয়ালের সামনে দাঁড়ান
ফোন কোমরের উচ্চতায়
টেবিল বা তাকে সোজা করে রাখুন
সোজা হয়ে দাঁড়ান
পা একটু ফাঁক, হাত শরীর থেকে দূরে
সালওয়ার কামিজের জন্য
চুল বেঁধে বা পিঠের দিকে রাখুন, যাতে কাঁধ ও গলা দেখা যায়
৫টির মধ্যে ২টি ঠিক আছে
এগিয়ে যান
সবগুলোতে টিক দিলে বোতাম চালু হবে
```

### ৪.৩ পোজ গাইড — ছেলে  ·  `PoseGuide`

- **Image:** `screens/15_4-3_PoseGuide.png`  ·  **HTML:** `html/15_4-3_PoseGuide.html`  ·  **Frame:** 360x800
- **Purpose:** Pose guide (male): front + side pose diagrams, 2–3 m distance diagram, tips; Open camera.
- **Navigates to:** `Checklist` (পেছনে যান); `Camera` (ক্যামেরা চালু করুন)
- **Visible text:**

```
এভাবে দাঁড়ান
দুটি ছবি, দুটি পোজ
১. সামনে থেকে
ক্যামেরার দিকে মুখ করে
২. পাশ থেকে
ডান দিকে ঘুরে দাঁড়ান
কোমরের উচ্চতা
২–৩ মিটার
মানে প্রায় ৭–১০ ফুট, বা ৪–৫ কদম দূরে
হাত শরীর থেকে একটু দূরে রাখুন
কখন ঘুরতে হবে, ভয়েস গাইড বলে দেবে
ক্যামেরা চালু করুন
```

### ৪.৪ পোজ গাইড — মেয়ে  ·  `PoseGuideFemale`

- **Image:** `screens/16_4-4_PoseGuideFemale.png`  ·  **HTML:** `html/16_4-4_PoseGuideFemale.html`  ·  **Frame:** 360x800
- **Purpose:** Pose guide (female) variant.
- **Navigates to:** `ChecklistFemale` (পেছনে যান); `CameraFemale` (ক্যামেরা চালু করুন)
- **Visible text:**

```
এভাবে দাঁড়ান
সালওয়ার কামিজ · ফারহানা
১. সামনে থেকে
ক্যামেরার দিকে মুখ করে
২. পাশ থেকে
ডান দিকে ঘুরে দাঁড়ান
কোমরের উচ্চতা
২–৩ মিটার
মানে প্রায় ৭–১০ ফুট, বা ৪–৫ কদম দূরে
হাত শরীর থেকে একটু দূরে রাখুন
চুল বেঁধে বা পিঠের দিকে রাখুন, যাতে কাঁধ দেখা যায়
ক্যামেরা চালু করুন
```

## ৫. ক্যামেরা — Camera

### ৫.১ সামনের ছবি ১/২ — ছেলে  ·  `Camera`

- **Image:** `screens/17_5-1_Camera.png`  ·  **HTML:** `html/17_5-1_Camera.html`  ·  **Frame:** 360x800
- **Purpose:** Full-screen camera: dashed body outline overlay, step pill 1/2, voice-guide toggle, top instruction banner (orange = correction), 10 s timer, start button.
- **Navigates to:** `Main` (বন্ধ করুন)
- **Visible text:**

```
সামনের ছবি ১/২
আরেকটু পেছনে যান
মাথা থেকে পা পর্যন্ত দাগের ভেতরে আনুন
১০
১০ সেকেন্ডের টাইমার
ফোন দেয়ালে ঠেস দিয়ে রাখুন, তারপর সোজা দাঁড়ান
ছবি তোলা শুরু করুন
```

### ৫.২ সামনের ছবি ১/২ — ডার্ক  ·  `CameraDark`

- **Image:** `screens/18_5-2_CameraDark.png`  ·  **HTML:** `html/18_5-2_CameraDark.html`  ·  **Frame:** 360x800
- **Purpose:** Dark-mode camera variant.
- **Navigates to:** `MainDark` (বন্ধ করুন)
- **Visible text:**

```
সামনের ছবি ১/২
আরেকটু পেছনে যান
মাথা থেকে পা পর্যন্ত দাগের ভেতরে আনুন
১০
১০ সেকেন্ডের টাইমার
ফোন দেয়ালে ঠেস দিয়ে রাখুন, তারপর সোজা দাঁড়ান
ছবি তোলা শুরু করুন
```

### ৫.৩ সামনের ছবি ১/২ — মেয়ে  ·  `CameraFemale`

- **Image:** `screens/19_5-3_CameraFemale.png`  ·  **HTML:** `html/19_5-3_CameraFemale.html`  ·  **Frame:** 360x800
- **Purpose:** Camera with female outline; banner "move arms out".
- **Navigates to:** `Main` (বন্ধ করুন)
- **Visible text:**

```
সামনের ছবি ১/২
হাত একটু দূরে রাখুন
কোমর ও হিপের দুই পাশ যেন দেখা যায়
১০
১০ সেকেন্ডের টাইমার
ফারহানা · উচ্চতা ৫ ফুট ৩ ইঞ্চি
ছবি তোলা শুরু করুন
```

### ৫.৪ পাশের ছবি ২/২  ·  `CameraSide`

- **Image:** `screens/20_5-4_CameraSide.png`  ·  **HTML:** `html/20_5-4_CameraSide.html`  ·  **Frame:** 360x800
- **Purpose:** Camera step 2/2: side outline, neutral instruction banner "turn right".
- **Navigates to:** `Main` (বন্ধ করুন)
- **Visible text:**

```
পাশের ছবি ২/২
ডান দিকে ঘুরে দাঁড়ান
হাত সোজা নিচে, দাগের ভেতরে থাকুন
১০
১০ সেকেন্ডের টাইমার
সামনের ছবি নেওয়া হয়েছে। এবার পাশ থেকে।
ছবি তোলা শুরু করুন
```

## ৬. ছবি যাচাই ও প্রসেসিং — Photo check & processing

### ৬.১ ছবি যাচাই — সফল ও প্রসেসিং  ·  `PhotoCheckSuccess`

- **Image:** `screens/21_6-1_PhotoCheckSuccess.png`  ·  **HTML:** `html/21_6-1_PhotoCheckSuccess.html`  ·  **Frame:** 360x800
- **Purpose:** Both photos OK (green), processing progress bar + step list; cancel.
- **Navigates to:** `Camera` (পেছনে যান); `Method` (বাতিল করুন)
- **Visible text:**

```
ছবি যাচাই
পাঞ্জাবি · রাহাত
দুটি ছবিই ঠিক আছে
সামনের ছবি
পাশের ছবি
মাপ বের করা হচ্ছে…
৬৮%
শরীরের আকার চেনা হয়েছে
উচ্চতার সাথে মেলানো হয়েছে
মাপ হিসাব হচ্ছে
সাইজ সাজেশন
ইন্টারনেট লাগছে না, সব হিসাব ফোনেই হচ্ছে
বাতিল করুন
```

### ৬.২ ছবি যাচাই — সমস্যা  ·  `PhotoCheckProblem`

- **Image:** `screens/22_6-2_PhotoCheckProblem.png`  ·  **HTML:** `html/22_6-2_PhotoCheckProblem.html`  ·  **Frame:** 360x800
- **Purpose:** Photo rejected: orange reason card, checklist of what passed/failed, Retake.
- **Navigates to:** `Camera` (পেছনে যান); `Camera` (আবার তুলুন)
- **Visible text:**

```
ছবি যাচাই
সামনের ছবি ১/২
আলো কম
ছবি অন্ধকার, আলো বাড়ান
শরীরের কিনারা পরিষ্কার দেখা যাচ্ছে না। ঘরের লাইট জ্বালান বা জানালার দিকে মুখ করে দাঁড়ান।
পুরো শরীর ফ্রেমের ভেতরে আছে
সোজা হয়ে দাঁড়ানো ঠিক আছে
আলো যথেষ্ট নয়
আবার তুলুন
```

### ৬.৩ মাপ বের করা যায়নি  ·  `AIFailed`

- **Image:** `screens/23_6-3_AIFailed.png`  ·  **HTML:** `html/23_6-3_AIFailed.html`  ·  **Frame:** 360x800
- **Purpose:** AI could not measure: reasons list; primary Retake, secondary Measure with tape.
- **Navigates to:** `Main` (হোমে ফিরুন); `Camera` (আবার ছবি তুলুন); `ManualEntry` (ফিতা দিয়ে মাপুন)
- **Visible text:**

```
পাঞ্জাবি · রাহাত
মাপ বের করা যায়নি
চিন্তা করবেন না, আবার ছবি তুললেই সাধারণত ঠিক হয়ে যায়।
সম্ভাব্য কারণ
শরীরের কিছু অংশ (মাথা বা পা) ফ্রেমের বাইরে ছিল
জামা অনেক ঢিলা, শরীরের আকার বোঝা যায়নি
আলো কম, বা পেছনে অনেক জিনিস ছিল
আবার ছবি তুলুন
ফিতা দিয়ে মাপুন
```

## ৭. ফিতা দিয়ে মাপ — Tape measuring

### ৭.১ প্রস্তুতি  ·  `ManualPrep`

- **Image:** `screens/24_7-1_ManualPrep.png`  ·  **HTML:** `html/24_7-1_ManualPrep.html`  ·  **Frame:** 360x800
- **Purpose:** Tape flow intro: what you need, helper advice, one-finger tightness illustration, unit toggle, Start.
- **Navigates to:** `Method` (পেছনে যান); `ManualList` (শুরু করুন)
- **Visible text:**

```
ফিতা দিয়ে মাপার আগে
পাঞ্জাবি · রাহাত
যা লাগবে: মাপার ফিতা (দর্জির নরম ফিতা)
পারলে কাউকে পাশে রাখুন, পুট ও হাতা নিজে মাপা কঠিন
পাতলা জামা পরুন, সোজা হয়ে দাঁড়ান
ফিতা টানটানও না, ঢিলাও না, ভেতরে এক আঙুল ঢুকবে
মাপের একক
ইঞ্চি
সেমি
শুরু করুন
```

### ৭.২ মাপের তালিকা  ·  `ManualList`

- **Image:** `screens/25_7-2_ManualList.png`  ·  **HTML:** `html/25_7-2_ManualList.html`  ·  **Frame:** 360x800
- **Purpose:** Tape flow overview: body vs preference measurements, done/now/pending states, "helper needed" badges; rows jump to that measurement.
- **Navigates to:** `ManualPrep` (পেছনে যান); `ManualEntry` (১ গলা (Neck) ১৫½"); `ManualEntry` (২ পুট (Shoulder) সাহায্যকারী লাগবে ১৮"); `ManualEntry` (৩ বুক (Chest) এখন); `ManualEntry` (৪ পেট (Waist) বাকি); `ManualEntry` (৫ হিপ (Hip) বাকি); `ManualSleeve` (৬ হাতা (Sleeve) সাহায্যকারী লাগবে বাকি); `ManualLength` (৭ হাতার মুখ (Cuff) বাকি); `ManualLength` (৮ ঝুল (Length) বাকি); `ManualEntry` (চালিয়ে যান (৩/৮))
- **Visible text:**

```
৮টি মাপ নিতে হবে
প্রায় ৫ মিনিট · পাঞ্জাবি · রাহাত
শরীরের মাপ
৬টি
১
গলা (Neck)
১৫½"
২
পুট (Shoulder)
সাহায্যকারী লাগবে
১৮"
৩
বুক (Chest)
এখন
৪
পেট (Waist)
বাকি
৫
হিপ (Hip)
বাকি
৬
হাতা (Sleeve)
সাহায্যকারী লাগবে
বাকি
পছন্দের মাপ
২টি
৭
হাতার মুখ (Cuff)
বাকি
৮
ঝুল (Length)
বাকি
যেকোনো মাপে চাপ দিলে সেটিতে চলে যাবে
চালিয়ে যান (৩/৮)
```

### ৭.৩ শরীরের মাপ — বুক  ·  `ManualEntry`

- **Image:** `screens/26_7-3_ManualEntry.png`  ·  **HTML:** `html/26_7-3_ManualEntry.html`  ·  **Frame:** 360x800
- **Purpose:** Tape entry (chest, 3/8): highlighted diagram, how-to, collapsible "avoid mistakes" (closed), big number field with −/+ (0.5"), Later + Next. Back left, list icon right.
- **Navigates to:** `ManualList` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualList` (পরে দেব); `ManualSleeve` (পরের মাপ)
- **Visible text:**

```
ফিতা দিয়ে মাপ
পাঞ্জাবি · রাহাত
৩/৮
বগলের নিচ দিয়ে
বুক (Chest)
বগলের নিচ দিয়ে, বুকের সবচেয়ে চওড়া জায়গায় ফিতা ঘুরিয়ে মাপুন। শ্বাস স্বাভাবিক রাখুন।
ভুল এড়াতে
ইঞ্চি
চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি
পরে দেব
পরের মাপ
```

### ৭.৪ শরীরের মাপ — হাতা (সাহায্যকারী লাগবে)  ·  `ManualSleeve`

- **Image:** `screens/27_7-4_ManualSleeve.png`  ·  **HTML:** `html/27_7-4_ManualSleeve.html`  ·  **Frame:** 360x800
- **Purpose:** Tape entry (sleeve, 6/8): helper banner, sleeve path diagram, "avoid mistakes" expanded with 2 tips.
- **Navigates to:** `ManualEntry` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualList` (পরে দেব); `ManualLength` (পরের মাপ)
- **Visible text:**

```
ফিতা দিয়ে মাপ
পাঞ্জাবি · রাহাত
৬/৮
এটি কাউকে দিয়ে মাপালে সঠিক হবে
কাঁধ
কনুই
কব্জি
হাতা (Sleeve)
কাঁধের হাড় থেকে কনুই হয়ে কব্জি পর্যন্ত মাপুন।
ভুল এড়াতে
হাত পুরো সোজা রাখবেন না, একটু ভাঁজ করুন
কাঁধের হাড়ের কিনারা থেকে মাপ শুরু করুন
ইঞ্চি
চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি
পরে দেব
পরের মাপ
```

### ৭.৫ অস্বাভাবিক মাপের সতর্কতা  ·  `ManualWarnSheet`

- **Image:** `screens/28_7-5_ManualWarnSheet.png`  ·  **HTML:** `html/28_7-5_ManualWarnSheet.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `ManualEntry` (dimmed scrim)
- **Purpose:** Outlier warning bottom sheet over 7.3 (value 14" outside 30–50"): Measure again / Yes it is correct.
- **Navigates to:** `ManualList` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualList` (পরে দেব); `ManualSleeve` (পরের মাপ); `ManualEntry` (আবার মাপব)
- **Visible text:**

```
ফিতা দিয়ে মাপ
পাঞ্জাবি · রাহাত
৩/৮
বগলের নিচ দিয়ে
বুক (Chest)
বগলের নিচ দিয়ে, বুকের সবচেয়ে চওড়া জায়গায় ফিতা ঘুরিয়ে মাপুন। শ্বাস স্বাভাবিক রাখুন।
ভুল এড়াতে
ইঞ্চি
চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি
পরে দেব
পরের মাপ
মাপটা ঠিক আছে তো?
বুকের মাপ সাধারণত ৩০"–৫০" হয়।
আপনি লিখেছেন ১৪"
আবার মাপব
হ্যাঁ, ঠিক আছে
```

### ৭.৬ পছন্দের মাপ — ঝুল  ·  `ManualLength`

- **Image:** `screens/29_7-6_ManualLength.png`  ·  **HTML:** `html/29_7-6_ManualLength.html`  ·  **Frame:** 360x800
- **Purpose:** Preference: length 8/8 — three picture cards (above knee/at knee/below knee) with values from height, editable number, tip, Finish.
- **Navigates to:** `ManualSleeve` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualSummary` (শেষ করুন)
- **Visible text:**

```
ফিতা দিয়ে মাপ
পাঞ্জাবি · রাহাত
✎ পছন্দের মাপ
ঝুল (Length) · ৮/৮
পাঞ্জাবি কতটা লম্বা চান? কাঁধ থেকে নিচে মাপা হয়।
হাঁটুর ওপরে
৩৮"
হাঁটু পর্যন্ত
৪২"
হাঁটুর নিচে
৪৫"
হাঁটু · আপনার উচ্চতা (৫ ফুট ৭ ইঞ্চি) অনুযায়ী হিসাব
ইঞ্চি
পছন্দের পুরনো পাঞ্জাবি থাকলে সেটার ঝুল মেপে লিখুন
শেষ করুন
```

### ৭.৭ সারসংক্ষেপ  ·  `ManualSummary`

- **Image:** `screens/30_7-7_ManualSummary.png`  ·  **HTML:** `html/30_7-7_ManualSummary.html`  ·  **Frame:** 360x800
- **Purpose:** Summary of all 8 values with edit; missing hip row in orange with "Measure now"; banner; primary Review, link Skip.
- **Navigates to:** `ManualLength` (পেছনে যান); `ManualList` (গলা (Neck) ১৫½"); `ManualList` (পুট (Shoulder) ১৮"); `ManualEntry` (বুক (Chest) ৪০"); `ManualList` (পেট (Waist) ৩৬"); `ManualEntry` (এখন মাপুন); `ManualSleeve` (হাতা (Sleeve) ২৪½"); `ManualList` (হাতার মুখ (Cuff) ১০"); `ManualLength` (ঝুল (Length) ৪২"); `VerifyManual` (যাচাই করে ফলাফল দেখুন); `VerifyManual` (বাদ রেখেই এগিয়ে যান)
- **Visible text:**

```
সব মাপ একনজরে
পাঞ্জাবি · রাহাত · ইঞ্চি
১টি মাপ বাদ আছে। বাদ রাখলে দর্জিকে মেপে নিতে বলা হবে।
গলা (Neck)
১৫½"
পুট (Shoulder)
১৮"
বুক (Chest)
৪০"
পেট (Waist)
৩৬"
হিপ (Hip)
বাদ আছে
এখন মাপুন
হাতা (Sleeve)
২৪½"
হাতার মুখ (Cuff)
১০"
ঝুল (Length)
৪২"
যাচাই করে ফলাফল দেখুন
বাদ রেখেই এগিয়ে যান
```

### ৭.৮ মাঝপথে বের হওয়া  ·  `ManualExitDialog`

- **Image:** `screens/31_7-8_ManualExitDialog.png`  ·  **HTML:** `html/31_7-8_ManualExitDialog.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `ManualEntry` (dimmed scrim)
- **Purpose:** Dialog over 7.3 when leaving mid-flow: progress is saved; Keep going / Later.
- **Navigates to:** `ManualList` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualList` (পরে দেব); `ManualSleeve` (পরের মাপ); `HomeIncomplete` (পরে করব); `ManualEntry` (চালিয়ে যান)
- **Visible text:**

```
ফিতা দিয়ে মাপ
পাঞ্জাবি · রাহাত
৩/৮
বগলের নিচ দিয়ে
বুক (Chest)
বগলের নিচ দিয়ে, বুকের সবচেয়ে চওড়া জায়গায় ফিতা ঘুরিয়ে মাপুন। শ্বাস স্বাভাবিক রাখুন।
ভুল এড়াতে
ইঞ্চি
চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি
পরে দেব
পরের মাপ
মাপ নেওয়া থামাবেন?
যে ৪টি মাপ নিয়েছেন তা সেভ থাকবে। পরে হোম থেকে বাকিটা চালিয়ে যেতে পারবেন।
পরে করব
চালিয়ে যান
```

### ৭.৯ হোম — অসমাপ্ত মাপ  ·  `HomeIncomplete`

- **Image:** `screens/32_7-9_HomeIncomplete.png`  ·  **HTML:** `html/32_7-9_HomeIncomplete.html`  ·  **Frame:** 360x800
- **Purpose:** Home with an incomplete measurement card at top (orange left border, 4/8 progress, Continue).
- **Navigates to:** `ProfileSheet` (প্রোফাইল বদলান, এখন রাহাত); `Method` (শার্ট ছেলেদের); `Method` (প্যান্ট ছেলেদের); `Method` (পাঞ্জাবি ছেলেদের); `Method` (পায়জামা ছেলেদের); `Method` (সালওয়ার কামিজ মেয়েদের); `Method` (গেঞ্জি সাইজ সাজেশন); `ManualList` (চালিয়ে যান); `Result` (পাঞ্জাবি ১৬ অক্টোবর ২০২৬ · সাইজ L); `Result` (শার্ট ২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠান); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
রা
কার মাপ?
রাহাত
নতুন মাপ নিন
শার্ট
ছেলেদের
প্যান্ট
ছেলেদের
পাঞ্জাবি
ছেলেদের
পায়জামা
ছেলেদের
সালওয়ার কামিজ
মেয়েদের
গেঞ্জি
সাইজ সাজেশন
সংরক্ষিত মাপ
৪টি
পাঞ্জাবি · অসমাপ্ত
৪/৮ মাপ নেওয়া হয়েছে
চালিয়ে যান
পাঞ্জাবি
১৬ অক্টোবর ২০২৬ · সাইজ L
শার্ট
২৮ সেপ্টেম্বর ২০২৬ · দর্জিকে পাঠানো
হোম
সাইজ চার্ট
সেটিংস
```

### ৭.১০ পছন্দের মাপ — গলার গভীরতা (মেয়ে)  ·  `ManualNeckDepth`

- **Image:** `screens/33_7-10_ManualNeckDepth.png`  ·  **HTML:** `html/33_7-10_ManualNeckDepth.html`  ·  **Frame:** 360x800
- **Purpose:** Preference (female): front neck depth with 3 picture cards (5/7/9"), editable number.
- **Navigates to:** `ManualList` (পেছনে যান); `ManualList` (সব মাপের তালিকা); `ManualList` (পরে দেব); `ManualList` (পরের মাপ)
- **Visible text:**

```
ফিতা দিয়ে মাপ
সালওয়ার কামিজ · ফারহানা
৮/১৪
✎ পছন্দের মাপ
গলার গভীরতা — সামনে
কাঁধ ও গলার জোড় থেকে সোজা নিচে কতটা গভীর চান?
কম
৫"
মাঝারি
৭"
বেশি
৯"
ইঞ্চি
চাপ দিয়ে লিখুন · −/+ আধা ইঞ্চি
পরে দেব
পরের মাপ
```

## ৮. মাপ যাচাই ও এডিট — Review & edit

### ৮.১ মাপ যাচাই — পাঞ্জাবি  ·  `Verify`

- **Image:** `screens/34_8-1_Verify.png`  ·  **HTML:** `html/34_8-1_Verify.html`  ·  **Frame:** 360x800
- **Purpose:** Review AI measurements (panjabi, 8 rows) with accuracy badges; tap row to edit (8.3); tailor note; See result.
- **Navigates to:** `Camera` (পেছনে যান); `Result` (ফলাফল দেখুন)
- **Visible text:**

```
মাপ যাচাই করুন
পাঞ্জাবি · রাহাত
কোনো মাপ ভুল মনে হলে সেটিতে ট্যাপ করে ঠিক করুন
ঝুল (Length)
● নির্ভুলতা উচ্চ
৪২"
বুক (Chest)
● নির্ভুলতা উচ্চ
৪০"
পেট (Waist)
◐ নির্ভুলতা মাঝারি
৩৬"
হিপ (Hip)
◐ নির্ভুলতা মাঝারি
৪১"
পুট (Shoulder)
● নির্ভুলতা উচ্চ
১৮"
হাতা (Sleeve)
◐ নির্ভুলতা মাঝারি
২৪½"
হাতার মুখ (Cuff)
◐ নির্ভুলতা মাঝারি
১০"
গলা (Neck)
▲ নিম্ন · ফিতা দিয়ে মেপে নিন
১৫½"
দর্জির জন্য নোট (ঐচ্ছিক)
ফলাফল দেখুন
```

### ৮.২ মাপ যাচাই — পাঞ্জাবি — ডার্ক  ·  `VerifyDark`

- **Image:** `screens/35_8-2_VerifyDark.png`  ·  **HTML:** `html/35_8-2_VerifyDark.html`  ·  **Frame:** 360x800
- **Purpose:** Dark-mode variant of 8.1.
- **Navigates to:** `CameraDark` (পেছনে যান); `ResultDark` (ফলাফল দেখুন)
- **Visible text:**

```
মাপ যাচাই করুন
পাঞ্জাবি · রাহাত
কোনো মাপ ভুল মনে হলে সেটিতে ট্যাপ করে ঠিক করুন
ঝুল (Length)
● নির্ভুলতা উচ্চ
৪২"
বুক (Chest)
● নির্ভুলতা উচ্চ
৪০"
পেট (Waist)
◐ নির্ভুলতা মাঝারি
৩৬"
হিপ (Hip)
◐ নির্ভুলতা মাঝারি
৪১"
পুট (Shoulder)
● নির্ভুলতা উচ্চ
১৮"
হাতা (Sleeve)
◐ নির্ভুলতা মাঝারি
২৪½"
হাতার মুখ (Cuff)
◐ নির্ভুলতা মাঝারি
১০"
গলা (Neck)
▲ নিম্ন · ফিতা দিয়ে মেপে নিন
১৫½"
দর্জির জন্য নোট (ঐচ্ছিক)
ফলাফল দেখুন
```

### ৮.৩ মাপ এডিট শিট  ·  `EditSheet`

- **Image:** `screens/36_8-3_EditSheet.png`  ·  **HTML:** `html/36_8-3_EditSheet.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `Verify` (dimmed scrim)
- **Purpose:** Edit bottom sheet over 8.1: small diagram, how-to, −/+ number field, AI original value shown, Save.
- **Navigates to:** `Camera` (পেছনে যান); `Result` (ফলাফল দেখুন); `Verify` (সংরক্ষণ করুন)
- **Visible text:**

```
মাপ যাচাই করুন
পাঞ্জাবি · রাহাত
কোনো মাপ ভুল মনে হলে সেটিতে ট্যাপ করে ঠিক করুন
ঝুল (Length)
● নির্ভুলতা উচ্চ
৪২"
বুক (Chest)
● নির্ভুলতা উচ্চ
৪০"
পেট (Waist)
◐ নির্ভুলতা মাঝারি
৩৬"
হিপ (Hip)
◐ নির্ভুলতা মাঝারি
৪১"
পুট (Shoulder)
● নির্ভুলতা উচ্চ
১৮"
হাতা (Sleeve)
◐ নির্ভুলতা মাঝারি
২৪½"
হাতার মুখ (Cuff)
◐ নির্ভুলতা মাঝারি
১০"
গলা (Neck)
▲ নিম্ন · ফিতা দিয়ে মেপে নিন
১৫½"
দর্জির জন্য নোট (ঐচ্ছিক)
ফলাফল দেখুন
পেট (Waist)
নাভির একটু ওপরে, পেটের চারপাশে ফিতা ঘুরিয়ে মাপুন।
ইঞ্চি
AI: ৩৬"
·
আপনি বদলেছেন
সংরক্ষণ করুন
```

### ৮.৪ মাপ যাচাই — সালওয়ার কামিজ  ·  `VerifySK`

- **Image:** `screens/37_8-4_VerifySK.png`  ·  **HTML:** `html/37_8-4_VerifySK.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Review for salwar kameez: Kameez (10) + Salwar (4) sections; list scrolls, "See result" button fixed at bottom.
- **Navigates to:** `CameraFemale` (পেছনে যান); `ResultSK` (ফলাফল দেখুন)
- **Visible text:**

```
মাপ যাচাই করুন
সালওয়ার কামিজ · ফারহানা · ১৪টি মাপ
ভুল মনে হলে মাপে ট্যাপ করে ঠিক করুন
"আপনার পছন্দ" লেখা মাপ AI বের করে না, পছন্দমতো বসিয়ে নিন
কামিজ
১০টি মাপ
বুক (Bust)
● নির্ভুলতা উচ্চ
৩৬"
কোমর (Waist)
◐ নির্ভুলতা মাঝারি
৩০"
হিপ (Hip)
◐ নির্ভুলতা মাঝারি
৩৮"
পুট (Shoulder)
● নির্ভুলতা উচ্চ
১৪"
আর্মহোল (Armhole)
▲ নিম্ন · ফিতা দিয়ে মেপে নিন
১৬"
হাতা (Sleeve)
● নির্ভুলতা উচ্চ
১৮"
হাতার মুখ (Cuff)
✎ আপনার পছন্দ
৯"
গলার গভীরতা — সামনে
✎ আপনার পছন্দ
৭"
গলার গভীরতা — পেছনে
✎ আপনার পছন্দ
৫"
ঝুল (Length)
✎ আপনার পছন্দ
৪০"
সালওয়ার
৪টি মাপ
কোমর (Waist)
◐ নির্ভুলতা মাঝারি
৩০"
হিপ (Hip)
◐ নির্ভুলতা মাঝারি
৩৮"
মোট লম্বা (Length)
● নির্ভুলতা উচ্চ
৩৮"
মোহরি (Bottom)
✎ আপনার পছন্দ
১২"
দর্জির জন্য নোট (ঐচ্ছিক)
ফলাফল দেখুন
```

### ৮.৫ মাপ যাচাই — ফিতায় মাপা  ·  `VerifyManual`

- **Image:** `screens/38_8-5_VerifyManual.png`  ·  **HTML:** `html/38_8-5_VerifyManual.html`  ·  **Frame:** 360x800
- **Purpose:** Review after tape flow: grey "Tape measured" badges, preference badges, missing hip shows "—" and orange badge.
- **Navigates to:** `ManualSummary` (পেছনে যান); `Result` (ফলাফল দেখুন)
- **Visible text:**

```
মাপ যাচাই করুন
পাঞ্জাবি · রাহাত · ফিতায় মাপা
কোনো মাপ ভুল মনে হলে সেটিতে ট্যাপ করে ঠিক করুন
ঝুল (Length)
✎ আপনার পছন্দ
৪২"
বুক (Chest)
ফিতায় মাপা
৪০"
পেট (Waist)
ফিতায় মাপা
৩৬"
হিপ (Hip)
বাদ আছে · দর্জি মেপে নেবেন
—
পুট (Shoulder)
ফিতায় মাপা
১৮"
হাতা (Sleeve)
ফিতায় মাপা
২৪½"
হাতার মুখ (Cuff)
✎ আপনার পছন্দ
১০"
গলা (Neck)
ফিতায় মাপা
১৫½"
দর্জির জন্য নোট (ঐচ্ছিক)
ফলাফল দেখুন
```

## ৯. ফলাফল — Results

### ৯.১ ফলাফল — পাঞ্জাবি  ·  `Result`

- **Image:** `screens/39_9-1_Result.png`  ·  **HTML:** `html/39_9-1_Result.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Result (panjabi): saved chip, numbered diagram + suggested size L, numbered list, inch/cm toggle, Share PDF (primary), Share image.
- **Navigates to:** `Verify` (পেছনে যান); `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
পাঞ্জাবির মাপ
রাহাত · ১৬ অক্টোবর ২০২৬
সেভ হয়েছে · ফোনেই সংরক্ষিত
১
২
৩
৪
৫
৬
৭
৮
সাজেস্ট করা সাইজ
L
রেডিমেড পাঞ্জাবি কিনতে
রেগুলার ফিট অনুযায়ী
মাপের তালিকা
ইঞ্চি
সেমি
১
ঝুল (Length)
৪২"
২
বুক (Chest)
৪০"
৩
পেট (Waist)
৩৬"
৪
হিপ (Hip)
৪১"
৫
পুট (Shoulder)
১৮"
৬
হাতা (Sleeve)
২৪½"
৭
হাতার মুখ (Cuff)
১০"
৮
গলা (Neck)
১৫½"
PDF শেয়ার
ছবি হিসেবে শেয়ার
হোম
সাইজ চার্ট
সেটিংস
```

### ৯.২ ফলাফল — পাঞ্জাবি — ডার্ক  ·  `ResultDark`

- **Image:** `screens/40_9-2_ResultDark.png`  ·  **HTML:** `html/40_9-2_ResultDark.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Dark-mode variant of 9.1.
- **Navigates to:** `VerifyDark` (পেছনে যান); `MainDark` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
পাঞ্জাবির মাপ
রাহাত · ১৬ অক্টোবর ২০২৬
সেভ হয়েছে · ফোনেই সংরক্ষিত
১
২
৩
৪
৫
৬
৭
৮
সাজেস্ট করা সাইজ
L
রেডিমেড পাঞ্জাবি কিনতে
রেগুলার ফিট অনুযায়ী
মাপের তালিকা
ইঞ্চি
সেমি
১
ঝুল (Length)
৪২"
২
বুক (Chest)
৪০"
৩
পেট (Waist)
৩৬"
৪
হিপ (Hip)
৪১"
৫
পুট (Shoulder)
১৮"
৬
হাতা (Sleeve)
২৪½"
৭
হাতার মুখ (Cuff)
১০"
৮
গলা (Neck)
১৫½"
PDF শেয়ার
ছবি হিসেবে শেয়ার
হোম
সাইজ চার্ট
সেটিংস
```

### ৯.৩ ফলাফল — সালওয়ার কামিজ  ·  `ResultSK`

- **Image:** `screens/41_9-3_ResultSK.png`  ·  **HTML:** `html/41_9-3_ResultSK.html`  ·  **Frame:** 360x800  ·  interactive states
- **Purpose:** Result for salwar kameez with Kameez/Salwar tabs switching diagram and list.
- **Navigates to:** `VerifySK` (পেছনে যান); `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
সালওয়ার কামিজের মাপ
ফারহানা · ১৬ অক্টোবর ·
সেভ হয়েছে
কামিজ · ১০
সালওয়ার · ৪
১
২
৩
৪
৫
৬
৭
৮
৯
১০
সাজেস্ট করা সাইজ
M
রেডিমেড কামিজ কিনতে
রেগুলার ফিট অনুযায়ী
১
বুক (Bust)
৩৬"
২
কোমর (Waist)
৩০"
৩
হিপ (Hip)
৩৮"
৪
পুট (Shoulder)
১৪"
৫
আর্মহোল (Armhole)
১৬"
৬
হাতা (Sleeve)
১৮"
৭
হাতার মুখ (Cuff)
৯"
৮
গলার গভীরতা — সামনে
৭"
৯
গলার গভীরতা — পেছনে
৫"
১০
ঝুল (Length)
৪০"
PDF শেয়ার
ছবি হিসেবে শেয়ার
হোম
সাইজ চার্ট
সেটিংস
```

### ৯.৪ গেঞ্জির সাইজ  ·  `GenjiResult`

- **Image:** `screens/42_9-4_GenjiResult.png`  ·  **HTML:** `html/42_9-4_GenjiResult.html`  ·  **Frame:** 360x800
- **Purpose:** T-shirt: size-only result — big L, basis text, M|L|XL comparison, brand note, Share + Size chart.
- **Navigates to:** `Main` (পেছনে যান); `SizeChart` (সাইজ চার্ট দেখুন); `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
গেঞ্জির সাইজ
রাহাত · ১৬ অক্টোবর ·
সেভ হয়েছে
আপনার সাইজ
L
বুক ৪০", পুট ১৮", ঝুল ২৮" অনুযায়ী
রেগুলার ফিট
পাশের সাইজের সাথে তুলনা
M
বুক ৩৮–৪০
L
বুক ৪০–৪২
XL
বুক ৪২–৪৪
টাইট পছন্দ হলে M, ঢিলা পছন্দ হলে XL দেখতে পারেন
ব্র্যান্ডভেদে সাইজ আলাদা হতে পারে
শেয়ার
সাইজ চার্ট দেখুন
হোম
সাইজ চার্ট
সেটিংস
```

## ১০. শেয়ার করার ফাইল — Share files

### ১০.১ PDF — পাঞ্জাবি, A4  ·  `TailorPDF`

- **Image:** `screens/43_10-1_TailorPDF.png`  ·  **HTML:** `html/43_10-1_TailorPDF.html`  ·  **Frame:** 794x1123
- **Purpose:** A4 PDF sent to tailor (panjabi). White, print-friendly.
- **Visible text:**

```
মাপ
মাপের কার্ড
নাম
রাহাত
পোশাক
পাঞ্জাবি
তারিখ
১৬ অক্টোবর ২০২৬
ইউনিট
ইঞ্চি
ফিট পছন্দ
রেগুলার
উচ্চতা
৫ ফুট ৭ ইঞ্চি
১
২
৩
৪
৫
৬
৭
৮
নং
মাপ
মান (ইঞ্চি)
১
ঝুল
Length
৪২"
২
বুক
Chest
৪০"
৩
পেট
Waist
৩৬"
৪
হিপ
Hip
৪১"
৫
পুট
Shoulder
১৮"
৬
হাতা
Sleeve
২৪½"
৭
হাতার মুখ
Cuff
১০"
৮
গলা
Neck
* ফিতায় মিলিয়ে নিন
১৫½"
দর্জির জন্য নোট
হাতা একটু ঢিলা রাখবেন। বন্ধ গলা, বোতাম কাপড়ের রঙে।
সাজেস্ট করা সাইজ
L
রেডিমেড কেনার জন্য · রেগুলার ফিট
অ্যাপ দিয়ে নেওয়া আনুমানিক মাপ। সেলাইয়ের আগে প্রয়োজনে ফিতা দিয়ে মিলিয়ে নিন।
পৃষ্ঠা ১/১
```

### ১০.২ PDF — সালওয়ার কামিজ, A4  ·  `TailorPDFSK`

- **Image:** `screens/44_10-2_TailorPDFSK.png`  ·  **HTML:** `html/44_10-2_TailorPDFSK.html`  ·  **Frame:** 794x1123
- **Purpose:** A4 PDF for salwar kameez with two diagrams and two-part table; * marks preference values.
- **Visible text:**

```
মাপ
মাপের কার্ড
নাম
ফারহানা
পোশাক
সালওয়ার কামিজ
তারিখ
১৬ অক্টোবর ২০২৬
ইউনিট
ইঞ্চি
ফিট পছন্দ
রেগুলার
উচ্চতা
৫ ফুট ৩ ইঞ্চি
কামিজ
১
২
৩
৪
৫
৬
৭
৮
৯
১০
সালওয়ার
১
২
৩
৪
নং
মাপ
মান (ইঞ্চি)
কামিজ
১
বুক
Bust
৩৬"
২
কোমর
Waist
৩০"
৩
হিপ
Hip
৩৮"
৪
পুট
Shoulder
১৪"
৫
আর্মহোল
Armhole
১৬"
৬
হাতা
Sleeve
১৮"
৭
হাতার মুখ *
Cuff
৯"
৮
গলার গভীরতা, সামনে *
Front neck
৭"
৯
গলার গভীরতা, পেছনে *
Back neck
৫"
১০
ঝুল *
Length
৪০"
সালওয়ার
১
কোমর
Waist
৩০"
২
```

### ১০.৩ ছবি হিসেবে শেয়ার, PNG  ·  `SharePNG`

- **Image:** `screens/45_10-3_SharePNG.png`  ·  **HTML:** `html/45_10-3_SharePNG.html`  ·  **Frame:** 1080x2600
- **Purpose:** Tall 1080-px PNG for sharing on messaging apps; large type for phone reading.
- **Visible text:**

```
মাপ
রাহাত
পাঞ্জাবি · ১৬ অক্টোবর ২০২৬
সাইজ L
রেগুলার ফিট
ইঞ্চি
১
২
৩
৪
৫
৬
৭
৮
১
ঝুল
Length
৪২"
২
বুক
Chest
৪০"
৩
পেট
Waist
৩৬"
৪
হিপ
Hip
৪১"
৫
পুট
Shoulder
১৮"
৬
হাতা
Sleeve
২৪½"
৭
হাতার মুখ
Cuff
১০"
৮
গলা
Neck
১৫½"
দর্জির জন্য নোট
হাতা একটু ঢিলা রাখবেন। বন্ধ গলা, বোতাম কাপড়ের রঙে।
অ্যাপ দিয়ে নেওয়া আনুমানিক মাপ · গলার মাপ ফিতায় মিলিয়ে নিন
```

## ১১. সাইজ চার্ট — Size chart

### ১১.১ সাইজ চার্ট  ·  `SizeChart`

- **Image:** `screens/46_11-1_SizeChart.png`  ·  **HTML:** `html/46_11-1_SizeChart.html`  ·  **Frame:** 360x800
- **Purpose:** Size chart tab: gender toggle, garment chips, XS–XXXL table, user row highlighted, brand disclaimer.
- **Navigates to:** `Main` (হোম); `Settings` (সেটিংস)
- **Visible text:**

```
সাইজ চার্ট
ছেলে
মেয়ে
শার্ট
পাঞ্জাবি
গেঞ্জি
প্যান্ট
সাইজ
বুক
কোমর
হিপ
XS
৩৪–৩৬
২৮–৩০
৩৪–৩৬
S
৩৬–৩৮
৩০–৩২
৩৬–৩৮
M
৩৮–৪০
৩২–৩৪
৩৮–৪০
L
আপনার
৪০–৪২
৩৪–৩৬
৪০–৪২
XL
৪২–৪৪
৩৬–৩৮
৪২–৪৪
XXL
৪৪–৪৬
৩৮–৪০
৪৪–৪৬
XXXL
৪৬–৪৮
৪০–৪২
৪৬–৪৮
সব মাপ ইঞ্চিতে · রাহাতের বুক ৪০" অনুযায়ী
আনুমানিক মাপ, ব্র্যান্ডভেদে আলাদা হতে পারে
হোম
সাইজ চার্ট
সেটিংস
```

## ১২. সেটিংস — Settings

### ১২.১ সেটিংস  ·  `Settings`

- **Image:** `screens/47_12-1_Settings.png`  ·  **HTML:** `html/47_12-1_Settings.html`  ·  **Frame:** 360x800
- **Purpose:** Settings tab: language, unit, backup, restore, privacy, about, delete all (red).
- **Navigates to:** `Main` (হোম); `SizeChart` (সাইজ চার্ট)
- **Visible text:**

```
সেটিংস
সাধারণ
ভাষা
বাংলা
মাপের একক
ইঞ্চি
ডেটা
ব্যাকআপ নিন
শেষ ব্যাকআপ: ১০ অক্টোবর ২০২৬
রিস্টোর করুন
ব্যাকআপ ফাইল থেকে মাপ ফিরিয়ে আনুন
অন্যান্য
গোপনীয়তা নীতি
অ্যাপ সম্পর্কে
সংস্করণ ১.০
সব ডেটা মুছুন
সব প্রোফাইল ও মাপ চিরতরে মুছে যাবে
হোম
সাইজ চার্ট
সেটিংস
```

### ১২.২ ব্যাকআপ সফল  ·  `BackupSnackbar`

- **Image:** `screens/48_12-2_BackupSnackbar.png`  ·  **HTML:** `html/48_12-2_BackupSnackbar.html`  ·  **Frame:** 360x800
- **Overlay:** sheet/dialog shown on top of `Settings` (dimmed scrim)
- **Purpose:** Green snackbar over Settings after backup.
- **Navigates to:** `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস)
- **Visible text:**

```
সেটিংস
সাধারণ
ভাষা
বাংলা
মাপের একক
ইঞ্চি
ডেটা
ব্যাকআপ নিন
শেষ ব্যাকআপ: ১০ অক্টোবর ২০২৬
রিস্টোর করুন
ব্যাকআপ ফাইল থেকে মাপ ফিরিয়ে আনুন
অন্যান্য
গোপনীয়তা নীতি
অ্যাপ সম্পর্কে
সংস্করণ ১.০
সব ডেটা মুছুন
সব প্রোফাইল ও মাপ চিরতরে মুছে যাবে
হোম
সাইজ চার্ট
সেটিংস
ব্যাকআপ সেভ হয়েছে · Downloads ফোল্ডারে
```

### ১২.৩ রিস্টোর নিশ্চিতকরণ  ·  `RestoreDialog`

- **Image:** `screens/49_12-3_RestoreDialog.png`  ·  **HTML:** `html/49_12-3_RestoreDialog.html`  ·  **Frame:** 360x800  ·  interactive states
- **Overlay:** sheet/dialog shown on top of `Settings` (dimmed scrim)
- **Purpose:** Restore dialog: file contents, merge vs replace radio (replace shows warning), Cancel / Restore.
- **Navigates to:** `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস); `Settings` (বাতিল)
- **Visible text:**

```
সেটিংস
সাধারণ
ভাষা
বাংলা
মাপের একক
ইঞ্চি
ডেটা
ব্যাকআপ নিন
শেষ ব্যাকআপ: ১০ অক্টোবর ২০২৬
রিস্টোর করুন
ব্যাকআপ ফাইল থেকে মাপ ফিরিয়ে আনুন
অন্যান্য
গোপনীয়তা নীতি
অ্যাপ সম্পর্কে
সংস্করণ ১.০
সব ডেটা মুছুন
সব প্রোফাইল ও মাপ চিরতরে মুছে যাবে
হোম
সাইজ চার্ট
সেটিংস
ব্যাকআপ রিস্টোর করবেন?
এই ফাইলে ৩টি প্রোফাইল ও ৫টি মাপ আছে
map-backup-2026-10-10.json
আগের ডেটার সাথে যোগ করুন
ফোনের মাপগুলোও থেকে যাবে
আগের ডেটা বদলে দিন
শুধু ফাইলের ডেটা থাকবে
বাতিল
রিস্টোর করুন
```

### ১২.৪ সব ডেটা মুছুন — নিশ্চিতকরণ  ·  `DeleteDialog`

- **Image:** `screens/50_12-4_DeleteDialog.png`  ·  **HTML:** `html/50_12-4_DeleteDialog.html`  ·  **Frame:** 360x800  ·  interactive states
- **Overlay:** sheet/dialog shown on top of `Settings` (dimmed scrim)
- **Purpose:** Delete-all confirmation: red warning, counts, type "মুছুন" to enable red Delete-all button.
- **Navigates to:** `Main` (হোম); `SizeChart` (সাইজ চার্ট); `Settings` (সেটিংস); `Settings` (বাতিল)
- **Visible text:**

```
সেটিংস
সাধারণ
ভাষা
বাংলা
মাপের একক
ইঞ্চি
ডেটা
ব্যাকআপ নিন
শেষ ব্যাকআপ: ১০ অক্টোবর ২০২৬
রিস্টোর করুন
ব্যাকআপ ফাইল থেকে মাপ ফিরিয়ে আনুন
অন্যান্য
গোপনীয়তা নীতি
অ্যাপ সম্পর্কে
সংস্করণ ১.০
সব ডেটা মুছুন
সব প্রোফাইল ও মাপ চিরতরে মুছে যাবে
হোম
সাইজ চার্ট
সেটিংস
সব ডেটা মুছবেন?
৩টি প্রোফাইল ও ৫টি মাপ চিরতরে মুছে যাবে। এটি আর ফেরানো যাবে না।
নিশ্চিত করতে নিচে "মুছুন" লিখুন
বাতিল
সব মুছুন
```

### ১২.৫ গোপনীয়তা নীতি  ·  `PrivacyPolicy`

- **Image:** `screens/51_12-5_PrivacyPolicy.png`  ·  **HTML:** `html/51_12-5_PrivacyPolicy.html`  ·  **Frame:** 360x800
- **Purpose:** Privacy policy page: green summary + 5 numbered sections.
- **Navigates to:** `Settings` (পেছনে যান)
- **Visible text:**

```
গোপনীয়তা নীতি
হালনাগাদ: ১৬ অক্টোবর ২০২৬
সংক্ষেপে: আপনার সব তথ্য শুধু আপনার ফোনেই থাকে।
১
কী তথ্য রাখা হয়
প্রোফাইলের নাম, লিঙ্গ, উচ্চতা, ফিট পছন্দ, মাপ আর দর্জির নোট।
২
কোথায় থাকে
শুধু আপনার ফোনে। কোনো সার্ভারে বা অন্য কারও কাছে যায় না।
৩
ছবির কী হয়
মাপ বের হলেই ছবি মুছে যায়। কোথাও সেভ বা পাঠানো হয় না।
৪
ইন্টারনেট লাগে না
অ্যাপ ইন্টারনেট ব্যবহার করে না। শেয়ার করলে শুধু আপনার বেছে নেওয়া অ্যাপে যায়।
৫
কীভাবে মুছবেন
সেটিংস › "সব ডেটা মুছুন" চাপুন। অ্যাপ আনইনস্টল করলেও সব মুছে যায়।
```

### ১২.৬ অ্যাপ সম্পর্কে  ·  `About`

- **Image:** `screens/52_12-6_About.png`  ·  **HTML:** `html/52_12-6_About.html`  ·  **Frame:** 360x800
- **Purpose:** About page: logo, name, version, tagline, offline badge, contact email, privacy link.
- **Navigates to:** `Settings` (পেছনে যান); `mailto:[যোগাযোগের-ইমেইল]` (যোগাযোগ [যোগাযোগের ইমেইল]); `PrivacyPolicy` (গোপনীয়তা নীতি)
- **Visible text:**

```
অ্যাপ সম্পর্কে
মাপ
সংস্করণ ১.০
ছবি বা ফিতা দিয়ে পোশাকের মাপ নিন, দর্জিকে পাঠান।
অফলাইন · কোনো ডেটা সংগ্রহ নেই
যোগাযোগ
[যোগাযোগের ইমেইল]
গোপনীয়তা নীতি
বাংলাদেশের মানুষের জন্য তৈরি
```
