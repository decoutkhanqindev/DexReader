import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.baselineprofile)
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
  id("com.google.gms.google-services")
  id("com.google.firebase.crashlytics")
  id("com.google.firebase.firebase-perf")
}

android {
  namespace = "com.decoutkhanqindev.dexreader"
  compileSdk = 37

  defaultConfig {
    applicationId = "com.decoutkhanqindev.dexreader"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "v1.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    android.buildFeatures.buildConfig = true

    val localProps = Properties().apply {
      load(rootProject.file("local.properties").inputStream())
    }

    buildConfigField(
      type = "String",
      name = "BASE_URL",
      value = "\"${localProps.getProperty("BASE_URL")}\""
    )

    buildConfigField(
      type = "String",
      name = "UPLOAD_URL",
      value = "\"${localProps.getProperty("UPLOAD_URL")}\""
    )
  }

  val keystoreProps = Properties().apply {
    load(rootProject.file("keystore.properties").inputStream())
  }

  signingConfigs {
    create("release") {
      storeFile = file(keystoreProps.getProperty("storeFile"))
      storePassword = keystoreProps.getProperty("storePassword")
      keyAlias = keystoreProps.getProperty("keyAlias")
      keyPassword = keystoreProps.getProperty("keyPassword")
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      isDebuggable = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }

    debug {
      isMinifyEnabled = false
      isShrinkResources = false
      isDebuggable = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
  }

  androidResources {
    generateLocaleConfig = true
  }

  lint {
    disable += "Instantiatable"
  }
}

composeCompiler {
  if (providers.gradleProperty("composeCompilerReports").orNull == "true") {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  // Immutable Collections
  implementation(libs.kotlinx.collections.immutable)

  // Preferences DataStore
  implementation(libs.androidx.datastore.preferences)

  // Room Database
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room.ktx)

  // ViewModel
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.savedstate)

  // Navigation
  implementation(libs.androidx.navigation.compose)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.androidx.hilt.navigation.compose)

  // Moshi
  implementation(libs.moshi.kotlin)
  ksp(libs.moshi.kotlin.codegen)

  // Retrofit
  implementation(libs.retrofit)
  implementation(libs.converter.moshi)

  // OkHttp
  implementation(libs.okhttp)
  implementation(libs.logging.interceptor)

  // Coil
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)

  // Zoomable image for Coil
  implementation(libs.zoomable.image.coil3)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)

  // Material3 Icons Extended
  implementation(libs.androidx.material.icons.extended)

  // Firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.auth.ktx)
  implementation(libs.firebase.firestore.ktx)
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.perf)

  // Logging
  implementation(libs.timber)

  // Profile Installer — required for Macrobenchmark to install/drop baseline profile at runtime
  implementation(libs.androidx.profileinstaller)
  baselineProfile(project(":baselineprofile"))

  // Vico Charts
  implementation(libs.vico.compose.m3)
}