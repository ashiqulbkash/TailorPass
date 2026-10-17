import SwiftUI
import Shared   // KMP shared মডিউলের framework (নাম আপনার baseName অনুযায়ী)

/// shared/Diagrams-এর ডেটা দিয়ে নম্বরসহ ডায়াগ্রাম (SwiftUI)।
/// outline/marks দুটোই Assets-এ template ইমেজ, তাই রং এখানে বসে।
///
///   NumberedDiagram(spec: Diagrams.shared.panjabi).frame(width: 134, height: 168)
struct NumberedDiagram: View {
    let spec: DiagramSpec
    var badgeSize: CGFloat = 20

    var body: some View {
        let vw = CGFloat(spec.viewportWidth), vh = CGFloat(spec.viewportHeight)
        let top = CGFloat(spec.cropTop), visibleH = CGFloat(spec.cropBottom - spec.cropTop)

        GeometryReader { geo in
            let s = geo.size.width / vw                 // প্রস্থ অনুযায়ী স্কেল
            ZStack(alignment: .topLeading) {
                Image(spec.outlineName).resizable().renderingMode(.template)
                    .foregroundStyle(Color.Maap.diagramOutline)
                    .frame(width: vw * s, height: vh * s)
                Image(spec.marksName).resizable().renderingMode(.template)
                    .foregroundStyle(Color.Maap.primary)
                    .frame(width: vw * s, height: vh * s)
                ForEach(spec.badges, id: \.number) { b in
                    Text(BanglaDigitsKt.toBanglaDigits(String(b.number)))
                        .font(.custom(MaapFont.bold, size: 11))
                        .foregroundStyle(Color.Maap.onPrimary)
                        .frame(width: badgeSize, height: badgeSize)
                        .background(Circle().fill(Color.Maap.primary))
                        .position(x: CGFloat(b.x) * s, y: CGFloat(b.y) * s)
                }
            }
            .offset(y: -top * s)                        // crop: উপরের অংশ সরিয়ে দিন
            .frame(width: geo.size.width, height: visibleH * s, alignment: .topLeading)
            .clipped()
        }
        .aspectRatio(vw / visibleH, contentMode: .fit)
        .accessibilityHidden(true)                      // নম্বরগুলো পাশের তালিকায় পড়া হয়
    }
}
