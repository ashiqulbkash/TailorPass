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

CMP প্রজেক্টে ফাইলগুলো যায় `shared/src/commonMain/composeResources/font/`-এ। এখানকার `androidApp/.../ui/theme/Type.kt` এখনো `R.font.hind_siliguri_regular` ব্যবহার করে; Step 0-এ সেটা `Font(Res.font.hind_siliguri_regular, …)`-এ বদলানো হয় (`plan.md` §2)। ফন্ট এই প্যাকেজে নেই — Step 0-এ এজেন্ট Google Fonts-এর অফিসিয়াল সোর্স (`github.com/google/fonts`, `ofl/hindsiliguri/`) থেকে নামায়।
