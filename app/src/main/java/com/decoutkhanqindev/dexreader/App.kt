package com.decoutkhanqindev.dexreader

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import okio.Path.Companion.toOkioPath
import timber.log.Timber

@HiltAndroidApp
class App : Application(), SingletonImageLoader.Factory {

  override fun onCreate() {
    super.onCreate()
    setupTimber()
  }

  override fun newImageLoader(context: PlatformContext): ImageLoader =
    ImageLoader.Builder(context)
      .memoryCache {
        MemoryCache.Builder()
          .maxSizePercent(context, percent = 0.25)
          .build()
      }
      .diskCache {
        DiskCache.Builder()
          .directory(cacheDir.resolve(IMAGE_CACHE_DIRECTORY).toOkioPath())
          .maxSizePercent(0.05)
          .minimumMaxSizeBytes(64L * 1024 * 1024)
          .maximumMaxSizeBytes(512L * 1024 * 1024)
          .build()
      }
      .build()

  private fun setupTimber() {
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
  }

  private companion object {
    const val IMAGE_CACHE_DIRECTORY = "coil3_disk_cache"
  }
}
