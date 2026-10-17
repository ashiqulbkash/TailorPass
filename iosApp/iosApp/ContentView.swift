import SwiftUI
import Shared

// Temporary root screen; replaced by native SwiftUI screens (see for-agent/plan.md, Step 11).
struct ContentView: View {
    var body: some View {
        Text(Greeting().greet())
    }
}
