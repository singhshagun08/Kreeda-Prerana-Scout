# Kreeda-Prerana Scout 🏆

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Auth_&_Firestore-FFCA28?logo=firebase)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Kreeda-Prerana Scout** is a high-performance sports talent scouting and athlete tracking application. Designed for coaches and scouts, it leverages AI-powered insights to discover future champions and monitor athletic progress with precision.

---

## 📸 Screenshots

| Splash & Branding | Login | Dashboard |
|:---:|:---:|:---:|
| <img src="metadata/screenshots/splash.png" width="200" /> | <img src="metadata/screenshots/login.png" width="200" /> | <img src="metadata/screenshots/home.png" width="200" /> |

| Athlete List | Performance Timer | Analytics |
|:---:|:---:|:---:|
| <img src="metadata/screenshots/list.png" width="200" /> | <img src="metadata/screenshots/timer.png" width="200" /> | <img src="metadata/screenshots/analytics.png" width="200" /> |

---

## ✨ Key Features

- 🔐 **Secure Authentication**: Firebase-powered Login & Signup with email verification.
- 🏃 **Athlete Management**: Full CRUD operations for athlete profiles, including photo uploads and physical attributes.
- ⏱️ **Precision Timer**: Millisecond-accurate sprint and trial timer for real-time performance logging.
- 📊 **Dynamic Analytics**: Visualized performance trends using MPAndroidChart.
- 🤖 **AI Insights**: Automated analysis to identify "District Ready" talent and consistency alerts.
- 🏆 **Achievement System**: Badge collection for reaching performance milestones.
- 📄 **Professional Reporting**: Shareable athlete report cards and data summaries.
- 🌑 **Futuristic UI**: Premium dark-tech theme with neon accents and smooth animations.

---

## 🛠 Tech Stack

- **UI**: Jetpack Compose, Material 3
- **Language**: Kotlin + Coroutines & Flow
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Database**: Room (Local) & Firebase Firestore (Cloud)
- **Networking/Auth**: Firebase SDK
- **Charts**: MPAndroidChart
- **Navigation**: Compose Navigation with Type-Safe Routes
- **Dependency Management**: Gradle Kotlin DSL

---

## 🚀 Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/kreedaperana.git
   ```

2. **Firebase Configuration**:
   - Create a project on [Firebase Console](https://console.firebase.google.com/).
   - Add an Android App with package name `com.example.kreedaperana`.
   - Download `google-services.json` and place it in the `app/` directory.
   - Enable Email/Password Auth and Firestore Database.

3. **Build the Project**:
   - Open in Android Studio (Ladybug or newer).
   - Sync Gradle and run the `:app` module.

---

## 📁 Project Structure

```text
com.example.kreedaperana
│
├── auth                # Authentication screens (Login, Signup, Forgot Password)
├── data                # Data layer
│   ├── local           # Room Database & DAOs
│   ├── remote          # Firestore Services
│   ├── model           # Data models (shared across layers)
│   └── repository      # Repository implementations
├── ui                  # Presentation layer
│   ├── screens         # Main feature screens
│   ├── components      # Reusable UI components
│   ├── navigation      # NavGraph & Type-safe routes
│   └── theme           # Color palette & Typography
├── viewmodel           # UI State management
└── utils               # Helper classes (Validation, Sharing, etc.)
```

---

## 🔮 Future Roadmap

- [ ] integration with Gemini API for advanced coaching recommendations.
- [ ] Offline-first sync with WorkManager.
- [ ] Video analysis for technique correction.
- [ ] PDF export for full team rosters.

---

## ⚖️ License

Distributed under the MIT License. See `LICENSE` for more information.

---
*Developed with ❤️ for the future of sports.*
