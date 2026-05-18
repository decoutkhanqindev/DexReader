# -----------------------------------------------------------------------
# Debugging: preserve line numbers in crash stack traces
# -----------------------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# -----------------------------------------------------------------------
# Kotlin: keep metadata so reflection-based libraries can read types
# -----------------------------------------------------------------------
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# -----------------------------------------------------------------------
# Moshi — KSP generates *JsonAdapter classes looked up by name at runtime
# -----------------------------------------------------------------------
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
# Keep all API response DTOs and their generated adapters
-keep class com.decoutkhanqindev.dexreader.data.network.api.response.** { *; }
-keep class com.decoutkhanqindev.dexreader.data.network.api.response.**JsonAdapter { *; }

# -----------------------------------------------------------------------
# Retrofit — keep annotated interface methods; Retrofit proxies them
# -----------------------------------------------------------------------
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}
-keep interface com.decoutkhanqindev.dexreader.data.network.api.ApiService { *; }

# -----------------------------------------------------------------------
# OkHttp / Okio — bundled consumer rules handle most cases; seal the rest
# -----------------------------------------------------------------------
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# -----------------------------------------------------------------------
# Firebase Firestore — DTOs are deserialized via reflection (toObject())
# All fields + no-arg constructor must survive R8
# -----------------------------------------------------------------------
-keep class com.decoutkhanqindev.dexreader.data.network.firebase.dto.** { *; }
-keepclassmembers class com.decoutkhanqindev.dexreader.data.network.firebase.dto.** {
    <init>();
    <fields>;
    <methods>;
}

# -----------------------------------------------------------------------
# Room — has bundled consumer rules; add entity package as safety net
# -----------------------------------------------------------------------
-keep class com.decoutkhanqindev.dexreader.data.local.database.entity.** { *; }

# -----------------------------------------------------------------------
# Kotlinx Coroutines — keep internal dispatcher resolution
# -----------------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# -----------------------------------------------------------------------
# Kotlinx Serialization — has bundled rules; suppress known warnings
# -----------------------------------------------------------------------
-dontwarn kotlinx.serialization.**

# -----------------------------------------------------------------------
# Hilt / Dagger — have bundled consumer rules; suppress generated warnings
# -----------------------------------------------------------------------
-dontwarn dagger.hilt.internal.**

# -----------------------------------------------------------------------
# ProfileInstaller — receiver must stay unobfuscated for Macrobenchmark
# broadcasts (DROP_SHADER_CACHE, INSTALL_PROFILE) to work at runtime
# -----------------------------------------------------------------------
-keep class androidx.profileinstaller.ProfileInstallReceiver { *; }

# -----------------------------------------------------------------------
# Timber — strip all log calls in release
# -----------------------------------------------------------------------
-assumenosideeffects class timber.log.Timber {
    public static void v(...);
    public static void d(...);
    public static void i(...);
    public static void w(...);
    public static void e(...);
}
