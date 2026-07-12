<h1 align="center">
  📗 Dex Reader 📘 
</h1>
<p align="center">
  <i>Read to enjoy - Save to remember</i>
</p>

## 📜 Description

**Dex Reader** is a modern manga reading application designed for a smooth and intuitive
experience.  
Built with **Jetpack Compose** and **Material 3**, the app follows **Clean Architecture** and **MVVM
pattern** to ensure high performance, scalability, and maintainability.  
Powered by the **MangaDex API**, it provides a vast collection of manga with seamless reading and
synchronization via **Firebase**.

## Features

- **Modern UI**: Built entirely with Jetpack Compose and Material 3.
- **Manga Explorer**: Browse latest updates, trending series, and new releases.
- **Optimized Reader**: High-performance reader with zoom, fullscreen, and pre-fetching.
- **Smart Search**: Search manga by title with real-time suggestions.
- **Library Management**: Manage favorites and track reading history.
- **Cloud Sync**: Sync your data across devices using Firebase.
- **Theme Support**: Full support for Dark and Light modes.

## 🛠 Built With

| Category                 | Technology                                                                     |
|--------------------------|--------------------------------------------------------------------------------|
| 🏛 Architecture          | Clean Architecture & MVVM                                                      |
| 🖼️ UI Framework         | [Jetpack Compose](https://developer.android.com/jetpack/compose)               |
| 🛠️ Dependency Injection | [Dagger-Hilt](https://dagger.dev/hilt/)                                        |
| 🗄️ Local Database       | [Room Database](https://developer.android.com/jetpack/androidx/releases/room)  |
| 🖼️ Image Loading        | [Coil](https://coil-kt.github.io/coil/)                                        |
| 🌐 Navigation            | [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) |
| 🔥 Backend & Auth        | [Firebase (Auth & Firestore)](https://firebase.google.com/)                    |
| 📡 Networking            | [Retrofit & Moshi](https://square.github.io/retrofit/)                         |

## 📱 Screenshots

<table style="width:100%">
  <tr>
    <th>Home Screen</th>
    <th>Manga Details</th> 
    <th>Reader Mode</th> 
  </tr>
  <tr>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/home/z6721003176267_6647fd751e8bb12a8a81d29a330f5269.jpg" width=240/></td> 
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/manga_details/z6721003374269_96e6f57fdb1e9881dfd52ba7f10dbf5c.jpg" width=240/></td>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/reader/z6721003401256_e5b9499fc06e26b21368c640e9c70ac3.jpg" width=240/></td>
  </tr>
  <tr>
    <th>Search Screen</th>
    <th>Categories</th> 
    <th>Authentication</th> 
  </tr>
  <tr>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/search/z6721003338898_1d27b1441fb04951e2e0ed531e04e537.jpg" width=240/></td> 
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/categories/z6721003199702_6b0a9842002278ec47cce44f620b0547.jpg" width=240/></td>
    <td><img src="https://raw.githubusercontent.com/decoutkhanqindev/DexReader/main/app/screenshots/auth/login/z6721028456319_1f3a9884e7847f30286cd77eec4f5f41.jpg" width=240/></td>
  </tr>
</table>

## 🎞️ Video Demo

[![video_demo](https://img.youtube.com/vi/HsdtD7xXBW8/0.jpg)](https://www.youtube.com/watch?v=HsdtD7xXBW8)

## 🚀 Getting Started

1. **Clone the project**:
   ```bash
   git clone https://github.com/decoutkhanqindev/DexReader.git
   ```
2. **Setup Firebase**:
    - Add `google-services.json` to the `app/` directory.
3. **Configure API**:
    - Add `BASE_URL` and `UPLOAD_URL` to `local.properties`.
4. **Build & Run**:
    - Use Android Studio Hedgehog or newer.

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to help improve **Dex
Reader**.

## 📝 License

This project is developed for educational and personal purposes.
