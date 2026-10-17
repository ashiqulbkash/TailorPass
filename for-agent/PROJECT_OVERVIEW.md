# TailorPass — Project Overview

Oct 17, 2026 · @Md Ashiqul Islam

TailorPass is an offline mobile app that takes clothing measurements from two photos or a measuring tape, and turns them into a clear measurement card for the tailor. Version 1 ships on Android first.

## 1. What TailorPass is

**The problem.** Getting clothes tailored usually means a visit to the tailor just to be measured. Measurements are written in a tailor's notebook, get lost, and must be taken again for every new garment or family member. People who live abroad, or order clothes for relatives, have no easy way to send accurate measurements.

**The users.**

- **Primary:** everyday people in Bangladesh who have clothes made by a tailor (panjabi, shirt, pants, salwar kameez), often for a whole family.
- **Secondary:** the international South Asian diaspora who send measurements home, and anyone buying ready-made clothes who wants to know their size.
- **Later:** tailors themselves, who can keep customer measurements in the app (version 3).

**The promise.** Measure at home in a few minutes, keep everyone's measurements in one place, and send the tailor a card they understand — without the photos or measurements ever leaving the phone.

## 2. How it works

&#91;embedded content: How TailorPass works · photo and tape paths\]

Photos that are too dark, blurry or cut off are sent back for a retake. Both paths end at the same review screen, and every result is saved on the phone so it can be shared again later.

## 3. Features in version 1

Version 1 covers the whole job — profile, measure, check, share, keep — in 11 feature areas across 52 designed screens.

| Area | What the user can do |
| --- | --- |
| **Profiles** | Create several profiles (self, family) with name, gender, height and fit preference (slim / regular / loose). Switch profiles from the home screen. |
| **Photo measurement (AI)** | Take a front and a side photo with a body-outline overlay, 10-second timer and voice guidance. On-device AI finds the body and calculates each measurement, using the person's height for scale. |
| **Preparation guide** | Checklist before photos (tight clothes, good light, plain wall, phone at waist height, stand straight) plus garment-specific tips and a pose diagram. |
| **Photo check** | The app rejects dark, blurry or cut-off photos and says why, before calculating. If the AI cannot measure, it offers to retake or switch to tape. |
| **Tape measurement** | Guided step-by-step entry from top of the body to bottom, with a picture and instructions for each measurement, "skip for now", unusual-value warnings, and choice cards for preference measurements (length, cuff, neck depth). Unfinished sessions can be resumed later. |
| **Review and edit** | Every measurement is listed with an accuracy badge (high / medium / low, "tape-measured" or "your choice") and can be corrected. A note for the tailor can be added. |
| **Result** | A body diagram with numbered measurements, a matching table in the tailor's own terms (e.g. পুট / Shoulder, মোহরি / Bottom opening), inch/cm toggle, and a suggested ready-made size. Saved automatically. |
| **Share with the tailor** | Share as an A4 PDF or a phone-friendly PNG image through WhatsApp, Messenger or any app. Measurements stay saved after sharing and can be re-shared any time. |
| **Saved measurements** | All measurements per profile on the home screen: open, re-share, edit, re-measure or delete. |
| **Size chart** | International sizes (XS–XXXL) for men's and women's garments, with the user's own size highlighted. Ready-made T-shirts (গেঞ্জি) get a size suggestion instead of a measurement list. |
| **Settings** | Language (Bangla / English), unit, backup to a file and restore, privacy policy, about, and delete all data. |

## 4. Garments and measurements

Version 1 covers 5 tailored garments plus a T-shirt size suggestion. Body measurements come from the AI or the tape; preference measurements (marked \*) are chosen by the user, with a suggested default.

| Garment | Measurements |
| --- | --- |
| Panjabi (men) | Length\*, chest, waist, hip, shoulder, sleeve, cuff\*, neck — 8 |
| Shirt (men) | Neck, chest, waist, hip, shoulder, sleeve, biceps, cuff\*, length\* |
| Pants (men) | Waist, hip, thigh, knee, crotch, inseam, full length, bottom opening\* |
| Pajama (men) | Waist, hip, thigh, full length, bottom opening\* |
| Salwar kameez (women) | Kameez: bust, waist, hip, shoulder, armhole, sleeve, cuff\*, front neck depth\*, back neck depth\*, length\* — 10. Salwar: waist, hip, full length, bottom opening\* — 4 |
| T-shirt / গেঞ্জি | Size suggestion (S–XXL) from chest, shoulder and length |

## 5. Privacy and offline promise

Nothing leaves the phone: the Android app does not even request internet permission.

- **Fully offline.** Every feature, including the AI, works in airplane mode.
- **Photos are temporary.** They are processed in memory and deleted as soon as the measurements are calculated. They are never saved to the gallery.
- **Measurements stay on the phone.** They leave it only when the user shares a PDF or image, or exports a backup file themselves.
- **No tracking.** No accounts, analytics, ads or crash reporting.
- **User in control.** One button deletes every profile and measurement.

The app says this plainly on the language screen, in onboarding, in the camera permission request and in the privacy policy. The Play Store and App Store listings will declare that no data is collected.

## 6. Roadmap

Version 2 widens the garment list and adds conveniences; version 3 opens the app to tailors.

**Version 2**

- More garments: suit and waistcoat (কটি) for men; blouse, kurti and burqa for women.
- Region-based size charts (US, UK, EU, Asian).
- Measurement history and comparison.
- Fabric calculator: how many yards to buy for each garment and fabric width.
- Measure from a well-fitting old garment laid flat.
- Design and style choices (collar, pocket, cuff, neckline) included on the PDF.
- App lock with PIN or fingerprint.
- iOS release.

**Version 3 and later**

- Tailor mode: a tailor keeps customer measurements, searchable by name or phone.
- Order tracker: order and delivery dates, advance and due payments, reminders.
- QR code sharing between users and tailors, offline.
- Re-measure reminders (more often for children), children's garments, an easy mode for older users, and a family package PDF.

## 7. Open decisions

- [ ] Confirm each garment's measurement list and fit allowances with a tailor.
- [ ] Collect tape measurements from 10–20 people to calibrate the AI (target: girths within ±2 cm).
- [ ] Record the Bangla and English voice-guide clips.
- [ ] Final check of the app name "TailorPass" on the Play Store, App Store and domain registrars.
- [ ] Decide which version 2 features become paid (premium) features, if any.
