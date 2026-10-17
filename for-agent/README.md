# TailorPass — Design Handoff Package (Kotlin Multiplatform + Compose Multiplatform)

Platform: **KMP + Compose Multiplatform** — the whole UI is written once in Compose (`shared/commonMain`), and the same UI runs on Android and iOS. There is no separate UI in SwiftUI. (The design HTML/PNGs still show the old name "মাপ" / "Maap"; the app is now called **TailorPass**.)

| File/folder | Contents |
|---|---|
| [`plan.md`](plan.md) | Instructions and step-by-step plan (Steps 0–11) for the AI coding agent — **give this first** |
| [`KMP + CMP Technical guideline.en.md`](KMP%20+%20CMP%20Technical%20guideline.en.md) | Technical guideline for Steps 10–11 (after the design phase is finished) |
| [`PROJECT_OVERVIEW.md`](PROJECT_OVERVIEW.md) | Product scope: users, v1 features, garments, privacy, roadmap, open decisions — imported into `CLAUDE.md`, so the agent loads it every session |
| `maap-design-screens.pdf` | All 52 screens in section order (A4 landscape, 21 pages, index on the first page) |
| `SCREENS.md` | Each screen's purpose, which screens it goes to, and all text on the screen |
| `screens.json`, `navigation.json` | The same information in machine-readable form + the navigation graph (built from the HTML links; the missing links are in `plan.md` §5) |
| `screens/` | PNG (2×) of every screen, named `order_number_id.png` |
| `html/` | Exact HTML/CSS of every screen (opens in a browser) |
| `design-tokens.json` | Colors, type, sizes and spacing |
| `kmp-resources/` | Ready-made resources — models, diagrams, Bengali digits, drawables, strings, Compose theme. They go into `shared/commonMain` in Step 0; how is described in the table in `plan.md` §2. `kmp-resources/README.md` was written for the old (native-UI) plan — use it only as a file list. |

File names are in order: `01_1-1_Language` … `52_12-6_About`.

The prompt for giving the agent its work, and the split into steps, are in `plan.md`.
