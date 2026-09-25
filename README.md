# Musica - Advanced Android Music Player & Discovery App

Musica is a modern, feature-rich Android music player and discovery application built with Kotlin and Jetpack Compose. It combines local data persistence, external music metadata APIs, real-time audio recognition, and AI-powered recommendations into a polished Material Design 3 experience.

---

## Key Features

### 1. Audio Playback & Queue Management
* **Advanced Player Engine**: Powered by Android MediaPlayer with multi-API preview URL resolution across Deezer and iTunes.
* **Queue Control**: Full session management supporting Play Now, Play Next, Add to Queue, Shuffle, and Looping.
* **Background Playback**: Seamless audio ticker and mini-player controls docked across application screens.

### 2. Searchable TopBar & External Music API Integration
* **Real-Time Metadata Search**: Searchable top bar integrating directly with external music APIs to find song metadata, artist details, and album artwork instantly.
* **Audio Recognition**: Integrated voice search, hum-to-search, and ambient music recognition services.

### 3. Local Persistence & Custom Playlists
* **Room Database**: Offline persistence for Liked Songs, listening history, cached tracks, and custom user playlists.
* **Playlist Management**: Create custom playlists, add or remove tracks, and organize your music library.

### 4. Synced Lyrics & Discovery
* **Real-Time Synced Lyrics**: Powered by LRCLIB integration for synchronized karaoke-style lyrics.
* **AI Recommendations**: Personalized music discovery and genre exploration backed by Gemini API integration.

---

## Tech Stack & Architecture

* **Language**: Kotlin
* **UI Framework**: Jetpack Compose & Material Design 3 (M3)
* **Architecture**: MVVM (Model-View-ViewModel) with Kotlin Coroutines and Flows
* **Local Database**: Room Database with KSP (Kotlin Symbol Processing)
* **Networking**: Retrofit & OkHttp with Deezer, iTunes, and LRCLIB APIs
* **Image Loading**: Coil Compose
* **Build System**: Gradle with Kotlin DSL

---

## Project Structure

```text
app/src/main/java/com/example/
├── data/
│   ├── local/          # Room database, DAOs, and entities
│   ├── remote/         # Retrofit API services (Deezer, iTunes, LRCLIB)
│   └── repository/     # Data repositories bridging local and remote sources
├── model/              # Domain models (Song, Album, Artist, Playlist)
├── player/             # AudioPlayerManager and playback engine
├── ui/
│   ├── components/     # Reusable Compose components (SearchableTopBar, MiniPlayer, etc.)
│   ├── screens/        # Screen composables (Home, Search, Favorites, Lyrics, Artist)
│   ├── theme/          # Material 3 theme, colors, and typography
│   └── viewmodel/      # MainViewModel managing application state
└── util/               # Voice search, humming recognition, ambient ID helpers
```

---

## Getting Started

1. Open the project in Android Studio (Arctic Fox or newer).
2. Ensure Android SDK 36 and Java 11 compatibility are configured.
3. Sync project with Gradle files.
4. Run the app on an Android emulator or physical device.
