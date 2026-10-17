import SwiftUI

/// Hind Siliguri টাইপ স্কেল — Android-এর Type.kt-এর সমান (sp → pt)।
/// ফন্ট ফাইলগুলো Xcode টার্গেটে যোগ করে Info.plist-এর UIAppFonts-এ নাম দিন (README দেখুন)।
/// `relativeTo:` থাকায় Dynamic Type-এ বড় লেখাও কাজ করবে।
enum MaapFont {
    static let regular = "HindSiliguri-Regular"
    static let medium = "HindSiliguri-Medium"
    static let semiBold = "HindSiliguri-SemiBold"
    static let bold = "HindSiliguri-Bold"

    private static func f(_ name: String, _ size: CGFloat, _ style: Font.TextStyle) -> Font {
        .custom(name, size: size, relativeTo: style)
    }

    // টাইপ স্কেল (Material 3 নাম রাখা হয়েছে যাতে দুই প্ল্যাটফর্মে মেলানো সহজ হয়)
    static let displayLarge   = f(bold, 88, .largeTitle)   // গেঞ্জির সাইজ "L"
    static let displayMedium  = f(bold, 54, .largeTitle)   // সাজেস্ট করা সাইজ
    static let headlineLarge  = f(bold, 26, .title)        // অনবোর্ডিং/ভাষা শিরোনাম
    static let headlineMedium = f(bold, 24, .title)        // মাপের নাম (ম্যানুয়াল এন্ট্রি)
    static let headlineSmall  = f(bold, 22, .title2)       // ডায়ালগ/শিট শিরোনাম
    static let titleLarge     = f(bold, 21, .title2)       // নেভিগেশন বার শিরোনাম
    static let titleMedium    = f(bold, 18, .headline)     // সেকশন হেডার
    static let titleSmall     = f(semiBold, 17, .headline) // তালিকার সারির নাম
    static let bodyLarge      = f(regular, 17, .body)
    static let bodyMedium     = f(regular, 16, .body)      // মূল লেখা (ন্যূনতম)
    static let bodySmall      = f(regular, 14, .subheadline) // তারিখ, সাব-লেখা
    static let labelLarge     = f(bold, 18, .headline)     // প্রধান বোতাম
    static let labelMedium    = f(semiBold, 14, .subheadline) // ট্যাব লেবেল
    static let labelSmall     = f(semiBold, 13, .caption)  // ব্যাজ

    // মাপের সংখ্যা
    static let numberInput   = f(bold, 44, .largeTitle)    // সংখ্যার ঘর (৪০)
    static let numberList    = f(bold, 24, .title)         // যাচাই তালিকা (৪০")
    static let numberCompact = f(bold, 18, .headline)      // ফলাফল টেবিল
}

/// Android-এর lineHeight মেলাতে (Bangla যুক্তাক্ষরের জন্য লাইনে জায়গা লাগে)।
extension View {
    /// উদাহরণ: Text(...).font(MaapFont.bodyMedium).maapLineHeight(24, fontSize: 16)
    func maapLineHeight(_ lineHeight: CGFloat, fontSize: CGFloat) -> some View {
        lineSpacing(max(0, lineHeight - fontSize * 1.2))
    }
}
