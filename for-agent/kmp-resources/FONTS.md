# Hind Siliguri ফন্ট — এই ফোল্ডারে রাখুন

ফন্ট ফাইলগুলো এই প্যাকেজে দেওয়া যায়নি, Google Fonts থেকে নিজে নামাতে হবে (বিনামূল্যে, SIL Open Font License)।

১. https://fonts.google.com/specimen/Hind+Siliguri খুলে "Get font" → "Download all" চাপুন।
২. ZIP খুলে নিচের ৪টি ফাইল এই ফোল্ডারে রাখুন। নামগুলো **ঠিক এভাবে** ছোট হাতে বদলে দিন, কারণ Compose Resources-এ নাম a-z, 0-9 আর _ হতে হবে:

| ডাউনলোড করা ফাইল | এই নামে রাখুন |
|---|---|
| HindSiliguri-Regular.ttf | hind_siliguri_regular.ttf |
| HindSiliguri-Medium.ttf | hind_siliguri_medium.ttf |
| HindSiliguri-SemiBold.ttf | hind_siliguri_semibold.ttf |
| HindSiliguri-Bold.ttf | hind_siliguri_bold.ttf |

৩. লাইসেন্স ফাইল (OFL.txt) অ্যাপের "লাইসেন্স" পাতায় বা রিপোজিটরিতে রেখে দিন।

`kotlin/ui/theme/Type.kt` এই ৪টি নামই ব্যবহার করে (`Res.font.hind_siliguri_regular` ইত্যাদি)।
