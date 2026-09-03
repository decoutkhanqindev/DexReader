<h1 align="center">
  <img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/screenshots/web/feature_graphic.webp" width="100%" alt="DexReader - Read to enjoy, Save to remember">
</h1>

<p align="center">
  <i>Read to enjoy — Save to remember</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?logo=android&logoColor=white" alt="Android 7.0+">
  <img src="https://img.shields.io/badge/version-1.0.1-blue" alt="Version 1.0.1">
  <img src="https://img.shields.io/badge/languages-64-orange" alt="64 languages">
  <img src="https://img.shields.io/badge/powered%20by-MangaDex-ff6740" alt="Powered by MangaDex">
</p>

## 📥 Download

<!-- TODO: thay "#" bằng link APKPure sau khi app được duyệt -->
<p align="center">
  <a href="#">
    <img src="https://img.shields.io/badge/Get%20it%20on-APKPure-6c47ff?style=for-the-badge" alt="Get it on APKPure">
  </a>
</p>

Requires Android 7.0 (API 24) or newer.

## 📖 About

**DexReader** is a manga reader for Android built on the open
[MangaDex](https://mangadex.org) API. Browse thousands of titles, read them in a
full-screen reader, and let the app remember exactly where you stopped — down to the page.

Everything is free, there are no ads, and your library follows you across devices
once you sign in.

## ✨ Features

### Browse and discover
- Home feed with **Trending**, **Latest Updates**, **New Releases** and **Top Rated**
- Explore by **genre, theme, format and content type** — each category shown with real cover art
- Sort and filter any list by publication status and content rating
- Search any title by name

### A reader made for long sessions
- Swipe smoothly through chapter pages
- Pinch to **zoom** into the artwork
- Jump to the previous or next chapter without leaving the page
- Pick the **translation language** per title

### Never lose your place
- **Favorite** the series you follow
- **Reading history** remembers the exact page you stopped on
- Resume any chapter with a single tap
- Reset a chapter's progress whenever you want

### See your reading habits
- Reading-time charts by **day, week, month and year**
- Totals for today, this week and all time

### Made to fit you
- Interface available in **64 languages**
- **Light and dark** theme
- Choose the language your titles and chapters load in
- Sign in to sync favorites, history and statistics across devices

## 📱 Screenshots

<table align="center">
  <tr>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/screenshots/web/onboarding_01_discover.webp" width="230" alt="Home"></td>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/screenshots/web/onboarding_02_browse.webp" width="230" alt="Browse"></td>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/screenshots/web/onboarding_03_read.webp" width="230" alt="Reader"></td>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/screenshots/web/onboarding_04_track.webp" width="230" alt="Track"></td>
  </tr>
</table>

## 🎞️ Video demo

[![Watch the demo](https://img.youtube.com/vi/HsdtD7xXBW8/0.jpg)](https://www.youtube.com/watch?v=HsdtD7xXBW8)

## 🔒 Privacy

DexReader stores your account, favorites, reading history and statistics in Firebase so
they can sync between devices. Nothing else is collected.
Read the full [Privacy Policy](https://decoutkhanqindev.github.io/DexReader/privacy-policy).

## ⚖️ Disclaimer

DexReader is an independent, unofficial client. All manga content, cover art and chapter
data are provided by **MangaDex** and belong to their respective creators and publishers.
DexReader does not host or upload any content.

---

<details>
<summary><b>🛠 For developers</b></summary>

<br>

Built with Jetpack Compose and Material 3, following Clean Architecture (domain / data /
presentation) with MVVM.

| Category                 | Technology                                                                     |
|--------------------------|--------------------------------------------------------------------------------|
| 🏛 Architecture          | Clean Architecture & MVVM                                                      |
| 🖼️ UI Framework         | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material 3  |
| 🛠️ Dependency Injection | [Dagger-Hilt](https://dagger.dev/hilt/)                                        |
| 🗄️ Local Database       | [Room](https://developer.android.com/jetpack/androidx/releases/room)           |
| 🖼️ Image Loading        | [Coil](https://coil-kt.github.io/coil/)                                        |
| 🌐 Navigation            | [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) |
| 🔥 Backend & Auth        | [Firebase (Auth & Firestore)](https://firebase.google.com/)                    |
| 📡 Networking            | [Retrofit & Moshi](https://square.github.io/retrofit/)                         |
| 📊 Charts                | [Vico](https://github.com/patrykandpatrick/vico)                               |
| ⚡ Startup                | Baseline Profiles + Macrobenchmark                                             |

### Getting started

1. **Clone the project**
   ```bash
   git clone https://github.com/decoutkhanqindev/DexReader.git
   ```
2. **Set up Firebase** — add `google-services.json` to the `app/` directory.
3. **Configure the API** — add `BASE_URL` and `UPLOAD_URL` to `local.properties`.
4. **Build and run** with a recent Android Studio.

To build a signed release, add `keystore.properties` to the project root with
`storeFile`, `storePassword`, `keyAlias` and `keyPassword`.

### Contributing

Contributions are welcome — feel free to open an issue or a pull request.

</details>

## 📝 License

Developed for educational and personal purposes.
