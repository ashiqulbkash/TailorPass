import Foundation

/// shared মডিউলের string key → Localizable.xcstrings-এর লেখা।
/// key আর Android-এর strings.xml-এর নাম হুবহু এক (যেমন "m_chest", "accuracy_high")।
///
///   Text(L10n.t("m_chest"))
///   Text(L10n.t("home_saved_count", count))        // %1$@ বসবে (সংখ্যা আগে toBanglaDigits করে String দিন)
///   Text(L10n.t(MeasurementKey.chest.labelKey))     // shared enum থেকে
///
/// অ্যাপের ভাষা বদলাতে (সেটিংস → ভাষা) `L10n.locale` সেট করুন; nil হলে সিস্টেমের ভাষা।
enum L10n {
    static var locale: Locale? = nil

    static func t(_ key: String, _ args: CVarArg...) -> String {
        let format: String
        if let locale, let path = Bundle.main.path(forResource: locale.language.languageCode?.identifier, ofType: "lproj"),
           let bundle = Bundle(path: path) {
            format = bundle.localizedString(forKey: key, value: nil, table: nil)
        } else {
            format = NSLocalizedString(key, comment: "")
        }
        return args.isEmpty ? format : String(format: format, locale: locale ?? .current, arguments: args)
    }
}
