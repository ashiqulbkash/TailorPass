import SwiftUI

/// স্পেসিং ও মাপ — Android-এর Dimens.kt-এর সমান (dp → pt)।
enum MaapDimens {
    static let space4: CGFloat = 4
    static let space6: CGFloat = 6
    static let space8: CGFloat = 8
    static let space10: CGFloat = 10
    static let space12: CGFloat = 12
    static let space14: CGFloat = 14
    static let space16: CGFloat = 16
    static let space20: CGFloat = 20
    static let space24: CGFloat = 24

    static let screenPadding: CGFloat = 16      // দুই পাশের মার্জিন
    static let minTouchTarget: CGFloat = 48     // সব চাপা যায় এমন জিনিস ≥৪৮

    static let appBarHeight: CGFloat = 64
    static let bottomNavHeight: CGFloat = 80

    static let buttonHeight: CGFloat = 56       // প্রধান পুরো-প্রস্থ বোতাম
    static let buttonHeightCompact: CGFloat = 46
    static let dialogButtonHeight: CGFloat = 52
    static let segmentedHeight: CGFloat = 44    // ইঞ্চি/সেমি টগল

    static let garmentCardHeight: CGFloat = 80
    static let listRowMinHeight: CGFloat = 52
    static let savedRowMinHeight: CGFloat = 64
    static let iconTile: CGFloat = 48
    static let iconTileSmall: CGFloat = 40
    static let iconSize: CGFloat = 24
    static let garmentIconSize: CGFloat = 30

    static let textFieldHeight: CGFloat = 56
    static let numberFieldHeight: CGFloat = 72  // বড় সংখ্যার ঘর
    static let stepperButtonWidth: CGFloat = 56 // − / + বোতাম

    static let stepBarHeight: CGFloat = 6
    static let progressBarHeight: CGFloat = 12

    static let sheetHandleWidth: CGFloat = 36
    static let sheetHandleHeight: CGFloat = 4
    static let dialogSideMargin: CGFloat = 20
    static let scrimOpacity: Double = 0.5

    // কোণের গোলাই (Android Shapes-এর সমান)
    static let radiusExtraSmall: CGFloat = 8    // চেকবক্স
    static let radiusSmall: CGFloat = 10        // ব্যাজ
    static let radiusMedium: CGFloat = 14       // ইনপুট, ছোট কার্ড, ব্যানার
    static let radiusLarge: CGFloat = 16        // কার্ড
    static let radiusSheet: CGFloat = 28        // ডায়ালগ, বটম শিট
    static let radiusPdfCard: CGFloat = 10
}

/// প্রধান বোতাম: পুরো প্রস্থ, ৫৬ উঁচু, ভরাট। প্রতিটি স্ক্রিনে একটিই।
struct MaapPrimaryButtonStyle: ButtonStyle {
    @Environment(\.isEnabled) private var isEnabled
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(MaapFont.labelLarge)
            .frame(maxWidth: .infinity, minHeight: MaapDimens.buttonHeight)
            .foregroundStyle(isEnabled ? Color.Maap.onPrimary : Color.Maap.onDisabled)
            .background(isEnabled ? Color.Maap.primary : Color.Maap.disabledContainer)
            .clipShape(Capsule())
            .opacity(configuration.isPressed ? 0.85 : 1)
    }
}

/// দ্বিতীয় বোতাম: বর্ডার দেওয়া।
struct MaapOutlinedButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(MaapFont.labelLarge)
            .frame(maxWidth: .infinity, minHeight: MaapDimens.buttonHeight)
            .foregroundStyle(Color.Maap.primary)
            .overlay(Capsule().stroke(Color.Maap.outline, lineWidth: 1))
            .contentShape(Capsule())
            .opacity(configuration.isPressed ? 0.7 : 1)
    }
}
