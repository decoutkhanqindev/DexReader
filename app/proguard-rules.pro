# -----------------------------------------------------------------------
# Crashlytics: readable stack traces.
# Required because proguard-android-optimize.txt ships this line commented out.
# -----------------------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# -----------------------------------------------------------------------
# Firebase Firestore: DTOs are mapped by reflection (toObject, @PropertyName),
# so their members and the runtime annotations must both survive.
# Nothing else in the build guarantees this attribute on its own.
# -----------------------------------------------------------------------
-keepattributes RuntimeVisibleAnnotations
-keep class com.decoutkhanqindev.dexreader.data.network.firebase.dto.** { *; }

# -----------------------------------------------------------------------
# Navigation Compose: type-safe routes are resolved through the class name.
# -----------------------------------------------------------------------
-keepnames @kotlinx.serialization.Serializable class com.decoutkhanqindev.dexreader.**
